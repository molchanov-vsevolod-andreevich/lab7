import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralProxy {

    private static ZMQ.Socket client;
    private static ZMQ.Socket storage;

    private static final Map<ZFrame, StorageInfo> storages = new HashMap<>();

    private static void removeIrrelevantStorages() {
        List<ZFrame> irrelevantStorages = new ArrayList<>();

        for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
            StorageInfo storage = entry.getValue();

            if (storage.getLastNotificationTime() + Constants.NOTIFICATION_TIMEOUT <= System.currentTimeMillis()) {
                irrelevantStorages.add(entry.getKey());
            }
        }

        for (ZFrame irrelevantStorage : irrelevantStorages) {
            storages.remove(irrelevantStorage);
        }
    }

    processGetRequest() {

    }

    processPutRequest() {

    }

    processNotifyMsg() {

    }
    
    processResponseMsg()  {

    }

    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(Constants.IO_THREADS);

        client = context.socket(SocketType.ROUTER);
        storage = context.socket(SocketType.ROUTER);
        client.bind(Constants.CLIENT_ADDRESS);
        storage.bind(Constants.STORAGE_ADDRESS);

        System.out.println(Constants.START_PROXY_MESSAGE);

        ZMQ.Poller items = context.poller(Constants.POLLER_SIZE);
        items.register(client, ZMQ.Poller.POLLIN);
        items.register(storage, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();

            removeIrrelevantStorages();

            if (items.pollin(Constants.POLLER_CLIENT_INDEX)) {
                ZMsg msg = ZMsg.recvMsg(client);

                Command command = new Command(msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT));
                System.out.println(msg.getFirst() + ": " + command.prettyPrinting());

                int commandType = command.getCommandType();

                if (commandType == Command.GET_COMMAND_TYPE) {
                    processGetRequest();
                    int key = Integer.parseInt(command.getArgs());

                    boolean isKeyValid = false;

                    for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
                        StorageInfo storageInfo = entry.getValue();

                        if (key >= storageInfo.getStartIdx() && key <= storageInfo.getEndIdx()) {
                            entry.getKey().send(storage, ZFrame.REUSE + ZFrame.MORE);
                            msg.send(storage, Constants.DONT_DESTROY);
                            isKeyValid = true;
                            break;
                        }
                    }

                    if (!isKeyValid) {
                        msg.getLast().reset(Command.KEY_ISNT_VALID_RESPONSE);
                        msg.send(client);
                    }
                }

                if (commandType == Command.PUT_COMMAND_TYPE) {
                    processPutRequest();
                    String[] commandArgs = command.getArgs().split(Constants.DELIMITER, Constants.LIMIT);

                    int key = Integer.parseInt(commandArgs[Constants.KEY_INDEX_IN_ARGS]);

                    boolean isKeyValid = false;

                    for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
                        StorageInfo storageInfo = entry.getValue();

                        if (key >= storageInfo.getStartIdx() && key <= storageInfo.getEndIdx()) {
                            entry.getKey().send(storage, Constants.REUSE_AND_MORE_ZMQ_FLAG);
                            msg.send(storage, Constants.DONT_DESTROY);
                            isKeyValid = true;
                        }
                    }

                    if (isKeyValid) {
                        msg.getLast().reset(Command.VALUE_PUTTED_RESPONSE);
                    } else {
                        msg.getLast().reset(Command.KEY_ISNT_VALID_RESPONSE);
                    }
                    msg.send(client);
                }
            }

            if (items.pollin(Constants.POLLER_STORAGE_INDEX)) {
                ZMsg msg = ZMsg.recvMsg(storage, Constants.DONT_WAIT);

                Command command = new Command(msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT));
                System.out.println(msg.getFirst() + ": " + command.prettyPrinting());

                int commandType = command.getCommandType();

                if (commandType == Command.NOTIFY_COMMAND_TYPE) {
                    processNotifyMsg();
                    ZFrame storageID = msg.unwrap();
                    storages.putIfAbsent(storageID, new StorageInfo(command.getArgs()));
                    storages.get(storageID).setLastNotificationTime(System.currentTimeMillis());
                }

                if (commandType == Command.RESPONSE_COMMAND_TYPE) {
                    processResponseMsg();
                    msg.remove();
                    String resp = command.getArgs();
                    msg.getLast().reset(resp);
                    msg.send(client);
                }
            }
        }

        client.close();
        storage.close();
        context.term();
    }
}
