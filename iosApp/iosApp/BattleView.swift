import SwiftUI

struct BattleView: View {
    @Environment(\.presentationMode) var presentationMode
    
    let player1Name: String
    let player2Name: String
    
    @State private var player1Score = 0
    @State private var player2Score = 0
    @State private var currentQuestion = "5 + 3 = ?"
    @State private var correctAnswer = 8
    @State private var answers: [Int] = []
    @State private var questionNumber = 1
    @State private var totalQuestions = 20 // Android używa 20 punktów do osiągnięcia
    @State private var showGameEnd = false
    @State private var winner = ""
    @State private var player1SelectedAnswer: Int? = nil
    @State private var player2SelectedAnswer: Int? = nil
    @State private var player1ShowFeedback = false
    @State private var player2ShowFeedback = false
    @State private var player1WrongAnswers: Set<Int> = []
    @State private var player2WrongAnswers: Set<Int> = []
    
    var body: some View {
        ZStack {
            // Tło gradientowe identyczne jak w głównym UI
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
                // Header z przyciskiem powrotu w lewym górnym rogu
                HStack {
                    Button(action: {
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Image(systemName: "chevron.left")
                            .font(.title2)
                            .foregroundColor(.white)
                            .padding()
                    }
                    Spacer()
                }
                .padding(.horizontal)
                .padding(.top)
                
                // Gracz 1 (góra, obrócony)
                PlayerBattlePanel(
                    playerName: player1Name,
                    score: player1Score,
                    question: currentQuestion,
                    answers: answers,
                    isRotated: true,
                    selectedAnswer: player1SelectedAnswer,
                    correctAnswer: correctAnswer,
                    showFeedback: player1ShowFeedback,
                    wrongAnswers: player1WrongAnswers,
                    onAnswerSelected: { answer in
                        handleAnswerSelected(answer: answer, player: 1)
                    }
                )
                .rotation3DEffect(.degrees(180), axis: (x: 0, y: 0, z: 1))
                
                // Gracz 2 (dół, normalny)
                PlayerBattlePanel(
                    playerName: player2Name,
                    score: player2Score,
                    question: currentQuestion,
                    answers: answers,
                    isRotated: false,
                    selectedAnswer: player2SelectedAnswer,
                    correctAnswer: correctAnswer,
                    showFeedback: player2ShowFeedback,
                    wrongAnswers: player2WrongAnswers,
                    onAnswerSelected: { answer in
                        handleAnswerSelected(answer: answer, player: 2)
                    }
                )
            }
            
            // Dialog końca gry
            if showGameEnd {
                GameEndDialog(
                    player1Name: player1Name,
                    player1Score: player1Score,
                    player2Name: player2Name,
                    player2Score: player2Score,
                    onDismiss: {
                        presentationMode.wrappedValue.dismiss()
                    }
                )
            }
        }
        .navigationBarHidden(true)
        .onAppear {
            generateNewQuestion()
        }
    }
    
    private func handleAnswerSelected(answer: Int, player: Int) {
        // Zatrzymaj dalsze kliknięcia podczas pokazywania feedbacku dla tego gracza
        if player == 1 {
            guard !player1ShowFeedback else { return }
            player1SelectedAnswer = answer
            player1ShowFeedback = true
        } else {
            guard !player2ShowFeedback else { return }
            player2SelectedAnswer = answer
            player2ShowFeedback = true
        }
        
        if answer == correctAnswer {
            // Prawidłowa odpowiedź - dodaj punkt i przejdź do następnego pytania
            if player == 1 {
                player1Score += 1
            } else {
                player2Score += 1
            }
            
            // Sprawdź czy koniec gry (20 punktów)
            if player1Score >= 20 || player2Score >= 20 {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                    resetFeedbackState()
                    showGameEnd = true
                }
                return
            }
            
            // Przejdź do następnego pytania po 1 sekundzie
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                resetFeedbackState()
                generateNewQuestion()
            }
        } else {
            // Błędna odpowiedź - dodaj do listy błędnych dla tego gracza
            if player == 1 {
                player1WrongAnswers.insert(answer)
            } else {
                player2WrongAnswers.insert(answer)
            }
            
            // Ukryj feedback po 1 sekundzie, ale zostań na tym samym pytaniu
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                if player == 1 {
                    player1ShowFeedback = false
                    player1SelectedAnswer = nil
                } else {
                    player2ShowFeedback = false
                    player2SelectedAnswer = nil
                }
            }
        }
    }
    
    private func resetFeedbackState() {
        player1ShowFeedback = false
        player2ShowFeedback = false
        player1SelectedAnswer = nil
        player2SelectedAnswer = nil
        player1WrongAnswers.removeAll()
        player2WrongAnswers.removeAll()
    }
    
    private func generateNewQuestion() {
        // Resetuj stan błędnych odpowiedzi dla nowego pytania
        resetFeedbackState()
        
        let a = Int.random(in: 1...20)
        let b = Int.random(in: 1...20)
        let operation = Int.random(in: 0...3)
        
        switch operation {
        case 0:
            currentQuestion = "\(a) + \(b) = ?"
            correctAnswer = a + b
        case 1:
            currentQuestion = "\(a) - \(b) = ?"
            correctAnswer = a - b
        case 2:
            currentQuestion = "\(a) × \(b) = ?"
            correctAnswer = a * b
        default:
            let dividend = a * b
            currentQuestion = "\(dividend) ÷ \(a) = ?"
            correctAnswer = b
        }
        
        // Generuj 4 odpowiedzi (1 poprawna + 3 błędne)
        answers = generateAnswers(correct: correctAnswer)
    }
    
    private func generateAnswers(correct: Int) -> [Int] {
        var answers = [correct]
        
        while answers.count < 4 {
            let wrong = correct + Int.random(in: -10...10)
            if wrong != correct && wrong > 0 && !answers.contains(wrong) {
                answers.append(wrong)
            }
        }
        
        return answers.shuffled()
    }
}

struct PlayerBattlePanel: View {
    let playerName: String
    let score: Int
    let question: String
    let answers: [Int]
    let isRotated: Bool
    let selectedAnswer: Int?
    let correctAnswer: Int
    let showFeedback: Bool
    let wrongAnswers: Set<Int>
    let onAnswerSelected: (Int) -> Void
    
    var body: some View {
        VStack(spacing: 0) {
            // Pasek postępu na górze
            HStack {
                Text("\(score)")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                    .padding(.leading, 8)
                
                Spacer()
                
                ProgressView(value: Double(score), total: 20.0)
                    .progressViewStyle(LinearProgressViewStyle(tint: Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0)))
                    .frame(height: 40)
            }
            .padding(.horizontal)
            
            // Pytanie w środku
            Text(question)
                .font(.system(size: 36, weight: .bold))
                .foregroundColor(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                .multilineTextAlignment(.center)
                .padding(.vertical, 32)
            
            // Przyciski odpowiedzi w układzie 2x2
            if answers.count >= 4 {
                HStack(spacing: 0) {
                    VStack(spacing: 20) {
                        AnswerButton(
                            answer: answers[0], 
                            selectedAnswer: selectedAnswer,
                            correctAnswer: correctAnswer,
                            showFeedback: showFeedback,
                            wrongAnswers: wrongAnswers,
                            onTap: onAnswerSelected
                        )
                        AnswerButton(
                            answer: answers[2], 
                            selectedAnswer: selectedAnswer,
                            correctAnswer: correctAnswer,
                            showFeedback: showFeedback,
                            wrongAnswers: wrongAnswers,
                            onTap: onAnswerSelected
                        )
                    }
                    .frame(maxWidth: .infinity)
                    
                    VStack(spacing: 20) {
                        AnswerButton(
                            answer: answers[1], 
                            selectedAnswer: selectedAnswer,
                            correctAnswer: correctAnswer,
                            showFeedback: showFeedback,
                            wrongAnswers: wrongAnswers,
                            onTap: onAnswerSelected
                        )
                        AnswerButton(
                            answer: answers[3], 
                            selectedAnswer: selectedAnswer,
                            correctAnswer: correctAnswer,
                            showFeedback: showFeedback,
                            wrongAnswers: wrongAnswers,
                            onTap: onAnswerSelected
                        )
                    }
                    .frame(maxWidth: .infinity)
                }
                .padding(.horizontal)
                .padding(.bottom, 32)
            }
        }
        .frame(maxHeight: .infinity)
    }
}

struct AnswerButton: View {
    let answer: Int
    let selectedAnswer: Int?
    let correctAnswer: Int
    let showFeedback: Bool
    let wrongAnswers: Set<Int>
    let onTap: (Int) -> Void
    
    private var buttonColor: Color {
        if showFeedback && selectedAnswer == answer {
            // Jeśli to aktualnie zaznaczona odpowiedź podczas feedbacku
            return answer == correctAnswer ? .green : .red
        } else if wrongAnswers.contains(answer) {
            // Jeśli to odpowiedź która była już oznaczona jako błędna
            return .red.opacity(0.6)
        } else if showFeedback && answer == correctAnswer && selectedAnswer == correctAnswer {
            // Pokaż zielony tylko gdy ktoś faktycznie wybrał prawidłową odpowiedź
            return .green
        } else {
            // Domyślny kolor - pomarańczowy jak w głównym UI
            return Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0)
        }
    }
    
    private var isDisabled: Bool {
        return showFeedback || wrongAnswers.contains(answer)
    }
    
    var body: some View {
        Button(action: {
            onTap(answer)
        }) {
            Text("\(answer)")
                .font(.title2)
                .fontWeight(.semibold)
                .foregroundColor(.white)
                .frame(width: 120, height: 60)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(buttonColor)
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color.white.opacity(0.3), lineWidth: 1)
                        )
                )
        }
        .buttonStyle(PlainButtonStyle())
        .disabled(isDisabled) // Zablokuj przyciski podczas feedbacku lub gdy są już błędne
    }
}

struct GameEndDialog: View {
    let player1Name: String
    let player1Score: Int
    let player2Name: String
    let player2Score: Int
    let onDismiss: () -> Void
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.8)
                .ignoresSafeArea()
            
            VStack(spacing: 20) {
                Text("Gra zakończona")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                
                Text("\(player1Name) wynik: \(player1Score)\n\(player2Name) wynik: \(player2Score)")
                    .font(.body)
                    .foregroundColor(.white)
                    .multilineTextAlignment(.center)
                
                Button("OK") {
                    onDismiss()
                }
                .font(.body)
                .foregroundColor(.white)
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(Color(red: 0xF6/255.0, green: 0x9A/255.0, blue: 0x3D/255.0))
                )
            }
            .padding(40)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.black.opacity(0.9))
            )
        }
    }
}

struct BattleView_Previews: PreviewProvider {
    static var previews: some View {
        BattleView(player1Name: "Gracz1", player2Name: "Gracz2")
    }
} 