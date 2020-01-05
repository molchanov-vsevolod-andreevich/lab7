import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.HashMap;
import java.util.Map;

public class Storage {

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

        System.out.println(storage);

        long timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
        
        ZMQ.Context context = ZMQ.context (1);

        // Socket to talk to server
        ZMQ.Socket notifier = context.socket (SocketType.DEALER);
        notifier.connect ("tcp://localhost:5560");

        System.out.println("Storage has been launched and connected");

        while (!Thread.currentThread().isInterrupted()) {

            if (System.currentTimeMillis() == timeToNofification) {
                notifier.send("NOTIFY " + startIdx + " " + (startIdx + storage.size()), 0);
                timeToNofification = System.currentTimeMillis() + Constants.NOTIFICATION_TIMEOUT;
            }

//            ZMsg msg = ZMsg.recvMsg(notifier);
//
//            if (msg != null) {
//                String cmd = new String(msg.getLast().getData());
//
//                String[] split = cmd.split(" ");
//
//                String commandType = split[0];
//                if (commandType.equals("") ) {
//
//                }
//            }

//            // Wait for next request from client
//            String string = notifier.recvStr (0);
//            System.out.printf ("Received request: [%s]\n", string);
//            // Do some 'work'
//            Thread.sleep (1000);
//            // Send reply back to client
//            notifier.send ("World");
        }

        // We never get here but clean up anyhow notifier.close();
        notifier.close();
        context.term();
    }
}
