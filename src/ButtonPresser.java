import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ButtonPresser extends JPanel {

    String status;
    JLabel statusLabel;
    NeuroSocket socket;

    public static JLabel upArrow;
    public static JLabel leftArrow;
    public static JLabel downArrow;
    public static JLabel rightArrow;

    public ButtonPresser(NeuroSocket socket) {
        this.socket = socket;
        setLayout(new GridLayout(3, 3));
        setBorder(new EmptyBorder(0, 20, 20, 20));

        drawComponents();
    }

    private void drawComponents() {

        upArrow = new JLabel();
        downArrow = new JLabel();
        leftArrow = new JLabel();
        rightArrow = new JLabel();

        upArrow.setIcon(new ImageIcon("Pictures/upArrow.png"));
        downArrow.setIcon(new ImageIcon("Pictures/downArrow.png"));
        leftArrow.setIcon(new ImageIcon("Pictures/leftArrow.png"));
        rightArrow.setIcon(new ImageIcon("Pictures/rightArrow.png"));

        upArrow.setHorizontalAlignment(JLabel.CENTER);
        downArrow.setHorizontalAlignment(JLabel.CENTER);
        leftArrow.setHorizontalAlignment(JLabel.CENTER);
        rightArrow.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < 9; i++) {
            JLabel label = new JLabel();

            if (i == 1) {
                add(upArrow);
            } else if (i == 3) {
                add(leftArrow);
            } else if (i == 5) {
                add(rightArrow);
            } else if (i == 7) {
                add(downArrow);
            } else {
                add(label);
            }
        }
    }
}