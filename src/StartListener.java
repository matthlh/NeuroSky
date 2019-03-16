import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartListener implements ActionListener{

    private NeuroSocket socket;
    private JButton button;
    private JPanel parent;
    private ControlLayout controlPane;

    public StartListener(JPanel parent, NeuroSocket socket, JButton button, ControlLayout controlPane) {
        this.socket = socket;
        this.button = button;
        this.parent = parent;
        this.controlPane = controlPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        socket.setStarted(!socket.getStarted());
        if (socket.getStarted()) {
            button.setText("Stop");
            button.setEnabled(true);
        } else {
            button.setText("Start");
            if(controlPane.getStatus().equalsIgnoreCase("Idle") ||
                    controlPane.getStatus().equalsIgnoreCase("Connecting") ||
                    controlPane.getStatus().equalsIgnoreCase("Waiting for EEG Data...")) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
                controlPane.connect.setEnabled(true);
                controlPane.status = "Waiting for EEG Data...";
            }
        }

        parent.requestFocusInWindow();
    }
}
