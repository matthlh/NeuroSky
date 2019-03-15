import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmitListener implements ActionListener {

    private NeuroSocket socket;
    private JButton button;
    private JPanel parent;

    public SubmitListener(JPanel parent, NeuroSocket socket, JButton button) {
        this.socket = socket;
        this.button = button;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        socket.setStarted(!socket.getStarted());
        if (socket.getStarted()) {
            button.setText("Stop");
        } else {
            button.setText("Start");
        }
        parent.requestFocusInWindow();
    }
}
