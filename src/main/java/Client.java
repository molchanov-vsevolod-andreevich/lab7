import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket requester = context.socket(SocketType.REQ);
        requester.connect("tcp://localhost:5559");
        System.out.println("Сlient has been launched and connected");

        Scanner in = new Scanner(System.in);

        while (true) {
            Command command = new Command(in.nextLine());

            if (command.getCommandType() == Constants.INVALID_COMMAND_TYPE) {
                System.out.println();
                continue;
            }

            if (command.getCommandType() == Constants.QUIT_COMMAND_TYPE) {
                break;
            }

            requester.send(command.toString(), 0);

            String reply = requester.recvStr (0);

            System.out.println(reply);

        }

        requester.close();
        context.term();
    }
}
