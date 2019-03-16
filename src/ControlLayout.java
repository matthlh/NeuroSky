import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ControlLayout extends JPanel implements ChangeListener {

    String status = "Idle";
    JLabel statusLabel;
    NeuroSocket socket;
    JPanel control;
    JPanel timeout;
    JPanel parent;
    JButton connect;
    JButton start;
    GraphListener graphListener;

    public ControlLayout(JPanel parent, NeuroSocket socket, GraphListener listener) {
        this.socket = socket;
        this.parent = parent;
        this.graphListener = listener;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        drawComponents();
    }

    public void drawComponents() {
        control = new JPanel(new FlowLayout());
        timeout = new JPanel(new FlowLayout());

        statusLabel = new JLabel("Status: " + status);
        connect = new JButton("Connect");
        start = new JButton("Start");
        start.setEnabled(false);
        connect.addActionListener(new ConnectListener(parent, connect, this));
        start.addActionListener(new StartListener(parent, socket, start, this));
        JButton graph = new JButton("Show Graph");
        graph.addActionListener(graphListener);
        JLabel timeoutLabel = new JLabel("Timeout");
        JSlider timeoutSlider = new JSlider(JSlider.HORIZONTAL,
                0,5, 0);
        timeoutSlider.addChangeListener(this);
        timeoutSlider.setMajorTickSpacing(1);
        timeoutSlider.setPaintTicks(true);
        timeoutSlider.setPaintLabels(true);
        timeoutSlider.setSnapToTicks(true);
        timeoutSlider.setValue(2);

        add(statusLabel);

        control.add(connect);
        control.add(start);
        control.add(graph);

        add(control);

        timeout.add(timeoutLabel);
        timeout.add(timeoutSlider);

        add(timeout);
    }

    public String getStatus() {return this.status;}

    public void setStatus(String status) {
        this.status = status;
        statusLabel.setText("Status: " + this.status);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (source.getValueIsAdjusting()) {
            System.out.println("is adjusting to: " + ((JSlider) e.getSource()).getValue());
        }
    }
}
