import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CentralProxy {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket frontend = context.socket(SocketType.ROUTER); // client
        ZMQ.Socket backend = context.socket(SocketType.ROUTER); // storage
        frontend.bind("tcp://localhost:5559");
        backend.bind("tcp://localhost:5560");

        System.out.println("Proxy server has been launched and connected");

        ZMQ.Poller items = context.poller (2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();
            // poll and memorize multipart detection items.poll();
            if (items.pollin(0)) {
                byte[] message;
                message = frontend.recv(0);
                String resp = "Hie";
//                backend.send(message, more ? ZMQ.SNDMORE : 0);
//                System.out.println("client " + new String(message));
                frontend.send(resp, )
            }

            if (items.pollin(1)) {
                byte[] message;
                message = backend.recv(0);
//                frontend.send(message, more ? ZMQ.SNDMORE : 0);
                System.out.println("storage" + new String(message));
            }
        }
    }
}
