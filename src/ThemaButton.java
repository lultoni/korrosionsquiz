import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ThemaButton extends JButton {

    Thema thema;
    private Color guessButtonCol;
    private Color guessButtonCorrect;
    private Color guessButtonFalse;
    private volatile boolean wasGuessed;
    private JFrame questionWindow;
    private final ArrayList<Question> questions;
    private int questionIndex;

    public ThemaButton(Thema thema) {
        this.thema = thema;
        this.questionWindow = null;
        this.questions = DBHelper.getQuestions(thema);
        this.questionIndex = 0;
        init();
    }

    private void init() {
        setText(thema.name);
        addActionListener(e -> {
            System.out.println("In Thema: " + thema.name);
            questionWindow = createQuestionWindow();
            questionWindow.setVisible(true);
        });
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
            // TODO add logic for counting correct questions etc
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
        JButton conButton = new JButton("Weiter");
        conButton.addActionListener(e -> {
            conButton.setBackground((wasGuessed) ? guessButtonCorrect : guessButtonFalse);
            questionIndex++;
            System.out.println("qs" + questions.size());
            System.out.println("qin" + questionIndex);
            if (questionIndex >= questions.size()) {
                questionWindow.dispose();
            } else {
                questionWindow = createQuestionWindow();
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

        ab1.addActionListener(e -> guessButtonAction(1, question, ab1, ab2, ab3, ab4));
        ab2.addActionListener(e -> guessButtonAction(2, question, ab1, ab2, ab3, ab4));
        ab3.addActionListener(e -> guessButtonAction(3, question, ab1, ab2, ab3, ab4));
        ab4.addActionListener(e -> guessButtonAction(4, question, ab1, ab2, ab3, ab4));

        ab1.setBackground(guessButtonCol);
        ab2.setBackground(guessButtonCol);
        ab3.setBackground(guessButtonCol);
        ab4.setBackground(guessButtonCol);

        questionPanel.setLayout(new GridLayout(0, 1));
        questionPanel.add(upPanel);
        questionPanel.add(dPanel);
        return questionPanel;
    }

    private void guessButtonAction(int clicked, Question q, JButton ab1, JButton ab2, JButton ab3, JButton ab4) {
        if (!wasGuessed) switch (clicked) {
            case 1 -> {
                if (q.ca == clicked) {
                    ab1.setBackground(guessButtonCorrect);
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
        wasGuessed = true;
    }

}
