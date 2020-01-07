import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(Constants.IO_THREADS);

        ZMQ.Socket requester = context.socket(SocketType.REQ);
        requester.connect(Constants.CLIENT_ADDRESS);
        System.out.println(Constants.START_CLIENT_MESSAGE);

        Scanner in = new Scanner(System.in);

        while (true) {
            Command command = new Command(in.nextLine());

            int commandType = command.getCommandType();

            if (commandType == Command.INVALID_COMMAND_TYPE) {
                System.out.println(Constants.INVALID_COMMAND_MESSAGE);
                continue;
            }

            if (commandType == Command.QUIT_COMMAND_TYPE) {
                break;
            }

            requester.send(command.toString(), Constants.DEFAULT_ZMQ_FLAG);

            String reply = requester.recvStr(Constants.DEFAULT_ZMQ_FLAG);
            System.out.println(reply);

        }

        requester.close();
        context.term();
    }
}
