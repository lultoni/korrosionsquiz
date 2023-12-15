import javax.swing.*;
import java.awt.*;

public class ThemaButton extends JButton {

    Thema thema;

    public ThemaButton(Thema thema) {
        this.thema = thema;
        init();
    }

    private void init() {
        setText(thema.name);
        addActionListener(e -> {
            createQuestionWindow();
        });
    }

    private void createQuestionWindow() {
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
        frame.add(createQuestionPanel());

        frame.setVisible(true);
    }

    private JPanel createQuestionPanel() {
        JPanel questionPanel =  new JPanel();
        for(Question question: DBHelper.getQuestions(thema)) {
            System.out.println("Q: " + question.q);

            JPanel upPanel = new JPanel();
            JLabel qLabel = new JLabel(question.q);
            JButton conButton = new JButton("Weiter");
            conButton.addActionListener(e -> {
                // TODO implement logic
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

            // TODO guess logic

            questionPanel.setLayout(new GridLayout(0, 1));
            questionPanel.add(upPanel);
            questionPanel.add(dPanel);
        }
        return questionPanel;
    }

}
