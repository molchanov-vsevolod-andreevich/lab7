import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    private static ZMQ.Socket notifier;

    private static long timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
    private static int startIdx;
    private static int endIdx;
    private static final Map<Integer, String> storage = new HashMap<>();

    private static void setInterval(String[] args) {
        startIdx = Integer.parseInt(args[Constants.START_INDEX_IN_ARGS]);
        endIdx = Integer.parseInt(args[Constants.END_INDEX_IN_ARGS]);
    }

    private static void setValues(String[] args) {
        int argsLen = args.length;
        for (int i = Constants.FIRST_VALUE_INDEX_IN_ARGS, j = startIdx; i < argsLen; i++, j++) {
            storage.put(j, args[i]);
        }
    }

    private static void notifyProxy() {
        Command command = new Command(Command.NOTIFY_COMMAND_TYPE, startIdx + " " + endIdx);
        notifier.send(command.toString(), Constants.DEFAULT_ZMQ_FLAG);
        timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
        System.out.println("Sent " + command.prettyPrinting() + " to Proxy\n");
    }

    private static void processGetRequest(Command command, ZMsg msg) {
        int key = Integer.parseInt(command.getArgs());
        String value = storage.get(key);
        System.out.println("Found value \"" + value + "\" by key " + key);

        Command resp = new Command(Command.RESPONSE_COMMAND_TYPE, value);

        msg.getLast().reset(resp.toString());
        System.out.println("Sent " + msg + " to Proxy => " + command.prettyPrinting() + "\n");
        msg.send(notifier);
    }

    private static void processPutRequest(Command command, ZMsg msg) {
        String[] commandArgs = command.getArgs().split(Constants.DELIMITER, Constants.LIMIT);
        int key = Integer.parseInt(commandArgs[Constants.KEY_INDEX_IN_ARGS]);
        String newValue = commandArgs[Constants.VALUE_INDEX_IN_ARGS];

        storage.put(key, newValue);
        System.out.println("New value \"" + newValue + "\" putted by key " + key);

        msg.destroy();
        System.out.println("Message destroyed\n");
    }

    public static void main(String[] args) {
        if (args.length < Constants.QUANTITY_OF_INTERVAL_ARGS) {
            System.err.println(Constants.NOT_ENOUGH_ARGS_ERROR_MESSAGE);
            return;
        }

        setInterval(args);

        int quantityOfNeededArgs = Constants.QUANTITY_OF_INTERVAL_ARGS + endIdx - startIdx + 1;
        if (args.length != quantityOfNeededArgs) {
            System.err.println(Constants.NOT_ENOUGH_ARGS_ERROR_MESSAGE);
            return;
        }

        setValues(args);
        
        ZMQ.Context context = ZMQ.context(Constants.IO_THREADS);

        notifier = context.socket(SocketType.DEALER);
        notifier.connect(Constants.STORAGE_ADDRESS);

        System.out.println(Constants.START_STORAGE_MESSAGE);

        while (!Thread.currentThread().isInterrupted()) {

            if (System.currentTimeMillis() == timeToNofification) {
                notifyProxy();
            }

            ZMsg msg = ZMsg.recvMsg(notifier, Constants.DONT_WAIT);

            if (msg != null) {
                String[] commandTypeAndArgs = msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT);
                Command command = new Command(commandTypeAndArgs);

                System.out.println("Proxy says " + msg + " => " + command.prettyPrinting());

                int commandType = command.getCommandType();

                if (commandType == Command.GET_COMMAND_TYPE) {
                    processGetRequest(command, msg);
                }

                if (commandType == Command.PUT_COMMAND_TYPE) {
                    processPutRequest(command, msg);
                }
            }
        }

        notifier.close();
        context.term();
    }
}
