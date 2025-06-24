import SwiftUI

struct MulDivGameView: View {
    @Environment(\.presentationMode) var presentationMode
    
    let playerName: String
    let operation: String
    let difficulty: String
    
    @State private var currentQuestion = "2 × 3 = ?"
    @State private var userAnswer = ""
    @State private var score = 0
    @State private var questionNumber = 1
    @State private var totalQuestions = 10
    @State private var timeRemaining = 30
    @State private var isGameActive = true
    @State private var showResult = false
    @State private var isCorrect = false
    @State private var timer: Timer?
    @State private var correctAnswer = 6
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
                
                // Obszar pytania z odpowiedzią po znaku "=" - wycentrowany
                VStack(spacing: 30) {
                    Text(getQuestionWithAnswer())
                        .font(.system(size: 48, weight: .bold))
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
                
                // Klawiatura numeryczna jak w tabliczce mnożenia
                SimpleKeyboardView { key in
                    if isGameActive {
                        handleKeyPress(key)
                    }
                }
                .padding(.horizontal)
                .padding(.bottom, 20)
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
        let questionPart = currentQuestion.replacingOccurrences(of: " = ?", with: " = ")
        let displayAnswer = userAnswer.isEmpty ? "?" : userAnswer
        
        var attributedString = AttributedString(questionPart + displayAnswer)
        
        // Kolorowanie odpowiedzi po zatwierdzeniu
        if hasAnswered && !userAnswer.isEmpty {
            let answerRange = attributedString.range(of: displayAnswer)
            if let range = answerRange {
                if let userAnswerInt = Int(userAnswer), userAnswerInt == correctAnswer {
                    attributedString[range].foregroundColor = .green // Prawidłowa odpowiedź
                } else {
                    attributedString[range].foregroundColor = .red // Błędna odpowiedź
                }
            }
        }
        
        return attributedString
    }
    
    private func handleKeyPress(_ key: String) {
        switch key {
        case "⌫":
            if !userAnswer.isEmpty {
                userAnswer = String(userAnswer.dropLast())
            }
        case "✓":
            if !userAnswer.isEmpty {
                checkAnswer()
            }
        default:
            if userAnswer.count < 6 { // maksymalna długość odpowiedzi
                userAnswer += key
            }
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
        if operation.contains("×") && operation.contains("÷") {
            // Mnożenie i dzielenie mieszane
            generateMixedMulDivQuestion()
        } else if operation.contains("×") {
            // Tylko mnożenie
            generateMultiplicationQuestion()
        } else if operation.contains("÷") {
            // Tylko dzielenie
            generateDivisionQuestion()
        } else {
            // Domyślnie mnożenie
            generateMultiplicationQuestion()
        }
    }
    
    private func generateMultiplicationQuestion() {
        let a = Int.random(in: 1...12)
        let b = Int.random(in: 1...12)
        currentQuestion = "\(a) × \(b) = ?"
        correctAnswer = a * b
    }
    
    private func generateDivisionQuestion() {
        let a = Int.random(in: 1...12)
        let b = Int.random(in: 1...12)
        let product = a * b
        currentQuestion = "\(product) ÷ \(a) = ?"
        correctAnswer = b
    }
    
    private func generateMixedMulDivQuestion() {
        let operationType = Int.random(in: 0...1)
        if operationType == 0 {
            generateMultiplicationQuestion()
        } else {
            generateDivisionQuestion()
        }
    }
}

struct MulDivGameView_Previews: PreviewProvider {
    static var previews: some View {
        MulDivGameView(playerName: "Gracz", operation: "× 5", difficulty: "Średni")
    }
} 