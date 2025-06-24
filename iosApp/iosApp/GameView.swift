import SwiftUI

struct GameView: View {
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
        // Sprawdź typ ćwiczenia na podstawie operation stringa
        if operation.contains("Tabliczka Mnożenia") || operation.contains("Tabliczka") {
            generateMultiplicationQuestion()
        } else if operation.contains("Czas") || operation.contains("Time") {
            generateTimeQuestion()
        } else if operation.contains("Długość") || operation.contains("Length") {
            generateLengthQuestion()
        } else if operation.contains("Waga") || operation.contains("Weight") {
            generateWeightQuestion()
        } else if operation.contains("Powierzchnia") || operation.contains("Surface") {
            generateSurfaceQuestion()
        } else if operation.contains("Wszystkie") || operation.contains("All") || operation.contains("jednostek") || operation.contains("units") {
            generateUnitsQuestion()
        } else if operation.contains("×") || operation.contains("multiply") || operation.contains("mnażenie") {
            generateMultiplicationQuestion()
        } else if operation.contains("÷") || operation.contains("division") || operation.contains("dzielenie") {
            generateDivisionQuestion()
        } else if operation.contains("podzielność") || operation.contains("divisibility") {
            generateDivisibilityQuestion()
        } else {
            generateBasicMathQuestion()
        }
    }
    
    private func generateBasicMathQuestion() {
        let a = Int.random(in: 1...20)
        let b = Int.random(in: 1...20)
        let operation = Int.random(in: 0...1)
        
        if operation == 0 {
            currentQuestion = "\(a) + \(b) = ?"
            correctAnswer = a + b
        } else {
            currentQuestion = "\(a) - \(b) = ?"
            correctAnswer = a - b
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
    
    private func generateDivisibilityQuestion() {
        let number = Int.random(in: 10...100)
        let divisor = Int.random(in: 2...9)
        currentQuestion = "Czy \(number) jest podzielne przez \(divisor)?"
        correctAnswer = (number % divisor == 0) ? 1 : 0 // 1 = tak, 0 = nie
    }
    
    private func generateUnitsQuestion() {
        let questionTypes = [
            generateTimeQuestion,
            generateLengthQuestion,
            generateWeightQuestion,
            generateSurfaceQuestion
        ]
        
        let selectedType = questionTypes.randomElement()!
        selectedType()
    }
    
    private func generateTimeQuestion() {
        let conversions = [
            (from: "minut", to: "sekund", factor: 60),
            (from: "godzin", to: "minut", factor: 60),
            (from: "dni", to: "godzin", factor: 24),
            (from: "tygodni", to: "dni", factor: 7)
        ]
        
        let conversion = conversions.randomElement()!
        let value = Int.random(in: 1...10)
        currentQuestion = "\(value) \(conversion.from) = ? \(conversion.to)"
        correctAnswer = value * conversion.factor
    }
    
    private func generateLengthQuestion() {
        let conversions = [
            (from: "m", to: "cm", factor: 100),
            (from: "km", to: "m", factor: 1000),
            (from: "m", to: "mm", factor: 1000),
            (from: "cm", to: "mm", factor: 10)
        ]
        
        let conversion = conversions.randomElement()!
        let value = Int.random(in: 1...10)
        currentQuestion = "\(value) \(conversion.from) = ? \(conversion.to)"
        correctAnswer = value * conversion.factor
    }
    
    private func generateWeightQuestion() {
        let conversions = [
            (from: "kg", to: "g", factor: 1000),
            (from: "t", to: "kg", factor: 1000),
            (from: "g", to: "mg", factor: 1000)
        ]
        
        let conversion = conversions.randomElement()!
        let value = Int.random(in: 1...5)
        currentQuestion = "\(value) \(conversion.from) = ? \(conversion.to)"
        correctAnswer = value * conversion.factor
    }
    
    private func generateSurfaceQuestion() {
        let conversions = [
            (from: "m²", to: "cm²", factor: 10000),
            (from: "km²", to: "m²", factor: 1000000),
            (from: "cm²", to: "mm²", factor: 100)
        ]
        
        let conversion = conversions.randomElement()!
        let value = Int.random(in: 1...3)
        currentQuestion = "\(value) \(conversion.from) = ? \(conversion.to)"
        correctAnswer = value * conversion.factor
    }
}

struct ResultOverlay: View {
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
                    Text("Poprawna odpowiedź: \(correctAnswer)")
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

struct GameView_Previews: PreviewProvider {
    static var previews: some View {
        GameView(playerName: "Player", operation: "+", difficulty: "Łatwe")
    }
} 