import org.omg.PortableInterceptor.INACTIVE;
import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.*;

public class CentralProxy {

    private static ZMQ.Socket client;
    private static ZMQ.Socket storage;

    private static final Map<ZFrame, StorageInfo> storages = new HashMap<>();

    private static void removeIrrelevantStorages() {
        List<ZFrame> irrelevantStorages = new ArrayList<>();

        for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
            StorageInfo storage = entry.getValue();

            if (storage.getLastNotificationTime() + storages.size() * Constants.NOTIFICATION_TIMEOUT <= System.currentTimeMillis()) {
                irrelevantStorages.add(entry.getKey());
            }
        }

        for (ZFrame irrelevantStorage : irrelevantStorages) {
            storages.remove(irrelevantStorage);
            System.out.println("Storage with id " + irrelevantStorage + " has been deleted");
        }
        if (!irrelevantStorages.isEmpty()) {
            System.out.println();
        }

    }

    private static void processGetRequest(Command command, ZMsg msg) {
        int key = Integer.parseInt(command.getArgs());

        List<ZFrame> suitableStorageIndexes = new ArrayList<>();

        for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
            StorageInfo storageInfo = entry.getValue();

            if (key >= storageInfo.getStartIdx() && key <= storageInfo.getEndIdx()) {
                System.out.println("Found suitable storage with id " + entry.getKey());
                suitableStorageIndexes.add(entry.getKey());
            }
        }

        if (suitableStorageIndexes.isEmpty()) {
            System.out.println(Command.KEY_ISNT_VALID_RESPONSE);

            msg.getLast().reset(Command.KEY_ISNT_VALID_RESPONSE);
            System.out.println("Sent " + msg + " to Client => " + Command.KEY_ISNT_VALID_RESPONSE + "\n");

            msg.send(client);
        } else {
            Random randomSuitableStorageIdx = 
            entry.getKey().send(storage, ZFrame.REUSE + ZFrame.MORE);

            msg.send(storage, Constants.DONT_DESTROY);
            System.out.println("Sent " + msg + " to Storage => " + command.prettyPrinting() + "\n");
        }
    }

    private static void processPutRequest(Command command, ZMsg msg) {
        String[] commandArgs = command.getArgs().split(Constants.DELIMITER, Constants.LIMIT);

        int key = Integer.parseInt(commandArgs[Constants.KEY_INDEX_IN_ARGS]);

        boolean isKeyValid = false;

        for (Map.Entry<ZFrame, StorageInfo> entry : storages.entrySet()) {
            StorageInfo storageInfo = entry.getValue();

            if (key >= storageInfo.getStartIdx() && key <= storageInfo.getEndIdx()) {
                System.out.println("Found suitable storage with id " + entry.getKey());
                entry.getKey().send(storage, Constants.REUSE_AND_MORE_ZMQ_FLAG);

                msg.send(storage, Constants.DONT_DESTROY);
                System.out.println("Sent " + msg + " to Storage => " + command.prettyPrinting());

                isKeyValid = true;
            }
        }

        if (isKeyValid) {
            msg.getLast().reset(Command.VALUE_PUTTED_RESPONSE);
            System.out.println("Sent " + msg + " to Client => " + Command.VALUE_PUTTED_RESPONSE + "\n");
        } else {
            msg.getLast().reset(Command.KEY_ISNT_VALID_RESPONSE);
            System.out.println("Sent " + msg + " to Client => " + Command.KEY_ISNT_VALID_RESPONSE + "\n");
        }
        msg.send(client);
    }

    private static void processNotifyMsg(Command command, ZMsg msg) {
        ZFrame storageID = msg.unwrap();
        int prevNumberOfStorages = storages.size();
        storages.putIfAbsent(storageID, new StorageInfo(command.getArgs()));
        storages.get(storageID).setLastNotificationTime(System.currentTimeMillis());

        if (storages.size() > prevNumberOfStorages) {
            System.out.println("New storage with id " + storageID + " has been registered\n");
        } else {
            System.out.println("Timeout of storage with id " + storageID + " has been updated\n");
        }
    }

    private static void processResponseMsg(Command command, ZMsg msg)  {
        msg.remove();
        System.out.println("Storage id has been deleted from message " + msg);

        String resp = command.getArgs();
        msg.getLast().reset(resp);
        System.out.println("Sent " + msg + " to Client => " + resp + "\n");

        msg.send(client);
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
                System.out.println("Client says " + msg + " => " + command.prettyPrinting());

                int commandType = command.getCommandType();

                if (commandType == Command.GET_COMMAND_TYPE) {
                    processGetRequest(command, msg);
                }

                if (commandType == Command.PUT_COMMAND_TYPE) {
                    processPutRequest(command, msg);
                }
            }

            if (items.pollin(Constants.POLLER_STORAGE_INDEX)) {
                ZMsg msg = ZMsg.recvMsg(storage, Constants.DONT_WAIT);

                Command command = new Command(msg.getLast().toString().split(Constants.DELIMITER, Constants.LIMIT));
                System.out.println("Storage says " + msg + " => " + command.prettyPrinting());

                int commandType = command.getCommandType();

                if (commandType == Command.NOTIFY_COMMAND_TYPE) {
                    processNotifyMsg(command, msg);
                }

                if (commandType == Command.RESPONSE_COMMAND_TYPE) {
                    processResponseMsg(command, msg);
                }
            }
        }

        client.close();
        storage.close();
        context.term();
    }
}
