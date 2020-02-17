import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectListener implements ActionListener{


    private JButton button;
    private JPanel parent;
    private ControlLayout controlPane;

    public ConnectListener(JPanel parent, JButton button, ControlLayout controlPane) {
        this.button = button;
        this.parent = parent;
        this.controlPane = controlPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (button.getText().equalsIgnoreCase("Disconnect")) {
            UserInterface.doDisconnect();

        } else if (button.getText().equalsIgnoreCase("Connect")) {
            UserInterface.doConnect();
        }
        parent.requestFocusInWindow();
    }
}
