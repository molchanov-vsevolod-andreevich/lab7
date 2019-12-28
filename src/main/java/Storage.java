import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.HashMap;

public class Storage {
    public static void main(String[] args) throws InterruptedException {
        Map<int, String> storage = new HashMap<>();
        
        ZMQ.Context context = ZMQ.context (1);

        // Socket to talk to server
        ZMQ.Socket notif = context.socket (SocketType.REP);
        notif.connect ("tcp://localhost:5560");

        while (!Thread.currentThread ().isInterrupted ()) {
        // Wait for next request from client
            String string = notif.recvStr (0); System.out.printf ("Received request: [%s]\n", string); // Do some 'work'
            Thread.sleep (1000);
        // Send reply back to client
            notif.send ("World");
        }
        // We never get here but clean up anyhow notif.close();
        notif.close();
        context.term();
    }
}
