import org.zeromq.ZMQ;

public class Client {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        // Socket to talk to server
        Socket requester = context.socket(SocketType.REQ); requester.connect("tcp://localhost:5559"); System.out.println("launch and connect client.");
        for (int request_nbr = 0; request_nbr < 10; request_nbr++) {
            requester.send("Hello", 0);
            String reply = requester.recvStr (0);
            System.out.println("Received reply " + request_nbr + " [" + reply + "]");
        }
        // We never get here but clean up anyhow requester.close();
        context.term();
    }
}
