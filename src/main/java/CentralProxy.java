import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class CentralProxy {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket client = context.socket(SocketType.ROUTER);
        ZMQ.Socket storage = context.socket(SocketType.ROUTER);
        client.bind("tcp://localhost:5559");
        storage.bind("tcp://localhost:5560");

        System.out.println("Proxy server has been launched and connected");

        ZMQ.Poller items = context.poller (2);
        items.register(client, ZMQ.Poller.POLLIN);
        items.register(storage, ZMQ.Poller.POLLIN);

        int startIdx = 0;
        int endIdx = -1;

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(client);
                String cmd = new String(msg.getLast().getData());

//                String[] split = cmd.split(" ");
//
//                String commandType = split[0];
//                if (commandType.equals("GET")) {
//                    int key = Integer.parseInt(split[1]);
//                    if (key >= startIdx && key <= endIdx) {
//
//                    }
//                } else if (commandType.equals("SET")) {
//
//                }
//                System.out.println("first " + msg.getFirst());
//                ZMsg resp = ZMsg.newStringMsg();
//                resp.getFirst().reset(msg.getFirst());
//                resp.getLast().reset(cmd);
//                msg.getLast().reset(String.valueOf(msg.getFirst()));
//                resp.getLast().reset(cmd);
//                ZFrame resp = msg.unwrap();
//                resp.send(client);
//                clientAddress.send();
                msg.getLast().reset(cmd);
                msg.send(client);
            }

            if (items.pollin(1)) {
                ZMsg msg = ZMsg.recvMsg(storage);

                Command command = new Command(msg.getLast().toString());

                int commandType = command.getCommandType();

                if (commandType == Constants.NOTIFY_COMMAND_TYPE) {
                    
                }

                if (commandType == Constants.RESPONSE_COMMAND_TYPE) {
                    String resp = command.getArgs();
                    msg.getLast().reset(resp);
                    msg.send(client);
                }
            }
        }
    }
}
