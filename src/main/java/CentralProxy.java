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

        boolean more = false;
        byte[] message;

        while (!Thread.currentThread().isInterrupted()) {
            // poll and memorize multipart detection items.poll();
            if (items.pollin(0)) {
                while (true) {
                    message = frontend.recv(0);
                    more = frontend.hasReceiveMore();
                    backend.send(message, more ? ZMQ.SNDMORE : 0);
                    if(!more){
                        break;
                    }
                }
            } }
            if (items.pollin(1)) {
                while (true) {
                    message = backend.recv(0);
                    more = backend.hasReceiveMore();
                    frontend.send(message, more ? ZMQ.SNDMORE : 0);
                    if(!more){
                        break;
                    }
                }
            }
        }
}
