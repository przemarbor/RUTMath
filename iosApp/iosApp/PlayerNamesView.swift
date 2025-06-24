import SwiftUI

struct PlayerNamesView: View {
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject private var localizationManager = LocalizationManager.shared
    @State private var player1Name = ""
    @State private var player2Name = ""
    @State private var showBattle = false
    @State private var showAlert = false
    @State private var player1Error = ""
    @State private var player2Error = ""
    @State private var showPlayer1Error = false
    @State private var showPlayer2Error = false
    
    var body: some View {
        ZStack {
            // Tło gradientowe identyczne jak w Android (bg_gradient)
            LinearGradient(
                gradient: Gradient(colors: [
                    Color(red: 0x98/255.0, green: 0xA8/255.0, blue: 0xCE/255.0).opacity(0.91), // accent_2
                    Color.white,
                    Color(red: 0x98/255.0, green: 0xA8/255.0, blue: 0xCE/255.0).opacity(0.91)  // accent_2
                ]),
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()
            
            // Tło obrazkowe (bg_image)
            Color.blue.opacity(0.1)
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Header z przyciskiem powrotu w lewym górnym rogu
                HStack {
                    Button(action: {
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Image(systemName: "chevron.left")
                            .font(.title2)
                            .foregroundColor(.black)
                    }
                    Spacer()
                }
                .padding(.horizontal)
                .padding(.top)
                
                // Instrukcja na górze (marginTop="32dp")
                Text("fragment_player_names_instruction".localized)
                    .font(.system(size: 18))  // textSize="18sp"
                    .foregroundColor(.black)
                    .multilineTextAlignment(.center)
                    .padding(.top, 32)
                
                // TextInputLayout 1 - Player1 (marginTop="16dp")
                VStack(alignment: .leading, spacing: 0) {
                    ZStack(alignment: .leading) {
                        RoundedRectangle(cornerRadius: 4)
                            .stroke(showPlayer1Error ? Color.red : Color.gray, lineWidth: 1)
                            .frame(height: 56)
                        
                        if player1Name.isEmpty {
                            Text("fragment_player_names_nickname_1".localized)
                                .foregroundColor(.gray)
                                .padding(.horizontal, 12)
                                .allowsHitTesting(false)
                        }
                        
                        TextField("", text: $player1Name)
                            .font(.system(size: 16))
                            .padding(.horizontal, 12)
                            .onChange(of: player1Name) { _ in
                                showPlayer1Error = false
                            }
                    }
                    
                    if showPlayer1Error {
                        Text(player1Error)
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding(.top, 4)
                            .padding(.horizontal, 12)
                    }
                }
                .padding(.horizontal, 16) // marginStart="16dp" marginEnd="16dp"
                .padding(.top, 16)
                
                // TextInputLayout 2 - Player2 (marginTop="16dp")
                VStack(alignment: .leading, spacing: 0) {
                    ZStack(alignment: .leading) {
                        RoundedRectangle(cornerRadius: 4)
                            .stroke(showPlayer2Error ? Color.red : Color.gray, lineWidth: 1)
                            .frame(height: 56)
                        
                        if player2Name.isEmpty {
                            Text("fragment_player_names_nickname_2".localized)
                                .foregroundColor(.gray)
                                .padding(.horizontal, 12)
                                .allowsHitTesting(false)
                        }
                        
                        TextField("", text: $player2Name)
                            .font(.system(size: 16))
                            .padding(.horizontal, 12)
                            .onChange(of: player2Name) { _ in
                                showPlayer2Error = false
                            }
                    }
                    
                    if showPlayer2Error {
                        Text(player2Error)
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding(.top, 4)
                            .padding(.horizontal, 12)
                    }
                }
                .padding(.horizontal, 16) // marginStart="16dp" marginEnd="16dp"
                .padding(.top, 16)
                
                Spacer()
                
                // Przycisk Start w prawym dolnym rogu (marginEnd="16dp" marginBottom="16dp")
                HStack {
                    Spacer()
                    Button("fragment_player_names_start".localized) {
                        startBattle()
                    }
                    .font(.system(size: 16))  // textSize="16sp"
                    .foregroundColor(.white)
                    .frame(width: 140, height: 60)  // layout_width="140dp" layout_height="60dp"
                    .background(
                        RoundedRectangle(cornerRadius: 4)
                            .fill(Color(red: 0xF8/255.0, green: 0xA9/255.0, blue: 0x5D/255.0)) // Widget.AppCompat.Button.Colored
                    )
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 16)
            }
        }
        .navigationBarHidden(true)
        .fullScreenCover(isPresented: $showBattle) {
            BattleView(player1Name: player1Name, player2Name: player2Name)
        }
        .onAppear {
            // Załaduj ostatnie nazwy jak w Android
            player1Name = UserDefaults.standard.string(forKey: "last_nickname_1") ?? ""
            player2Name = UserDefaults.standard.string(forKey: "last_nickname_2") ?? ""
        }
    }
    
    private func startBattle() {
        // Validacja jak w Android
        var hasError = false
        
        if player1Name.isEmpty {
            player1Error = "nick_empty".localized
            showPlayer1Error = true
            hasError = true
        }
        
        if player2Name.isEmpty {
            player2Error = "nick_empty".localized
            showPlayer2Error = true
            hasError = true
        }
        
        if hasError {
            return
        }
        
        // Zapisz nazwy jak w Android
        UserDefaults.standard.set(player1Name, forKey: "last_nickname_1")
        UserDefaults.standard.set(player2Name, forKey: "last_nickname_2")
        
        // Nawiguj do BattleGameFragment
        showBattle = true
    }
}

struct PlayerNamesView_Previews: PreviewProvider {
    static var previews: some View {
        PlayerNamesView()
    }
} 