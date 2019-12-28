import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class CentralProxy {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket client = context.socket(SocketType.ROUTER); // client
        ZMQ.Socket storage = context.socket(SocketType.ROUTER); // storage
        client.bind("tcp://localhost:5559");
        storage.bind("tcp://localhost:5560");

        System.out.println("Proxy server has been launched and connected");

        ZMQ.Poller items = context.poller (2);
        items.register(client, ZMQ.Poller.POLLIN);
        items.register(storage, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(client);
                String cmd = new String(msg.getLast().getData());

                msg.getLast().reset(cmd);
                msg.send(client);
            }

            if (items.pollin(1)) {
                ZMsg msg = ZMsg.recvMsg(storage);
                String interval = new String(msg.getLast().getData());
                System.out.println("storage: " + interval);
            }
        }
    }
}
