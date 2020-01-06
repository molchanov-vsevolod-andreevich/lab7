import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralProxy {

    private static final Map<ZFrame, StorageInfo> storages = new HashMap<>();

    private static void removeIrrelevantStorages() {
        List<ZFrame> irrelevantStorages = new ArrayList<>();

        for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
            StorageInfo storage = entry.getValue();

            if (storage.getLastNotificationTime() + (storages.size() + 1) * Constants.NOTIFICATION_TIMEOUT < System.currentTimeMillis()) {
                irrelevantStorages.add(entry.getKey());
            }
        }

        for (ZFrame irrelevantStorage : irrelevantStorages) {
            storages.remove(irrelevantStorage);
        }
    }

    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket client = context.socket(SocketType.ROUTER);
        ZMQ.Socket storage = context.socket(SocketType.ROUTER);
        client.bind("tcp://localhost:5559");
        storage.bind("tcp://localhost:5560");

        System.out.println("Proxy server has been launched and connected");

        ZMQ.Poller items = context.poller(2);
        items.register(client, ZMQ.Poller.POLLIN);
        items.register(storage, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();

            removeIrrelevantStorages();

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(client);

                Command command = new Command(msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT));

                int commandType = command.getCommandType();
                if (commandType == Constants.GET_COMMAND_TYPE) {
                    System.out.println("GET client: " + msg.getFirst());
                    int key = Integer.parseInt(command.getArgs());

                    boolean isKeyValid = false;

                    for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
                        StorageInfo storageInfo = entry.getValue();

                        if (key >= storageInfo.getStartIdx() && key <= storageInfo.getEndIdx()) {
                            System.out.println("Store: " + entry.getKey());
                            System.out.println("Store message: " + msg.getFirst());
                            entry.getKey().send(storage, ZFrame.REUSE + ZFrame.MORE);
                            msg.send(storage);
                            isKeyValid = true;
                            break;
                        }
                    }

                    if (!isKeyValid) {
                        msg.getLast().reset("Key is not Valid");
                        msg.send(client);
                    }
                }

                if (commandType == Constants.PUT_COMMAND_TYPE) {
                    String[] commandArgs = command.getArgs().split(Constants.DELIMITER, Constants.LIMIT);

                    int key = Integer.parseInt(commandArgs[0]);

                    boolean isKeyValid = false;

                    for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
                        StorageInfo storageInfo = entry.getValue();

                        if (key >= storageInfo.getStartIdx() && key <= storageInfo.getEndIdx()) {
                            entry.getKey().send(storage, ZFrame.REUSE + ZFrame.MORE);
                            msg.send(storage);
                            isKeyValid = true;
                        }
                    }

                    if (!isKeyValid) {
                        msg.getLast().reset("Key is not Valid");
                    } else {
                        msg.getLast().reset("Value has been putted");
                    }
                    msg.send(client);
                }
            }

            if (items.pollin(1)) {
                ZMsg msg = ZMsg.recvMsg(storage, false);

                Command command = new Command(msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT));

                int commandType = command.getCommandType();

                if (commandType == Constants.NOTIFY_COMMAND_TYPE) {
                    ZFrame storageID = msg.unwrap();
                    storages.putIfAbsent(storageID, new StorageInfo(command.getArgs()));
                    storages.get(storageID).setLastNotificationTime(System.currentTimeMillis());
                }

                if (commandType == Constants.RESPONSE_COMMAND_TYPE) {
                    msg.remove();
                    String resp = command.getArgs();
                    System.out.println("GET storage: " + msg.getFirst());
                    msg.getLast().reset(resp);
                    msg.send(client);
                }
            }
        }
    }
}
