import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    private static long timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
    private static int startIdx;
    private static int endIdx;
    private static final Map<Integer, String> storage = new HashMap<>();

    static void setInterval(String[] args) {
        startIdx = Integer.parseInt(args[0]);
        endIdx = Integer.parseInt(args[1]);
    }

    static void setValues(String[] args) {
        int argsLen = args.length;
        for (int i = 2, j = 0; i < argsLen; i++, j++) {
            storage.put(startIdx + j, args[i]);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.err.println(Constants.NOT_ENOUGH_ARGS_ERROR_MESSAGE);
            return;
        }

        setInterval(args);

        if (args.length != 2 + endIdx - startIdx + 1) {
            System.err.println(Constants.NOT_ENOUGH_ARGS_ERROR_MESSAGE);
            return;
        }

        setValues(args);
        
        ZMQ.Context context = ZMQ.context (1);

        // Socket to talk to server
        ZMQ.Socket notifier = context.socket (SocketType.DEALER);
        notifier.connect ("tcp://localhost:5560");

        System.out.println("Storage has been launched and connected");

        while (!Thread.currentThread().isInterrupted()) {

            if (System.currentTimeMillis() == timeToNofification) {
                Command command = new Command(Constants.NOTIFY_COMMAND_TYPE + " " + startIdx + " " + endIdx);
                notifier.send(command.toString(), 0);
                timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
            }

            ZMsg msg = ZMsg.recvMsg(notifier, false);

            if (msg != null) {
                String[] commandTypeAndArgs = new String(msg.getLast().getData()).split(Constants.DELIMITER, Constants.LIMIT);
                Command command = new Command(commandTypeAndArgs);

                System.out.println(command.toString());

                if (command.getCommandType() == Constants.GET_COMMAND_TYPE) {
                    int key = Integer.parseInt(command.getArgs());
                    String value = storage.get(key);

                    msg.getLast().reset(value);
                    msg.send(notifier);
                }

                if (command.getCommandType() == Constants.GET_COMMAND_TYPE) {
                    String[] commandArgs = command.getArgs().split(Constants.DELIMITER, Constants.LIMIT);
                    int key = Integer.parseInt(commandArgs[0]);
                    String newValue = commandArgs[1];

                    storage.put(key, newValue);
                }
            }
        }

        // We never get here but clean up anyhow notifier.close();
        notifier.close();
        context.term();
    }
}
