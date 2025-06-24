import Foundation
import SwiftUI

class LocalizationManager: ObservableObject {
    static let shared = LocalizationManager()
    
    @Published var currentLanguage: Language {
        didSet {
            UserDefaults.standard.set(currentLanguage.rawValue, forKey: "selected_language")
            // Notify about language change - SwiftUI will automatically refresh views
            objectWillChange.send()
        }
    }
    
    private init() {
        let savedLanguage = UserDefaults.standard.string(forKey: "selected_language") ?? "en"
        self.currentLanguage = Language(rawValue: savedLanguage) ?? .english
    }
    
    func localizedString(for key: String) -> String {
        guard let path = Bundle.main.path(forResource: currentLanguage.rawValue, ofType: "lproj"),
              let bundle = Bundle(path: path) else {
            // Fallback to English if translation not found
            guard let path = Bundle.main.path(forResource: "en", ofType: "lproj"),
                  let bundle = Bundle(path: path) else {
                return key // Return key if even English is not found
            }
            return NSLocalizedString(key, bundle: bundle, comment: "")
        }
        
        return NSLocalizedString(key, bundle: bundle, comment: "")
    }
}

// Extension for easier usage
extension String {
    var localized: String {
        return LocalizationManager.shared.localizedString(for: self)
    }
    
    func localized(with arguments: CVarArg...) -> String {
        return String(format: self.localized, arguments: arguments)
    }
}

// SwiftUI View extension for reactive localization
extension View {
    func localizedText(_ key: String) -> some View {
        Text(key.localized)
    }
} 