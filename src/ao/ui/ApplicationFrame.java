package ao.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class ApplicationFrame extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public ApplicationFrame() {
        init();
    }

    private void init() {
        setTitle("Алгоритм Зонга-Суня vs гибридный алгоритм");
        setBounds(SCREEN_SIZE.width / 4, SCREEN_SIZE.height / 4, SCREEN_SIZE.width / 2, SCREEN_SIZE.height / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new ApplicationPanel());
        setVisible(true);
    }
}
