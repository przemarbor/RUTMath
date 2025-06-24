import SwiftUI

struct DivisibilityGameView: View {
    @Environment(\.presentationMode) var presentationMode
    
    let playerName: String
    let operation: String
    let difficulty: String
    
    @State private var currentQuestion = "Czy 12 jest podzielne przez 3?"
    @State private var userAnswer = ""
    @State private var score = 0
    @State private var questionNumber = 1
    @State private var totalQuestions = 10
    @State private var timeRemaining = 30
    @State private var isGameActive = true
    @State private var showResult = false
    @State private var isCorrect = false
    @State private var timer: Timer?
    @State private var correctAnswer = 1 // 1 = TAK, 0 = NIE
    @State private var hasAnswered = false // Czy odpowiedź została już udzielona
    
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
            
            VStack(spacing: 0) {
                // Header tylko z przyciskiem powrotu
                HStack {
                    Button(action: {
                        timer?.invalidate()
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Image(systemName: "chevron.left")
                            .font(.title2)
                            .foregroundColor(.black)
                            .frame(width: 44, height: 44)
                            .background(Color.clear)
                    }
                    
                    Spacer()
                }
                .padding(.horizontal)
                .padding(.top, 8)
                
                // Pasek postępu na samej górze
                ProgressView(value: Double(questionNumber), total: Double(totalQuestions))
                    .progressViewStyle(LinearProgressViewStyle(tint: Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0)))
                    .padding(.horizontal)
                    .padding(.top, 8)
                
                Spacer()
                
                // Obszar pytania z odpowiedzią - wycentrowany
                VStack(spacing: 30) {
                    Text(getQuestionWithAnswer())
                        .font(.system(size: 36, weight: .bold))
                        .foregroundColor(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                        .multilineTextAlignment(.center)
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 20)
                                .fill(Color.white.opacity(0.5))
                                .overlay(
                                    RoundedRectangle(cornerRadius: 20)
                                        .stroke(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0).opacity(0.5), lineWidth: 2)
                                )
                        )
                }
                .padding(.horizontal)
                
                Spacer()
                
                // Przyciski TAK/NIE zamiast klawiatury
                HStack(spacing: 40) {
                    Button(action: {
                        if isGameActive {
                            userAnswer = "0" // NIE
                            checkAnswer()
                        }
                    }) {
                        Text("divisibility_no".localized)
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .frame(width: 100, height: 70)
                            .background(
                                RoundedRectangle(cornerRadius: 16)
                                    .fill(Color.red.opacity(0.8))
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 16)
                                            .stroke(Color.red, lineWidth: 3)
                                    )
                            )
                    }
                    .buttonStyle(PlainButtonStyle())
                    .disabled(!isGameActive)
                    
                    Button(action: {
                        if isGameActive {
                            userAnswer = "1" // TAK
                            checkAnswer()
                        }
                    }) {
                        Text("divisibility_yes".localized)
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .frame(width: 100, height: 70)
                            .background(
                                RoundedRectangle(cornerRadius: 16)
                                    .fill(Color.green.opacity(0.8))
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 16)
                                            .stroke(Color.green, lineWidth: 3)
                                    )
                            )
                    }
                    .buttonStyle(PlainButtonStyle())
                    .disabled(!isGameActive)
                }
                .padding(.horizontal)
                .padding(.bottom, 50)
            }
            
            // Usunięto overlay z wynikiem
        }
        .navigationBarHidden(true)
        .onAppear {
            generateNewQuestion()
            // Usunięto timer - brak ograniczenia czasowego
        }
    }
    
    private func getQuestionWithAnswer() -> AttributedString {
        // Wyświetlaj tylko pytanie bez "?" i bez odpowiedzi użytkownika
        var attributedString = AttributedString(currentQuestion)
        
        // Kolorowanie całego pytania po udzieleniu odpowiedzi
        if hasAnswered && !userAnswer.isEmpty {
            if let userAnswerInt = Int(userAnswer), userAnswerInt == correctAnswer {
                // Całe pytanie na zielono dla poprawnej odpowiedzi
                let range = attributedString.startIndex..<attributedString.endIndex
                attributedString[range].foregroundColor = .green
            } else {
                // Całe pytanie na czerwono dla błędnej odpowiedzi
                let range = attributedString.startIndex..<attributedString.endIndex
                attributedString[range].foregroundColor = .red
            }
        }
        
        return attributedString
    }
    
    private func getAnswerText() -> String {
        if userAnswer.isEmpty {
            return "?"
        } else if userAnswer == "1" {
            return "divisibility_yes".localized
        } else if userAnswer == "0" {
            return "divisibility_no".localized
        } else {
            return "?"
        }
    }
    
    private func startTimer() {
        timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { _ in
            if timeRemaining > 0 && isGameActive {
                timeRemaining -= 1
            } else if timeRemaining == 0 {
                // Czas się skończył
                checkAnswer(timeUp: true)
            }
        }
    }
    
    private func checkAnswer(timeUp: Bool = false) {
        timer?.invalidate()
        isGameActive = false
        hasAnswered = true
        
        let userAnswerInt = Int(userAnswer) ?? -999
        isCorrect = userAnswerInt == correctAnswer && !timeUp
        
        if isCorrect {
            score += 10
            // Przechodź do następnego pytania tylko przy poprawnej odpowiedzi
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                nextQuestion()
            }
        } else {
            // Przy błędnej odpowiedzi pozwól spróbować ponownie
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                userAnswer = ""
                hasAnswered = false
                isGameActive = true
            }
        }
    }
    
    private func nextQuestion() {
        questionNumber += 1
        
        if questionNumber > totalQuestions {
            // Koniec gry
            presentationMode.wrappedValue.dismiss()
            return
        }
        
        userAnswer = ""
        hasAnswered = false
        isGameActive = true
        generateNewQuestion()
        // Usunięto timer - brak ograniczenia czasowego
    }
    
    private func generateNewQuestion() {
        let number = Int.random(in: 10...100)
        let divisor = Int.random(in: 2...9)
        currentQuestion = "Czy \(number) jest podzielne przez \(divisor)?"
        correctAnswer = (number % divisor == 0) ? 1 : 0 // 1 = TAK, 0 = NIE
    }
}

struct DivisibilityResultOverlay: View {
    let isCorrect: Bool
    let correctAnswer: Int
    let onNext: () -> Void
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.7)
                .ignoresSafeArea()
            
            VStack(spacing: 20) {
                Image(systemName: isCorrect ? "checkmark.circle.fill" : "xmark.circle.fill")
                    .font(.system(size: 80))
                    .foregroundColor(isCorrect ? .green : .red)
                
                Text(isCorrect ? "Dobrze!" : "Źle!")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                
                if !isCorrect {
                    Text("Poprawna odpowiedź: \(correctAnswer == 1 ? "TAK" : "NIE")")
                        .font(.headline)
                        .foregroundColor(.white.opacity(0.9))
                }
                
                Button("Następne") {
                    onNext()
                }
                .buttonStyle(PrimaryButtonStyle())
            }
            .padding(40)
            .background(
                RoundedRectangle(cornerRadius: 20)
                    .fill(Color.black.opacity(0.8))
            )
        }
    }
}

struct DivisibilityGameView_Previews: PreviewProvider {
    static var previews: some View {
        DivisibilityGameView(playerName: "Gracz", operation: "Łatwy 1", difficulty: "Łatwy")
    }
} 