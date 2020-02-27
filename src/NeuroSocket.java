import org.json.*;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NeuroSocket implements Runnable {

    BufferedReader in;
    PrintWriter out;
    java.net.Socket echoSocket;
    OutputStream outStream;
    boolean connected = false;
    boolean working;

    int delta;
    int theta;
    int lowAlpha;
    int highAlpha;
    int lowBeta;
    int highBeta;
    int lowGamma;
    int highGamma;

    ArrayList<Double> deltaArray;
    ArrayList<Double> thetaArray;
    ArrayList<Double> lowAlphaArray;
    ArrayList<Double> highAlphaArray;
    ArrayList<Double> lowBetaArray;
    ArrayList<Double> highBetaArray;
    ArrayList<Double> lowGammaArray;
    ArrayList<Double> highGammaArray;

    // 1 = correct
    // 0 = wrong
    // -1 = not pressed
    int result;
    boolean started;
    OnEEGDataListener eegListener;

    @Override
    public void run() {
        System.out.println(connected);
        if (connected) {
            try {
                String userInput;

                while ((userInput = in.readLine()) != null) {
                    //Invoke callback
                    eegListener.onEEGData();

                    JSONObject eegPower;
                    try {
                        if(userInput.contains("eegPower")) {
                            eegPower = new JSONObject(userInput);
                            eegPower = eegPower.getJSONObject("eegPower");


                            //Recieves brain waves from headset
                            JSONObject attnMeditLevel = new JSONObject(userInput);
                            attnMeditLevel = attnMeditLevel.getJSONObject("eSense");
                            if (attnMeditLevel.getInt("attention") != 0 &&
                                    attnMeditLevel.getInt("meditation") != 0 &&
                                    isNotSame(eegPower)) {
                                working = true;
                                if (started) {
                                    delta = eegPower.getInt("delta");
                                    theta = eegPower.getInt("theta");
                                    lowAlpha = eegPower.getInt("lowAlpha");
                                    highAlpha = eegPower.getInt("highAlpha");
                                    lowBeta = eegPower.getInt("lowBeta");
                                    highBeta = eegPower.getInt("highBeta");
                                    lowGamma = eegPower.getInt("lowGamma");
                                    highGamma = eegPower.getInt("highGamma");

                                    deltaArray.add((double) delta);
                                    thetaArray.add((double) theta);
                                    lowAlphaArray.add((double) lowAlpha);
                                    highAlphaArray.add((double) highAlpha);
                                    lowBetaArray.add((double) lowBeta);
                                    highBetaArray.add((double) highBeta);
                                    lowGammaArray.add((double) lowGamma);
                                    highGammaArray.add((double) highGamma);
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
                // Socket closed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNotSame(JSONObject newData) {
        try {
            if (delta != newData.getInt("delta") &&
                    theta != newData.getInt("theta") &&
                    lowAlpha != newData.getInt("lowAlpha") &&
                    highAlpha != newData.getInt("highAlpha") &&
                    lowBeta != newData.getInt("lowBeta") &&
                    highBeta != newData.getInt("highBeta") &&
                    lowGamma != newData.getInt("lowGamma") &&
                    highGamma != newData.getInt("highGamma")
            ) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean getStarted() {
        return this.started;
    }

    public void sendMessage(String s) {
        System.out.println("Sending message: " + s);
        if (out != null) {
            out.println(s);
        } else {
            System.out.println("Could not send message: not connected");
        }
    }

    public String getWriteQueue() {
        return getResult() + "," + LocalDateTime.now() + "," + delta + ","
                + theta + "," + lowAlpha + "," + highAlpha + ","
                + lowBeta + "," + highBeta +
                "," + lowGamma + "," + highGamma + "\n";
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

            deltaArray = new ArrayList<>();
            thetaArray = new ArrayList<>();
            lowAlphaArray = new ArrayList<>();
            highAlphaArray = new ArrayList<>();
            lowBetaArray = new ArrayList<>();
            highBetaArray = new ArrayList<>();
            lowGammaArray = new ArrayList<>();
            highGammaArray = new ArrayList<>();

        } catch (ConnectException e) {
            // failed to connect
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            echoSocket.close();
            outStream.close();
            out.close();
            in.close();

            connected = false;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void registerOnEEGListener(OnEEGDataListener eegListener) {
        this.eegListener = eegListener;
    }
}

