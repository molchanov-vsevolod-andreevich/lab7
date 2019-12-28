import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, String> storage = new HashMap<>();

        long timeToNofification = System.currentTimeMillis();
        
        ZMQ.Context context = ZMQ.context (1);

        // Socket to talk to server
        ZMQ.Socket notifier = context.socket (SocketType.REP);
        notifier.connect ("tcp://localhost:5560");

        while (!Thread.currentThread ().isInterrupted ()) {
        // Wait for next request from client
            String string = notifier.recvStr (0); System.out.printf ("Received request: [%s]\n", string); // Do some 'work'
            Thread.sleep (1000);
        // Send reply back to client
            notifier.send ("World");
        }
        // We never get here but clean up anyhow notifier.close();
        notifier.close();
        context.term();
    }
}
