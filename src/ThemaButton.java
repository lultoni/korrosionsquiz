import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ThemaButton extends JButton {

    Thema thema;
    private Color guessButtonCol;
    private Color guessButtonCorrect;
    private Color guessButtonFalse;
    private volatile boolean wasGuessed;
    private JFrame questionWindow;
    private final ArrayList<Question> questions;
    private int questionIndex;
    private int userScore;

    public ThemaButton(Thema thema) {
        this.thema = thema;
        this.questionWindow = null;
        this.questions = DBHelper.getQuestions(thema);
        Collections.shuffle(questions);
        this.questionIndex = 0;
        this.userScore = 0;
        init();
    }

    private void init() {
        updateText();
        addActionListener(e -> {
            System.out.println("In Thema: " + thema.name);
            this.questionIndex = 0;
            this.userScore = 0;
            questionWindow = createQuestionWindow();
            questionWindow.setVisible(true);
        });
        setEnabled(!questions.isEmpty());
        guessButtonCol = new Color(171, 171, 171);
        guessButtonCorrect = new Color(170, 255, 95);
        guessButtonFalse = new Color(255, 110, 110);
        wasGuessed = false;
    }

    private JFrame createQuestionWindow() {
        JFrame frame = new JFrame(getText());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLayout(new BorderLayout());

        JPanel nP = new JPanel(new GridLayout());

        JLabel title = new JLabel(getText());
        nP.add(title);

        JButton backButton = new JButton("Zurück zum Startmenü");
        backButton.addActionListener(e -> {
            frame.dispose();
            updateText();
        });
        nP.add(backButton);

        frame.add(nP, BorderLayout.NORTH);
        frame.setVisible(true);
        JPanel questionPanel = createQuestionPanel();
        frame.add(questionPanel);
        return frame;
    }

    private JPanel createQuestionPanel() {
        System.out.println("In createQuestionPanel");
        Question question = questions.get(questionIndex);
        JPanel questionPanel =  new JPanel();
        wasGuessed = false;

        JPanel upPanel = new JPanel();
        JLabel qLabel = new JLabel(question.q);
        JButton conButton = new JButton("Weiter"); // TODO enable when button was pressed (move to class vars)
        conButton.addActionListener(e -> {
            conButton.setBackground((wasGuessed) ? guessButtonCorrect : guessButtonFalse);
            if (wasGuessed) {
                questionIndex++;
                questionWindow.dispose();
                updateText();
                if (questionIndex < questions.size()) questionWindow = createQuestionWindow();
            }
        });

        upPanel.setLayout(new BorderLayout());
        upPanel.add(qLabel);
        upPanel.add(conButton, BorderLayout.EAST);

        JPanel dPanel = new JPanel();
        JButton ab1 = new JButton(question.a1);
        JButton ab2 = new JButton(question.a2);
        JButton ab3 = new JButton(question.a3);
        JButton ab4 = new JButton(question.a4);

        dPanel.setLayout(new GridLayout());
        dPanel.add(ab1);
        dPanel.add(ab2);
        dPanel.add(ab3);
        dPanel.add(ab4);

        ab1.addActionListener(e -> guessButtonAction(1, question, ab1, ab2, ab3, ab4, question.score));
        ab2.addActionListener(e -> guessButtonAction(2, question, ab1, ab2, ab3, ab4, question.score));
        ab3.addActionListener(e -> guessButtonAction(3, question, ab1, ab2, ab3, ab4, question.score));
        ab4.addActionListener(e -> guessButtonAction(4, question, ab1, ab2, ab3, ab4, question.score));

        ab1.setBackground(guessButtonCol);
        ab2.setBackground(guessButtonCol);
        ab3.setBackground(guessButtonCol);
        ab4.setBackground(guessButtonCol);

        questionPanel.setLayout(new GridLayout(0, 1));
        questionPanel.add(upPanel);
        questionPanel.add(dPanel);
        return questionPanel;
    }

    private void updateText() {
        this.setText(thema.name + " (Score: " + userScore + "/" + getBestScore() + ")");
    }

    private int getBestScore() {
        int back = 0;
        for (Question question: questions) {
            back += question.score;
        }
        return back;
    }

    private void guessButtonAction(int clicked, Question q, JButton ab1, JButton ab2, JButton ab3, JButton ab4, int questionScore) {
        boolean corAns = false;
        if (!wasGuessed) switch (clicked) {
            case 1 -> {
                if (q.ca == clicked) {
                    ab1.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab1.setBackground(guessButtonFalse);
                    switch (q.ca) {
                        case 2 -> ab2.setBackground(guessButtonCorrect);
                        case 3 -> ab3.setBackground(guessButtonCorrect);
                        case 4 -> ab4.setBackground(guessButtonCorrect);
                    }
                }
            }
            case 2 -> {
                if (q.ca == clicked) {
                    ab2.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab2.setBackground(guessButtonFalse);
                    switch (q.ca) {
                        case 1 -> ab1.setBackground(guessButtonCorrect);
                        case 3 -> ab3.setBackground(guessButtonCorrect);
                        case 4 -> ab4.setBackground(guessButtonCorrect);
                    }
                }
            }
            case 3 -> {
                if (q.ca == clicked) {
                    ab3.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab3.setBackground(guessButtonFalse);
                    switch (q.ca) {
                        case 2 -> ab2.setBackground(guessButtonCorrect);
                        case 1 -> ab1.setBackground(guessButtonCorrect);
                        case 4 -> ab4.setBackground(guessButtonCorrect);
                    }
                }
            }
            case 4 -> {
                if (q.ca == clicked) {
                    ab4.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab4.setBackground(guessButtonFalse);
                    switch (q.ca) {
                        case 2 -> ab2.setBackground(guessButtonCorrect);
                        case 3 -> ab3.setBackground(guessButtonCorrect);
                        case 1 -> ab1.setBackground(guessButtonCorrect);
                    }
                }
            }
        }
        if (corAns) userScore += questionScore;
        wasGuessed = true;
    }

}
