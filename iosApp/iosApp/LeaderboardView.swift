import SwiftUI

struct LeaderboardView: View {
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject private var localizationManager = LocalizationManager.shared
    @State private var scores: [PlayerScore] = []
    
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
                
                if scores.isEmpty {
                    // Pusty stan - identyczny jak w Android emptyListInstruction
                    VStack {
                        Spacer()
                        
                        Text("leaderboard_empty".localized)
                            .font(.system(size: 18, weight: .bold))  // textSize="18sp" textStyle="bold"
                            .foregroundColor(Color(red: 0xF8/255.0, green: 0xA9/255.0, blue: 0x5D/255.0))  // textColor="@color/accent"
                            .multilineTextAlignment(.center)
                            .textCase(.uppercase)  // textAllCaps="true"
                            .padding(.horizontal, 16)  // marginStart="16dp" marginEnd="16dp"
                        
                        Spacer()
                    }
                } else {
                    VStack(spacing: 0) {
                        // Tytuł po lewej stronie jak w Android scoreBoardInfo
                        HStack {
                            Text("leaderboard_title".localized)
                                .font(.system(size: 18))  // textSize="18sp"
                                .fontWeight(.bold)
                                .foregroundColor(.black)
                                .textCase(.uppercase)  // textAllCaps="true"
                            Spacer()
                        }
                        .padding(.horizontal, 8)  // marginStart="8dp"
                        .padding(.top, 8)         // marginTop="8dp"
                        
                        // RecyclerView - lista wyników
                        ScrollView {
                            LazyVStack(spacing: 0) {
                                ForEach(Array(scores.enumerated()), id: \.element.id) { index, score in
                                    AndroidScoreRow(
                                        position: index + 1,
                                        score: score
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        .navigationBarHidden(true)
        .onAppear {
            loadScores()
        }
    }
    
    private func loadScores() {
        // Pusta lista wyników - w prawdziwej aplikacji pobieralibyśmy z bazy danych
        // sortedBy { it.score }.reversed() jak w Android
        scores = []
    }
}

// ScoreboardAdapter item identyczny jak w Android
struct AndroidScoreRow: View {
    let position: Int
    let score: PlayerScore
    
    var body: some View {
        HStack {
            // Pozycja + nazwa gracza po lewej (jak w Android score_item.xml)
            Text("\(position). \(score.playerName)")
                .font(.body)
                .fontWeight(.medium)
                .foregroundColor(.black)
            
            Spacer()
            
            // Wynik po prawej (jak w Android)
            Text("\(score.score)")
                .font(.body)
                .foregroundColor(.black)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(
            // Tło jak w Android RecyclerView item
            Color.white.opacity(0.1)
        )
    }
}

struct PlayerScore: Identifiable {
    let id: Int
    let playerName: String
    let score: Int
}

struct LeaderboardView_Previews: PreviewProvider {
    static var previews: some View {
        LeaderboardView()
    }
} 