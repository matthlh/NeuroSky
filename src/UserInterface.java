import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;

public class UserInterface implements Runnable, OnEEGDataListener, KeyListener {
    private JFrame frame;
    static NeuroSocket socket = new NeuroSocket();

    static GraphListener listener = new GraphListener(socket);
    ButtonPresser buttonPresserPane = new ButtonPresser(socket);
    static Thread t;
    FileWriter fout;

    public static int randNum;

    static JPanel myContainer = new JPanel();
    static ControlLayout controlPane = new ControlLayout(myContainer, socket, listener);

    public UserInterface() {
        myContainer.setLayout(new BoxLayout(myContainer, BoxLayout.Y_AXIS));
        myContainer.add(controlPane);
        myContainer.add(buttonPresserPane);

        Timer t = new Timer();
        Random rand = new Random();

        t.scheduleAtFixedRate(
                new TimerTask()
                {
                    public void run() {
                        if (socket.started) {
                            if(controlPane.getStatus().equalsIgnoreCase("Waiting for EEG Data...")) {
                                System.out.println("Headset has disconnected");
                            }
                            randNum = rand.nextInt(4);

                            if (socket.getResult() == -1) {
                                logData();
                            }

                            socket.setResult(-1);

                            ButtonPresser.upArrow.setIcon(new ImageIcon("Pictures/upArrow.png"));
                            ButtonPresser.leftArrow.setIcon(new ImageIcon("Pictures/leftArrow.png"));
                            ButtonPresser.rightArrow.setIcon(new ImageIcon("Pictures/rightArrow.png"));
                            ButtonPresser.downArrow.setIcon(new ImageIcon("Pictures/downArrow.png"));


                            t.schedule(
                                    new TimerTask() {
                                        public void run() {
                                            if (randNum == 0) {
                                                ButtonPresser.upArrow.setIcon(new ImageIcon("Pictures/upArrowSelected.png"));
                                            } else if (randNum == 1) {
                                                ButtonPresser.leftArrow.setIcon(new ImageIcon("Pictures/leftArrowSelected.png"));
                                            } else if (randNum == 2) {
                                                ButtonPresser.rightArrow.setIcon(new ImageIcon("Pictures/rightArrowSelected.png"));
                                            } else if (randNum == 3) {
                                                ButtonPresser.downArrow.setIcon(new ImageIcon("Pictures/downArrowSelected.png"));
                                            }
                                        }
                                    },
                                    100);
                        }
                    }
                },
                controlPane.timeoutSec * 1000,
                2000);


        myContainer.addKeyListener(this);
        myContainer.setFocusable(true);
        myContainer.requestFocus();
    }

    // implementing the run function of the Runnable interface

    @Override
    public void run() {
        // create new JFrame with title
        frame = new JFrame("Arrow key clicker");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set dimensions
        frame.setPreferredSize(new Dimension(350, 500));
        // add components into our content page
        createComponents(frame.getContentPane());
        // layout all the components
        frame.pack();
        // make our frame visible to the user
        frame.setVisible(true);

        socket.registerOnEEGListener(this);

        try {
            fout = new FileWriter(LocalDateTime.now().toString().split("\\.")[0] + ".csv", true);
            fout.write("ButtonHeld,Current Time,delta,theta,lowAlpha,highAlpha,lowBeta,highBeta,lowGamma,highGamma\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doConnect() {
        //Opens new socket through client and server
        t = new Thread(socket);
        socket.init("127.0.0.1", 13854 );
        t.start();
        socket.sendMessage("{\"enableRawOutput\": \"false\", \"format\": \"Json \"}");
    }

    public static void doDisconnect() {
        //disconnects socket
        socket.disconnect();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        controlPane.connect.setText("Connect");
        controlPane.start.setEnabled(false);
        controlPane.setStatus("Not Ready");
        socket.setStarted(false);
    }

    @Override
    public void onEEGData() {
        //updates graph whenever ran
        if(socket.connected) {
            if(socket.working) {
                if (socket.started) {
                  controlPane.setStatus("Data collection in progress...");
                    controlPane.connect.setText("Disconnect");
                    controlPane.connect.setEnabled(false);
                    controlPane.start.setEnabled(true);
                    controlPane.graph.setEnabled(true);
                    if (listener != null && listener.chart != null) {
                        listener.chart.updateXYSeries("Delta", null, socket.deltaArray, null);
                        listener.chart.updateXYSeries("Theta", null, socket.thetaArray, null);
                        listener.chart.updateXYSeries("Low Alpha", null, socket.lowAlphaArray, null);
                        listener.chart.updateXYSeries("High Alpha", null, socket.highAlphaArray, null);
                        listener.chart.updateXYSeries("Low Beta", null, socket.lowBetaArray, null);
                        listener.chart.updateXYSeries("High Beta", null, socket.highBetaArray, null);
                        listener.chart.updateXYSeries("Low Gamma", null, socket.lowGammaArray, null);
                        listener.chart.updateXYSeries("High Gamma", null, socket.highGammaArray, null);
                        listener.chartPanel.revalidate();
                        listener.chartPanel.repaint();
                    }
                } else {
                    controlPane.setStatus("Ready");
                    controlPane.start.setEnabled(true);
                    controlPane.connect.setEnabled(true);
                    controlPane.connect.setText("Disconnect");
                }
            } else {
                controlPane.setStatus("Waiting for EEG Data...");
                controlPane.start.setEnabled(false);
                if(controlPane.start.getText().equalsIgnoreCase("stop")) {
                    controlPane.start.setEnabled(true);
                }
                controlPane.connect.setText("Disconnect");
                controlPane.connect.setEnabled(true);
            }
        } else {
            controlPane.setStatus("Failed to connect");
            controlPane.connect.setText("Connect");
            controlPane.connect.setEnabled(true);
        }

        controlPane.validate();
        controlPane.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        //Checks if correct button was clicked
        System.out.println(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if(UserInterface.randNum == 0) {
                socket.setResult(1);
            } else {
                socket.setResult(0);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(UserInterface.randNum == 1) {
                socket.setResult(1);
            } else {
                socket.setResult(0);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(UserInterface.randNum == 2) {
                socket.setResult(1);
            } else {
                socket.setResult(0);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if(UserInterface.randNum == 3) {
                socket.setResult(1);
            } else {
                socket.setResult(0);
            }
        } else {
            socket.setResult(0);
        }
        logData();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    private void createComponents(Container container) {
        //Adds components in the JPanel(myContainer)
        container.add(myContainer);
    }

    public void logData() {
        //logs the data
        try {
            if (fout != null && socket != null && socket.getWriteQueue() != null) {
                fout.write(socket.getWriteQueue());
                System.out.println(socket.getWriteQueue());
                fout.flush();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
}