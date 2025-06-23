import SwiftUI

// MARK: - Shared Models
struct PlayerModel: Codable, Identifiable {
    let id = UUID()
    let name: String
    
    enum CodingKeys: String, CodingKey {
        case name
    }
}

enum Language: String, CaseIterable {
    case english = "en"
    case polish = "pl"
    case portuguese = "pt"
    case french = "fr"
    case spanish = "es"
    case greek = "el"
    
    var simpleName: String {
        switch self {
        case .english: return "English"
        case .polish: return "Polski"
        case .portuguese: return "Português"
        case .french: return "Français"
        case .spanish: return "Español"
        case .greek: return "Ελληνικά"
        }
    }
    
    var name: String {
        switch self {
        case .english: return "English"
        case .polish: return "Polski"
        case .portuguese: return "Português"
        case .french: return "Français"
        case .spanish: return "Español"
        case .greek: return "Ελληνικά"
        }
    }
    
    var nativeName: String {
        switch self {
        case .english: return "English"
        case .polish: return "Polski"
        case .portuguese: return "Português"
        case .french: return "Français"
        case .spanish: return "Español"
        case .greek: return "Ελληνικά"
        }
    }
    
    var flag: String {
        switch self {
        case .english: return "🇬🇧"
        case .polish: return "🇵🇱"
        case .portuguese: return "🇵🇹"
        case .french: return "🇫🇷"
        case .spanish: return "🇪🇸"
        case .greek: return "🇬🇷"
        }
    }
}

// MARK: - Button Styles
struct PrimaryButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.headline)
            .fontWeight(.semibold)
            .foregroundColor(.white)
            .padding(.horizontal, 30)
            .padding(.vertical, 15)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color.orange)
                    .opacity(configuration.isPressed ? 0.8 : 1.0)
            )
    }
}

struct SecondaryButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.headline)
            .fontWeight(.semibold)
            .foregroundColor(.white)
            .padding(.horizontal, 30)
            .padding(.vertical, 15)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color.gray.opacity(0.6))
                    .opacity(configuration.isPressed ? 0.8 : 1.0)
            )
    }
}

// MARK: - Custom Views
struct GradientBackground: View {
    var body: some View {
        LinearGradient(
            gradient: Gradient(colors: [Color.blue.opacity(0.8), Color.purple.opacity(0.6)]),
            startPoint: .topLeading,
            endPoint: .bottomTrailing
        )
        .ignoresSafeArea()
    }
}

struct BackButton: View {
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Image(systemName: "chevron.left")
                .font(.title2)
                .foregroundColor(.white)
        }
    }
}

struct HeaderView: View {
    let title: String
    let onBack: () -> Void
    
    var body: some View {
        HStack {
            BackButton(action: onBack)
            
            Spacer()
            
            Text(title)
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.white)
            
            Spacer()
            
            // Placeholder for balance
            Color.clear.frame(width: 24)
        }
        .padding(.horizontal)
        .padding(.top)
    }
}

// MARK: - Extensions
extension Color {
    static let rutMathOrange = Color(red: 0.96, green: 0.60, blue: 0.24)
    static let rutMathBlue = Color(red: 0.20, green: 0.40, blue: 0.80)
    static let rutMathPurple = Color(red: 0.50, green: 0.20, blue: 0.80)
}

// MARK: - Constants
struct AppConstants {
    static let defaultAnimationDuration: Double = 0.3
    static let cornerRadius: CGFloat = 12
    static let largePadding: CGFloat = 20
    static let mediumPadding: CGFloat = 16
    static let smallPadding: CGFloat = 8
}

// MARK: - Theme
struct RUTMathTheme {
    static let primaryColor = Color.orange
    static let secondaryColor = Color.blue
    static let accentColor = Color.white
    static let backgroundColor = Color.black.opacity(0.3)
    
    static let titleFont = Font.title2.weight(.bold)
    static let headlineFont = Font.headline.weight(.semibold)
    static let bodyFont = Font.body
    static let captionFont = Font.caption
}

// MARK: - SimpleKeyboardView
struct SimpleKeyboardView: View {
    let onKeyPress: (String) -> Void
    
    var body: some View {
        VStack(spacing: 12) {
            // Pierwsza linia: 1, 2, 3
            HStack(spacing: 12) {
                KeyboardButton(key: "1", onKeyPress: onKeyPress)
                KeyboardButton(key: "2", onKeyPress: onKeyPress)
                KeyboardButton(key: "3", onKeyPress: onKeyPress)
            }
            
            // Druga linia: 4, 5, 6
            HStack(spacing: 12) {
                KeyboardButton(key: "4", onKeyPress: onKeyPress)
                KeyboardButton(key: "5", onKeyPress: onKeyPress)
                KeyboardButton(key: "6", onKeyPress: onKeyPress)
            }
            
            // Trzecia linia: 7, 8, 9
            HStack(spacing: 12) {
                KeyboardButton(key: "7", onKeyPress: onKeyPress)
                KeyboardButton(key: "8", onKeyPress: onKeyPress)
                KeyboardButton(key: "9", onKeyPress: onKeyPress)
            }
            
            // Czwarta linia: backspace, 0, checkmark
            HStack(spacing: 12) {
                KeyboardButton(key: "⌫", onKeyPress: onKeyPress, isSpecial: true)
                KeyboardButton(key: "0", onKeyPress: onKeyPress)
                KeyboardButton(key: "✓", onKeyPress: onKeyPress, isSpecial: true)
            }
        }
    }
}

// MARK: - KeyboardButton
struct KeyboardButton: View {
    let key: String
    let onKeyPress: (String) -> Void
    let isSpecial: Bool
    
    init(key: String, onKeyPress: @escaping (String) -> Void, isSpecial: Bool = false) {
        self.key = key
        self.onKeyPress = onKeyPress
        self.isSpecial = isSpecial
    }
    
    var body: some View {
        Button(action: {
            onKeyPress(key)
        }) {
            Text(key)
                .font(.system(size: 24, weight: .bold))
                .foregroundColor(.white)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .frame(height: 60)
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color(red: 0xd5/255.0, green: 0x95/255.0, blue: 0x59/255.0), lineWidth: 2)
                        )
                )
        }
        .buttonStyle(PlainButtonStyle())
    }
}

// MARK: - ExercisesView
struct ExercisesView: View {
    @Environment(\.presentationMode) var presentationMode
    let selectedPlayer: PlayerModel
    
    @State private var showAddSubGame = false
    @State private var showMulDivGame = false
    @State private var showTableGame = false
    @State private var showUnitsGame = false
    @State private var showDivisibilityGame = false
    
    var body: some View {
        ZStack {
            // Gradient background identical to main menu
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
            
            // Background image (bg_image)
            Color.blue.opacity(0.1)
                .ignoresSafeArea()
            
            VStack(spacing: 0) {
                // Header with back button and title - better centered
                VStack(spacing: 8) {
                    HStack {
                        // Przycisk cofania po lewej stronie
                        Button(action: {
                            presentationMode.wrappedValue.dismiss()
                        }) {
                            Image(systemName: "chevron.left")
                                .font(.title2)
                                .foregroundColor(.black)
                        }
                        
                        Spacer()
                    }
                    .padding(.horizontal, 20)
                    
                    // Title centered separately
                    Text("exercises_title".localized)
                        .font(.system(size: 22, weight: .bold))
                        .foregroundColor(.black) // zmieniony na czarny
                        .multilineTextAlignment(.center)
                        .frame(maxWidth: .infinity)
                }
                .padding(.top, 10)
                .padding(.bottom, 5)
                
                // Exercise list with buttons like in main menu in ScrollView - centered
                ScrollView {
                    VStack(spacing: 12) {
                        // 1. Addition and subtraction
                        ExerciseButton(
                            title: "category_add_sub".localized,
                            icon: "plus.circle",
                            action: {
                                showAddSubGame = true
                            }
                        )
                        
                        // 2. Multiplication and division
                        ExerciseButton(
                            title: "category_mul_div".localized, 
                            icon: "multiply.circle",
                            action: {
                                showMulDivGame = true
                            }
                        )
                        
                        // 3. Podzielność
                        ExerciseButton(
                            title: "category_divisibility".localized,
                            icon: "divide.circle",
                            action: {
                                showDivisibilityGame = true
                            }
                        )
                        
                        // 4. Zamiana jednostek
                        ExerciseButton(
                            title: "category_units".localized,
                            icon: "ruler",
                            action: {
                                showUnitsGame = true
                            }
                        )
                        
                        // 5. Multiplication table
                        ExerciseButton(
                            title: "category_table".localized,
                            icon: "grid.circle",
                            action: {
                                showTableGame = true
                            }
                        )
                    }
                    .frame(maxWidth: .infinity) // Center buttons
                    .padding(.horizontal, 20)
                                          .padding(.top, 30) // increased top padding to lower buttons
                    .padding(.bottom, 30)
                }
                
                Spacer()
            }
        }
        .navigationBarHidden(true)
        .fullScreenCover(isPresented: $showAddSubGame) {
            AddSubGameView(playerName: selectedPlayer.name, operation: "category_add_sub".localized, difficulty: "divisibility_difficulty_1".localized)
        }
        .fullScreenCover(isPresented: $showMulDivGame) {
            MulDivGameView(playerName: selectedPlayer.name, operation: "category_mul_div".localized, difficulty: "divisibility_difficulty_1".localized)
        }
        .fullScreenCover(isPresented: $showTableGame) {
            GameView(playerName: selectedPlayer.name, operation: "category_table".localized, difficulty: "divisibility_difficulty_1".localized)
        }
        .fullScreenCover(isPresented: $showUnitsGame) {
            UnitsGameView(playerName: selectedPlayer.name, operation: "category_units".localized, difficulty: "divisibility_difficulty_1".localized)
        }
        .fullScreenCover(isPresented: $showDivisibilityGame) {
            DivisibilityGameView(playerName: selectedPlayer.name, operation: "category_divisibility".localized, difficulty: "divisibility_difficulty_1".localized)
        }
    }
}

// MARK: - ExerciseButton - exercise button identical to main menu
struct ExerciseButton: View {
    let title: String
    let icon: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 0) {
                // Ikona po lewej stronie (100x100dp z bg_in_tile_menu)
                ZStack {
                    // Icon background - orange with rounded corners like bg_in_tile_menu
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color(red: 0xF8/255.0, green: 0xA9/255.0, blue: 0x5D/255.0)) // accent color
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(Color(red: 0xd5/255.0, green: 0x95/255.0, blue: 0x59/255.0), lineWidth: 3) // accent_stroke
                        )
                        .frame(width: 90, height: 90)
                    
                    // Ikona SF Symbols
                    Image(systemName: icon)
                        .font(.system(size: 30, weight: .medium))
                        .foregroundColor(.white)
                }
                
                // Tekst po prawej stronie (szerokość dopasowana do ekranu)
                ZStack {
                    // Tło tekstu - ciemniejsze pomarańczowe jak bg_in_tile_mode_dark
                    RoundedRectangle(cornerRadius: 18)
                        .fill(Color(red: 0xe7/255.0, green: 0x9b/255.0, blue: 0x54/255.0)) // accent_dark
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color(red: 0xcc/255.0, green: 0x8d/255.0, blue: 0x52/255.0), lineWidth: 3) // accent_stroke_dark
                        )
                        .frame(width: 210, height: 90)
                    
                    Text(title)
                        .font(.system(size: 18, weight: .bold))
                        .foregroundColor(.white)
                        .textCase(.uppercase)
                        .multilineTextAlignment(.center)
                        .frame(width: 190) // Mniejsza niż container żeby było miejsce
                }
            }
        }
        .buttonStyle(PlainButtonStyle())
        .frame(width: 300, height: 90) // Całkowity rozmiar przycisku - powiększony
    }
} 