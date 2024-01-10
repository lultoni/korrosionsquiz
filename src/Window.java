import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setTitle("Korrosions Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init() {
        setLayout(new BorderLayout());

        JPanel themaPanel = new JPanel();
        themaPanel.setLayout(new GridLayout());
        System.out.println("ini Window start");
        for (Thema thema: DBHelper.getThemen()) {
            System.out.println("On Thema: " + thema.name);
            ThemaButton themaButton = new ThemaButton(thema);
            themaPanel.add(themaButton);
        }
        add(themaPanel);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout());
        JLabel titleLabel = new JLabel(getTitle());
        topPanel.add(titleLabel);
        JButton exitButton = new JButton("Programm beenden");
        exitButton.addActionListener(e -> System.exit(69420));
        topPanel.add(exitButton);
        add(topPanel, BorderLayout.NORTH);

        System.out.println("ini Window end");
        update();
    }

    public void update() {
        revalidate();
        repaint();
    }

}
