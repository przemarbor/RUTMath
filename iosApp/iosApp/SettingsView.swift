import SwiftUI

struct SettingsView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject private var localizationManager = LocalizationManager.shared
    @State private var selectedLanguage: Language
    @State private var maxBattleNumber = "100"
    // Removed showSaveAlert - no longer showing popup
    @State private var errorMessage = ""
    @State private var showError = false
    
    init() {
        _selectedLanguage = State(initialValue: LocalizationManager.shared.currentLanguage)
    }
    
    var body: some View {
        ZStack {
            // Gradient background identical to Android (bg_gradient)
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
                // Header with back button in top left corner
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
                
                                                        // Title "Max number for duel" at the top (marginTop="30dp")
                Text("settings_max_number".localized)
                    .font(.system(size: 24, weight: .bold))
                    .foregroundColor(.black)
                    .multilineTextAlignment(.center)
                    .padding(.top, 30)
                
                // TextInputLayout z TextInputEditText (Android Material Design)
                VStack(alignment: .leading, spacing: 0) {
                    ZStack(alignment: .leading) {
                        RoundedRectangle(cornerRadius: 4)
                            .stroke(Color.gray, lineWidth: 1)
                            .frame(height: 56)
                        
                        TextField("", text: $maxBattleNumber)
                            .keyboardType(.numberPad)
                            .font(.system(size: 22))
                            .padding(.horizontal, 12)
                            .onChange(of: maxBattleNumber) { _ in
                                showError = false
                            }
                    }
                    
                    if showError {
                        Text(errorMessage)
                            .font(.caption)
                            .foregroundColor(.red)
                            .padding(.top, 4)
                            .padding(.horizontal, 12)
                    }
                }
                .padding(.horizontal, 20)
                .padding(.top, 10)
                
                                                        // Title "Select language" (marginTop="20dp")
                Text("settings_select_language".localized)
                    .font(.system(size: 24, weight: .bold))
                    .foregroundColor(.black)
                    .multilineTextAlignment(.center)
                    .padding(.top, 20)
                
                // RadioGroup z RadioButtonami (jak w Android)
                VStack(spacing: 0) {
                    AndroidRadioButton(
                        text: "English",
                        isSelected: selectedLanguage == .english,
                        onSelect: { 
                            selectedLanguage = .english
                            updatePreviewText()
                        }
                    )
                    
                    AndroidRadioButton(
                        text: "Français",
                        isSelected: selectedLanguage == .french,
                        onSelect: { 
                            selectedLanguage = .french
                            updatePreviewText()
                        }
                    )
                    
                    AndroidRadioButton(
                        text: "Polski",
                        isSelected: selectedLanguage == .polish,
                        onSelect: { 
                            selectedLanguage = .polish
                            updatePreviewText()
                        }
                    )
                    
                    AndroidRadioButton(
                        text: "Português",
                        isSelected: selectedLanguage == .portuguese,
                        onSelect: { 
                            selectedLanguage = .portuguese
                            updatePreviewText()
                        }
                    )
                    
                    AndroidRadioButton(
                        text: "Español",
                        isSelected: selectedLanguage == .spanish,
                        onSelect: { 
                            selectedLanguage = .spanish
                            updatePreviewText()
                        }
                    )
                    
                    AndroidRadioButton(
                        text: "Ελληνικά",
                        isSelected: selectedLanguage == .greek,
                        onSelect: { 
                            selectedLanguage = .greek
                            updatePreviewText()
                        }
                    )
                }
                .padding(.horizontal, 20)
                .padding(.top, 10)
                
                Spacer()
                
                // Przycisk Save w prawym dolnym rogu (marginTop="24dp", marginEnd="18dp")
                HStack {
                    Spacer()
                    Button("save".localized) {
                        saveSettings()
                    }
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(.white)
                    .padding(.horizontal, 32)
                    .padding(.vertical, 16)
                    .background(
                        RoundedRectangle(cornerRadius: 4)
                            .fill(Color(red: 0xF8/255.0, green: 0xA9/255.0, blue: 0x5D/255.0)) // accent color jak w Android
                    )
                }
                .padding(.horizontal, 18)
                .padding(.bottom, 24)
            }
        }
        .navigationBarHidden(true)
                    // Removed confirmation popup for saving settings
        .onAppear {
            // Load saved max battle number
            maxBattleNumber = UserDefaults.standard.string(forKey: "max_battle_number") ?? "100"
            // Sync selected language with current language
            selectedLanguage = localizationManager.currentLanguage
        }
    }
    
    private func saveSettings() {
        // Walidacja liczby jak w Android (MIN_NUMBER = 5, MAX_NUMBER = 200)
        guard let number = Int(maxBattleNumber) else {
            errorMessage = "Invalid number"
            showError = true
            return
        }
        
        if number > 200 {
            errorMessage = "Max number is 200"  // W prawdziwej app: getString(R.string.settings_fragment_max_number, MAX_NUMBER)
            showError = true
            return
        }
        
        if number < 5 {
            errorMessage = "Min number is 5"   // W prawdziwej app: getString(R.string.settings_fragment_min_number, MIN_NUMBER) 
            showError = true
            return
        }
        
        showError = false
        
        // Save max battle number
        UserDefaults.standard.set(maxBattleNumber, forKey: "max_battle_number")
        
                    // Save language - change happens immediately
        localizationManager.currentLanguage = selectedLanguage
        
        // Zapisano ustawienia - bez popup, natychmiast wracamy do menu
        presentationMode.wrappedValue.dismiss()
    }
    
    private func updatePreviewText() {
        // Update preview texts like in Android setPreviewText()
        // W iOS LocalizationManager robi to automatycznie
    }
}

// RadioButton identyczny jak w Android
struct AndroidRadioButton: View {
    let text: String
    let isSelected: Bool
    let onSelect: () -> Void
    
    var body: some View {
        Button(action: onSelect) {
            HStack {
                // Radio button circle jak w Android
                ZStack {
                    Circle()
                        .stroke(Color.black, lineWidth: 2)
                        .frame(width: 20, height: 20)
                    
                    if isSelected {
                        Circle()
                            .fill(Color.black)
                            .frame(width: 12, height: 12)
                    }
                }
                
                Text(text)
                    .font(.system(size: 24)) // textSize="24sp" jak w Android
                    .foregroundColor(.black)
                    .padding(.leading, 10)
                
                Spacer()
            }
            .padding(.horizontal, 10) // layout_marginHorizontal="10dp"
            .padding(.vertical, 10)   // padding="10dp"
        }
        .buttonStyle(PlainButtonStyle())
    }
}

        // Language enum moved to SharedComponents.swift

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
} 