import SwiftUI

struct PlayerChoiceView: View {
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject private var localizationManager = LocalizationManager.shared
    
    @State private var players: [PlayerModel] = []
    @State private var showAddPlayerAlert = false
    @State private var newPlayerName = ""
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var selectedPlayer: PlayerModel?
    
    var body: some View {
        ZStack {
            // Tło gradientowe identyczne jak w menu głównym
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
                // Header z przyciskiem powrotu
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
                
                // Instrukcja na górze
                Text("fragment_choose_player_instruction".localized)
                    .font(.system(size: 20, weight: .bold))
                    .foregroundColor(.black)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 16)
                    .padding(.top, 8)
                
                // Lista graczy lub komunikat o pustej liście
                if players.isEmpty {
                    VStack {
                        Spacer()
                        Text("empty".localized)
                            .font(.headline)
                            .foregroundColor(.gray)
                        Spacer()
                    }
                } else {
                    ScrollView {
                        LazyVStack(spacing: 5) {
                            ForEach(players, id: \.id) { player in
                                PlayerRowView(player: player) {
                                    selectPlayer(player)
                                }
                            }
                        }
                        .padding(.horizontal, 16)
                        .padding(.top, 16)
                    }
                }
                
                Spacer()
            }
            
            // Floating Action Button (przycisk plus)
            VStack {
                Spacer()
                HStack {
                    Spacer()
                    Button(action: {
                        showAddPlayerAlert = true
                    }) {
                        Image(systemName: "plus")
                            .font(.title2)
                            .foregroundColor(.white)
                            .frame(width: 56, height: 56)
                            .background(
                                Circle()
                                    .fill(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                            )
                    }
                    .padding(.trailing, 16)
                    .padding(.bottom, 16)
                }
            }
        }
        .navigationBarHidden(true)
        .onAppear {
            loadPlayers()
        }
        .alert("choose_player_input".localized, isPresented: $showAddPlayerAlert) {
            TextField("", text: $newPlayerName)
            Button("ok".localized) {
                addPlayer()
            }
            Button("cancel".localized, role: .cancel) {
                newPlayerName = ""
            }
        }
        .alert("error".localized, isPresented: $showErrorAlert) {
            Button("ok".localized) { }
        } message: {
            Text(errorMessage)
        }
        .fullScreenCover(item: $selectedPlayer) { player in
            ExercisesView(selectedPlayer: player)
                .onAppear {
                    print("🟢 Showing ExercisesView for player: \(player.name)")
                }
        }
    }
    
    private func loadPlayers() {
        // Załaduj graczy z UserDefaults (symulacja bazy danych)
        print("🔵 Loading players from UserDefaults...")
        if let playersData = UserDefaults.standard.data(forKey: "saved_players"),
           let savedPlayers = try? JSONDecoder().decode([PlayerModel].self, from: playersData) {
            players = savedPlayers
            print("🔵 Loaded \(players.count) players: \(players.map { $0.name })")
        } else {
            print("🔵 No players found in UserDefaults")
        }
    }
    
    private func addPlayer() {
        guard !newPlayerName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            errorMessage = "nick_empty".localized
            showErrorAlert = true
            newPlayerName = ""
            return
        }
        
        // Sprawdź czy gracz już istnieje
        if players.contains(where: { $0.name == newPlayerName }) {
            errorMessage = "choose_player_nick_exist".localized
            showErrorAlert = true
            newPlayerName = ""
            return
        }
        
        // Dodaj nowego gracza
        let newPlayer = PlayerModel(name: newPlayerName)
        players.append(newPlayer)
        savePlayers()
        newPlayerName = ""
    }
    
    private func savePlayers() {
        if let encoded = try? JSONEncoder().encode(players) {
            UserDefaults.standard.set(encoded, forKey: "saved_players")
        }
    }
    
    private func selectPlayer(_ player: PlayerModel) {
        print("🔴 Player selected: \(player.name)")
        selectedPlayer = player
        print("🔴 selectedPlayer set to: \(selectedPlayer?.name ?? "nil")")
    }
}

struct PlayerRowView: View {
    let player: PlayerModel
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack {
                Text(player.name)
                    .font(.system(size: 20, weight: .bold))
                    .foregroundColor(.white)
                    .padding(.leading, 20)
                
                Spacer()
            }
            .frame(height: 60)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color(red: 0xF8/255.0, green: 0xA9/255.0, blue: 0x5D/255.0)) // accent color
                    .overlay(
                        RoundedRectangle(cornerRadius: 16)
                            .stroke(Color(red: 0xd5/255.0, green: 0x95/255.0, blue: 0x59/255.0), lineWidth: 3) // accent_stroke
                    )
            )
        }
        .buttonStyle(PlainButtonStyle())
        .padding(.horizontal, 5)
    }
}

// PlayerModel został przeniesiony do SharedComponents.swift

struct PlayerChoiceView_Previews: PreviewProvider {
    static var previews: some View {
        PlayerChoiceView()
    }
} 