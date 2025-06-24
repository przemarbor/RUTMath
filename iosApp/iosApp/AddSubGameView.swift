import SwiftUI

struct AddSubGameView: View {
    @Environment(\.presentationMode) var presentationMode
    
    let playerName: String
    let operation: String
    let difficulty: String
    
    @State private var currentQuestion = "5 + 3 = ?"
    @State private var userAnswer = ""
    @State private var score = 0
    @State private var questionNumber = 1
    @State private var totalQuestions = 10
    @State private var timeRemaining = 30
    @State private var isGameActive = true
    @State private var showResult = false
    @State private var isCorrect = false
    @State private var timer: Timer?
    @State private var correctAnswer = 8
    @State private var hasAnswered = false // Whether the answer has already been given
    
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
            
            VStack(spacing: 0) {
                // Header with back button only
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
                
                // Progress bar at the very top
                ProgressView(value: Double(questionNumber), total: Double(totalQuestions))
                    .progressViewStyle(LinearProgressViewStyle(tint: Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0)))
                    .padding(.horizontal)
                    .padding(.top, 8)
                
                Spacer()
                
                // Question area with answer after "=" sign - centered
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
                
                // Numeric keyboard like in multiplication table
                SimpleKeyboardView { key in
                    if isGameActive {
                        handleKeyPress(key)
                    }
                }
                .padding(.horizontal)
                .padding(.bottom, 20)
            }
            
            // Removed result overlay
        }
        .navigationBarHidden(true)
        .onAppear {
            generateNewQuestion()
            // Removed timer - no time limit
        }
    }
    
    private func getQuestionWithAnswer() -> AttributedString {
        let questionPart = currentQuestion.replacingOccurrences(of: " = ?", with: " = ")
        let displayAnswer = userAnswer.isEmpty ? "?" : userAnswer
        
        var attributedString = AttributedString(questionPart + displayAnswer)
        
        // Coloring the answer after confirmation
        if hasAnswered && !userAnswer.isEmpty {
            let answerRange = attributedString.range(of: displayAnswer)
            if let range = answerRange {
                if let userAnswerInt = Int(userAnswer), userAnswerInt == correctAnswer {
                    attributedString[range].foregroundColor = .green // Correct answer
                } else {
                    attributedString[range].foregroundColor = .red // Wrong answer
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
            if userAnswer.count < 6 { // maximum answer length
                userAnswer += key
            }
        }
    }
    
    private func startTimer() {
        timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { _ in
            if timeRemaining > 0 && isGameActive {
                timeRemaining -= 1
            } else if timeRemaining == 0 {
                // Time is up
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
            // Move to next question only on correct answer
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                nextQuestion()
            }
        } else {
            // On wrong answer allow to try again
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
        // Removed timer - no time limit
    }
    
    private func generateNewQuestion() {
        if operation.contains("+") && operation.contains("-") {
            // Dodawanie i odejmowanie mieszane
            generateMixedAddSubQuestion()
        } else if operation.contains("+") {
            // Tylko dodawanie
            generateAdditionQuestion()
        } else if operation.contains("-") {
            // Tylko odejmowanie
            generateSubtractionQuestion()
        } else {
            // Addition by default
            generateAdditionQuestion()
        }
    }
    
    private func generateAdditionQuestion() {
        let a = Int.random(in: 1...20)
        let b = Int.random(in: 1...20)
        currentQuestion = "\(a) + \(b) = ?"
        correctAnswer = a + b
    }
    
    private func generateSubtractionQuestion() {
        let a = Int.random(in: 10...50)
        let b = Int.random(in: 1...a) // ensures result won't be negative
        currentQuestion = "\(a) - \(b) = ?"
        correctAnswer = a - b
    }
    
    private func generateMixedAddSubQuestion() {
        let operationType = Int.random(in: 0...1)
        if operationType == 0 {
            generateAdditionQuestion()
        } else {
            generateSubtractionQuestion()
        }
    }
}

struct AddSubGameView_Previews: PreviewProvider {
    static var previews: some View {
        AddSubGameView(playerName: "Gracz", operation: "+ 5", difficulty: "Średni")
    }
} 