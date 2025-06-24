import SwiftUI

struct ContentView: View {
    @State private var showExercises = false
    @State private var showBattle = false
    @State private var showLeaderboard = false
    @State private var showSettings = false
    @ObservedObject private var localizationManager = LocalizationManager.shared
    
    var body: some View {
        ZStack {
            // Tło gradientowe identyczne jak w Android (bg_gradient.xml)
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
                // Header z logami uniwersytetu (jak w Android)
                HStack {
                    // Logo PRz w lewym górnym rogu
                    Button(action: {
                        if let url = URL(string: "https://w.prz.edu.pl") {
                            UIApplication.shared.open(url)
                        }
                    }) {
                        Image("prz_logo") // Placeholder dla logo PRz
                            .resizable()
                            .scaledToFit()
                            .frame(width: 150, height: 60)
                    }
                    
                    Spacer()
                    
                    // Logo WEiI w prawym górnym rogu  
                    Button(action: {
                        if let url = URL(string: "https://weii.prz.edu.pl") {
                            UIApplication.shared.open(url)
                        }
                    }) {
                        Image("weii_logo") // Placeholder dla logo WEiI
                            .resizable()
                            .scaledToFit()
                            .frame(width: 150, height: 60)
                    }
                }
                .padding(.horizontal, 16)
                .padding(.top, 16)
                
                // Logo aplikacji i nazwa (jak w Android)
                HStack(alignment: .top, spacing: 16) {
                    // Logo aplikacji po lewej stronie (120x120dp)
                    Image("logo") // Logo aplikacji z drawable/logo.xml
                        .resizable()
                        .scaledToFit()
                        .frame(width: 120, height: 120)
                        .padding(.leading, 32)
                    
                    VStack(alignment: .leading, spacing: 4) {
                        // Nazwa aplikacji
                        Text("RUTMath")
                            .font(.system(size: 34, weight: .bold))
                            .foregroundColor(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0)) // accent_text
                            .padding(.top, 16)
                        
                        // Opis uniwersytetu
                        Text("fragment_menu_university".localized)
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                            .multilineTextAlignment(.leading)
                    }
                    
                    Spacer()
                }
                .padding(.top, 24)
                
                // Menu główne (identyczne jak w Android)
                VStack(spacing: 16) {
                    // Przycisk "ĆWICZENIA" / "modesButton"
                    AndroidMenuButton(
                        title: "menu_exercises".localized,
                        icon: "ic_dumbbell_solid",
                        action: { showExercises = true }
                    )
                    
                    // Przycisk "POJEDYNEK" / "pvpButton"  
                    AndroidMenuButton(
                        title: "menu_battle".localized,
                        icon: "ic_gamepad_solid",
                        action: { showBattle = true }
                    )
                    
                    // Przycisk "WYNIKI" / "leaderboardButton"
                    AndroidMenuButton(
                        title: "menu_leaderboard".localized,
                        icon: "ic_trophy_solid", 
                        action: { showLeaderboard = true }
                    )
                    
                    // Przycisk "USTAWIENIA" / "settingsButton"
                    AndroidMenuButton(
                        title: "menu_settings".localized,
                        icon: "ic_settings_24dp",
                        action: { showSettings = true }
                    )
                }
                .padding(.horizontal, 16)
                .padding(.top, 16)
                
                Spacer()
            }
        }
        .navigationBarHidden(true)
        .fullScreenCover(isPresented: $showExercises) {
            PlayerChoiceView()
        }
        .fullScreenCover(isPresented: $showBattle) {
            PlayerNamesView()
        }
        .fullScreenCover(isPresented: $showLeaderboard) {
            LeaderboardView()
        }
        .fullScreenCover(isPresented: $showSettings) {
            SettingsView()
        }
    }
}

// Przycisk menu identyczny jak w Android (LinearLayout z ImageView + TextView)
struct AndroidMenuButton: View {
    let title: String
    let icon: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 0) {
                // Ikona po lewej stronie (100x100dp z bg_in_tile_menu)
                ZStack {
                    // Tło ikony - pomarańczowe z zaokrąglonymi rogami jak bg_in_tile_menu
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color(red: 0xF8/255.0, green: 0xA9/255.0, blue: 0x5D/255.0)) // accent color
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(Color(red: 0xd5/255.0, green: 0x95/255.0, blue: 0x59/255.0), lineWidth: 3) // accent_stroke
                        )
                        .frame(width: 100, height: 100)
                    
                    // Ikona SF Symbols zamiast drawable
                    Image(systemName: getSystemIcon(for: icon))
                        .font(.system(size: 32, weight: .medium))
                        .foregroundColor(.white)
                }
                
                // Tekst po prawej stronie (200dp szerokości)
                ZStack {
                    // Tło tekstu - ciemniejsze pomarańczowe jak bg_in_tile_mode_dark
                    RoundedRectangle(cornerRadius: 20)
                        .fill(Color(red: 0xe7/255.0, green: 0x9b/255.0, blue: 0x54/255.0)) // accent_dark
                        .overlay(
                            RoundedRectangle(cornerRadius: 20)
                                .stroke(Color(red: 0xcc/255.0, green: 0x8d/255.0, blue: 0x52/255.0), lineWidth: 4) // accent_stroke_dark
                        )
                        .frame(width: 200, height: 100)
                    
                    Text(title)
                        .font(.system(size: 24, weight: .bold))
                        .foregroundColor(.white)
                        .textCase(.uppercase)
                        .multilineTextAlignment(.center)
                        .frame(width: 180) // Mniejsza niż container żeby było miejsce
                }
            }
        }
        .buttonStyle(PlainButtonStyle())
        .frame(width: 300, height: 100) // Całkowity rozmiar przycisku
    }
    
    // Mapowanie ikon Android drawable na SF Symbols
    private func getSystemIcon(for iconName: String) -> String {
        switch iconName {
        case "ic_dumbbell_solid":
            return "dumbbell.fill"
        case "ic_gamepad_solid":
            return "gamecontroller.fill"
        case "ic_trophy_solid":
            return "trophy.fill"
        case "ic_settings_24dp":
            return "gearshape.fill"
        default:
            return "questionmark"
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
