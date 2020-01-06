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

    private static void setInterval(String[] args) {
        startIdx = Integer.parseInt(args[0]);
        endIdx = Integer.parseInt(args[1]);
    }

    private static void setValues(String[] args) {
        int argsLen = args.length;
        for (int i = 2, j = 0; i < argsLen; i++, j++) {
            storage.put(startIdx + j, args[i]);
        }
    }

    public static void main(String[] args) {
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
        
        ZMQ.Context context = ZMQ.context (Constants.IO_THREADS);

        ZMQ.Socket notifier = context.socket(SocketType.DEALER);
        notifier.connect (Constants.STORAGE_ADDRESS);

        System.out.println(Constants.START_STORAGE_MESSAGE);

        while (!Thread.currentThread().isInterrupted()) {

            if (System.currentTimeMillis() == timeToNofification) {
                Command command = new Command(Constants.NOTIFY_COMMAND_TYPE, startIdx + " " + endIdx);
                notifier.send(command.toString(), Constants.DEFAULT_ZMQ_FLAG);
                timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
            }

            ZMsg msg = ZMsg.recvMsg(notifier, false);

            if (msg != null) {
                String[] commandTypeAndArgs = msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT);
                Command command = new Command(commandTypeAndArgs);

                System.out.println(command.toString());

                int commandType = command.getCommandType();
                if (commandType == Constants.GET_COMMAND_TYPE) {
                    int key = Integer.parseInt(command.getArgs());
                    String value = storage.get(key);

                    Command resp = new Command(Constants.RESPONSE_COMMAND_TYPE, value);

                    System.out.println(msg.getFirst());
                    msg.getLast().reset(resp.toString());
                    msg.send(notifier);
                }

                if (commandType == Constants.PUT_COMMAND_TYPE) {
                    String[] commandArgs = command.getArgs().split(Constants.DELIMITER, Constants.LIMIT);
                    int key = Integer.parseInt(commandArgs[0]);
                    String newValue = commandArgs[1];

                    storage.put(key, newValue);

                    msg.destroy();
                }
            }
        }

        notifier.close();
        context.term();
    }
}
