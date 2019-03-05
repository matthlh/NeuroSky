import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.io.IOException;

public class NeuroSocket implements Runnable {

    BufferedReader in;
    PrintWriter out;
    java.net.Socket echoSocket;
    OutputStream outStream;
    boolean connected = false;

    @Override
    public void run() {
        System.out.println(connected);
        if (connected) {
            try {
                String userInput;
                while ((userInput = in.readLine()) != null) {
                    String[] packets = userInput.split("/\r/");
                    for (int i = 0; i < packets.length; i++) {
//                    JSONObject obj = new JSONObject((String) packets[i]);
                        System.out.println((String) packets[i]);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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
}

