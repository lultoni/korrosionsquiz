import javax.swing.*;
import java.awt.*;
import java.util.*;

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
    private final JButton conButton = new JButton("Weiter");

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
        setFont(new Font("Arial", Font.BOLD, 15));
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
        title.setFont(new Font("Arial", Font.BOLD, 20));
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
        upPanel.setLayout(new BorderLayout());
        JLabel qLabel = new JLabel(question.q);
        qLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        conButton.addActionListener(e -> {
            if (wasGuessed) {
                questionIndex++;
                questionWindow.dispose();
                updateText();
                if (questionIndex < questions.size()) questionWindow = createQuestionWindow();
            }
        });
        conButton.setEnabled(false);

        if (question.imagePath == null) {
            upPanel.add(qLabel);
        } else {
            JPanel innerUpPanel = new JPanel();
            ImagePanel imagePanel = new ImagePanel(question.imagePath);
            innerUpPanel.setLayout(new GridLayout());
            innerUpPanel.add(qLabel);
            innerUpPanel.add(imagePanel);
            upPanel.add(innerUpPanel);
        }
        upPanel.add(conButton, BorderLayout.EAST);

        JPanel dPanel = new JPanel();
        ArrayList<String> ans = new ArrayList<>();
        ans.add(question.a1);
        ans.add(question.a2);
        ans.add(question.a3);
        ans.add(question.a4);
        String corString = ans.get(question.ca - 1);
        Collections.shuffle(ans);
        JButton ab1 = new JButton(ans.get(0));
        JButton ab2 = new JButton(ans.get(1));
        JButton ab3 = new JButton(ans.get(2));
        JButton ab4 = new JButton(ans.get(3));

        dPanel.setLayout(new GridLayout());
        dPanel.add(ab1);
        dPanel.add(ab2);
        dPanel.add(ab3);
        dPanel.add(ab4);

        int corAnsNum = 0;
        for (int i = 0; i < 4; i++) {
            if (ans.get(i).equals(corString)) corAnsNum = i + 1;
        }
        int finalCorAnsNum = corAnsNum;

        ab1.addActionListener(e -> guessButtonAction(1, question, ab1, ab2, ab3, ab4, finalCorAnsNum));
        ab2.addActionListener(e -> guessButtonAction(2, question, ab1, ab2, ab3, ab4, finalCorAnsNum));
        ab3.addActionListener(e -> guessButtonAction(3, question, ab1, ab2, ab3, ab4, finalCorAnsNum));
        ab4.addActionListener(e -> guessButtonAction(4, question, ab1, ab2, ab3, ab4, finalCorAnsNum));

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
        setText(thema.name + " (Score: " + userScore + "/" + getBestScore() + ")");
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        float percentage = (float) userScore / getBestScore();
        int r = (int) (235 + percentage * (178 - 235));
        int b = (int) (224 + percentage * (144 - 224));
        Color backgroundColor = new Color(r, 255, b);
        setBackground(backgroundColor);
    }

    private int getBestScore() {
        int back = 0;
        for (Question question: questions) {
            back += question.score;
        }
        return back;
    }

    private void guessButtonAction(int clicked, Question q, JButton ab1, JButton ab2, JButton ab3, JButton ab4, int corAnsNum) {
        boolean corAns = false;
        conButton.setEnabled(true);
        if (!wasGuessed) switch (clicked) {
            case 1 -> {
                if (corAnsNum == clicked) {
                    ab1.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab1.setBackground(guessButtonFalse);
                    switch (corAnsNum) {
                        case 2 -> ab2.setBackground(guessButtonCorrect);
                        case 3 -> ab3.setBackground(guessButtonCorrect);
                        case 4 -> ab4.setBackground(guessButtonCorrect);
                    }
                }
            }
            case 2 -> {
                if (corAnsNum == clicked) {
                    ab2.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab2.setBackground(guessButtonFalse);
                    switch (corAnsNum) {
                        case 1 -> ab1.setBackground(guessButtonCorrect);
                        case 3 -> ab3.setBackground(guessButtonCorrect);
                        case 4 -> ab4.setBackground(guessButtonCorrect);
                    }
                }
            }
            case 3 -> {
                if (corAnsNum == clicked) {
                    ab3.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab3.setBackground(guessButtonFalse);
                    switch (corAnsNum) {
                        case 2 -> ab2.setBackground(guessButtonCorrect);
                        case 1 -> ab1.setBackground(guessButtonCorrect);
                        case 4 -> ab4.setBackground(guessButtonCorrect);
                    }
                }
            }
            case 4 -> {
                if (corAnsNum == clicked) {
                    ab4.setBackground(guessButtonCorrect);
                    corAns = true;
                } else {
                    ab4.setBackground(guessButtonFalse);
                    switch (corAnsNum) {
                        case 2 -> ab2.setBackground(guessButtonCorrect);
                        case 3 -> ab3.setBackground(guessButtonCorrect);
                        case 1 -> ab1.setBackground(guessButtonCorrect);
                    }
                }
            }
        }
        if (corAns) userScore += q.score;
        wasGuessed = true;
    }

}
