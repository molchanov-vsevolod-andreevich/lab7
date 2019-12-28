import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class Storage {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context context = ZMQ.context (1);

        // Socket to talk to server
        ZMQ.Socket storage = context.socket (SocketType.REP);
        storage.connect ("tcp://localhost:5560");

        while (!Thread.currentThread ().isInterrupted ()) {
        // Wait for next request from client
            String string = storage.recvStr (0); System.out.printf ("Received request: [%s]\n", string); // Do some 'work'
            Thread.sleep (1000);
        // Send reply back to client
            storage.send ("World");
        }
        // We never get here but clean up anyhow storage.close();
        storage.close();
        context.term();
    }
}
