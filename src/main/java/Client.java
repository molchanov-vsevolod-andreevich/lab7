import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        // Socket to talk to server
        ZMQ.Socket requester = context.socket(SocketType.REQ); requester.connect("tcp://localhost:5559"); System.out.println("launch and connect client.");

        Scanner in = new Scanner(System.in);

        System.out.println("Client has been launched. Enter messages");

        while (true) {
            String command = in.nextLine();

            if (command.isEmpty()) {
                break;
            }


        }

        // We never get here but clean up anyhow requester.close();
        context.term();
    }
}
