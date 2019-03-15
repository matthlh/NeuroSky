import org.json.*;

import java.io.*;
import java.net.SocketException;
import java.time.LocalDateTime;

public class NeuroSocket implements Runnable {

    BufferedReader in;
    PrintWriter out;
    java.net.Socket echoSocket;
    OutputStream outStream;
    boolean connected = false;
    boolean working;
    boolean buttonHeld;
    boolean started;
    OnEEGDataListener eegListener;

    @Override
    public void run() {
        System.out.println(connected);
        if (connected) {
            try {
                String userInput;
                FileWriter fout = new FileWriter("fromJSON.csv", true);
                fout.write("ButtonHeld,Current Time,delta,theta,lowAlpha,highAlpha,lowBeta,highBeta,lowGamma,highGamma\n");
                while ((userInput = in.readLine()) != null) {
                    // invoke callback
                    eegListener.onEEGData();

                    String[] packets = userInput.split("/\r/");
                    for (int i = 0; i < packets.length; i++) {
                        System.out.println((String) packets[i]);
                    }
                    JSONObject eegPower;
                    try {
                        if(userInput.contains("eegPower")) {
                            eegPower = new JSONObject(userInput);
                            eegPower = eegPower.getJSONObject("eegPower");

                            int delta = eegPower.getInt("delta");
                            int theta = eegPower.getInt("theta");
                            int lowAlpha = eegPower.getInt("lowAlpha");
                            int highAlpha = eegPower.getInt("highAlpha");
                            int lowBeta = eegPower.getInt("lowBeta");
                            int highBeta = eegPower.getInt("highBeta");
                            int lowGamma = eegPower.getInt("lowGamma");
                            int highGamma = eegPower.getInt("highGamma");

                            System.out.println(this.buttonHeld);

                            JSONObject attnMeditLevel = new JSONObject(userInput);
                            attnMeditLevel = attnMeditLevel.getJSONObject("eSense");
                            if (attnMeditLevel.getInt("attention") != 0 && attnMeditLevel.getInt("meditation") != 0) {
                                working = true;
                                if (started) {
                                    fout.write(getButtonHeld() + "," + LocalDateTime.now() + "," + delta + "," + theta + "," + lowAlpha + "," + highAlpha + "," + lowBeta + "," + highBeta +
                                            "," + lowGamma + "," + highGamma + "\n");
                                    fout.flush();
                                }
                            } else {
                                working = false;
                            }
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setButtonHeld(boolean held) {
        this.buttonHeld = held;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean getButtonHeld() {
        return this.buttonHeld;
    }

    public boolean getStarted() {
        return this.started;
    }

    public void sendMessage(String s) {
        System.out.println("Sending message: " + s);
        out.println(s);
    }

    public void init(String hostName, int portNumber) {
        try {
            echoSocket = new java.net.Socket(hostName, portNumber);
            outStream = echoSocket.getOutputStream();
            out =
                    new PrintWriter(outStream, true);
            in =
                    new BufferedReader(
                            new InputStreamReader(echoSocket.getInputStream()));
            connected = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerOnEEGListener(OnEEGDataListener eegListener) {
        this.eegListener = eegListener;
    }
}

