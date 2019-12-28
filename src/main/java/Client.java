import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        // Socket to talk to server
        ZMQ.Socket requester = context.socket(SocketType.REQ);
        requester.connect("tcp://localhost:5559");
        System.out.println("Сlient has been launched and connected");

        Scanner in = new Scanner(System.in);

        while (true) {
            String command = in.nextLine();

            if (command.equals("F")) {
                break;
            }

            requester.send(command, 0);

            String reply = requester.recvStr (0);

            System.out.println(reply);

        }

        // We never get here but clean up anyhow requester.close();
        requester.close();
        context.term();
    }
}
