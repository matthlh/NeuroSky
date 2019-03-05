
public class Main {
    public static void main(String[] args) {
        NeuroSocket socket = new NeuroSocket();
        Thread t = new Thread(socket);
        socket.init("127.0.0.1", 13854 );
        t.start();
//        socket.sendMessage("{\"appName\": \"client\", \"appKey\": \"4bce3c25bd2f14af6b4923a9d6a125ce3fd59422\"}");
    }
}
