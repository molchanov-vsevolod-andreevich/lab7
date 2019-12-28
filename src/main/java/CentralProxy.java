public class CentralProxy {
    public static void main(String[] args) {
        Context context = ZMQ.context(1);
        Socket frontend = context.socket(SocketType.ROUTER);
        Socket backend = context.socket(SocketType.DEALER);
        frontend.bind("tcp://*:5559"); backend.bind("tcp://*:5560");
        System.out.println("launch and connect broker.");
// Initialize poll set
        Poller items = context.poller (2); items.register(frontend, Poller.POLLIN); items.register(backend, Poller.POLLIN); boolean more = false;
        byte[] message;
// Switch messages between sockets
        while (!Thread.currentThread().isInterrupted()) {
// poll and memorize multipart detection items.poll();
            
    }
}
