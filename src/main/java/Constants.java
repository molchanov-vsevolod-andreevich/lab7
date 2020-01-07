import org.zeromq.ZFrame;

class Constants {
    // Addresses
    static final String CLIENT_ADDRESS = "tcp://localhost:5559";
    static final String STORAGE_ADDRESS = "tcp://localhost:5560";

    // Messages
    static final String NOT_ENOUGH_ARGS_ERROR_MESSAGE = "Not enough arguments. Run program with arguments [startIdx] [endIdx] " +
            "[value1] [value2] ... [valueN], where N = endIdx - startIdx + 1";
    static final String INVALID_COMMAND_MESSAGE = "Invalid command or arguments. Use following commands:\n" +
            "\tPUT [index] [value]\n" +
            "\tGET [index]\n" +
            "\tQ for quit";
    static final String START_CLIENT_MESSAGE = "Сlient has been launched and connected";
    static final String START_STORAGE_MESSAGE = "Storage has been launched and connected";
    static final String START_PROXY_MESSAGE = "Proxy server has been launched and connected";

    // ZMQ Flags
    static final int DEFAULT_ZMQ_FLAG = 0;
    static final int REUSE_AND_MORE_ZMQ_FLAG = ZFrame.REUSE + ZFrame.MORE;

    // Poller constants
    static final int POLLER_SIZE = 2;
    static final int POLLER_CLIENT_INDEX = 0;
    static final int POLLER_STORAGE_INDEX = 1;

    // Indexes
    static final int START_INDEX_IN_ARGS = 0;
    static final int END_INDEX_IN_ARGS = 1;
    static final int FIRST_VALUE_INDEX_IN_ARGS = 2;
    static final int QUANTITY_OF_INTERVAL_ARGS = 2;
    static final int KEY_INDEX_IN_ARGS = 0;
    static final int VALUE_INDEX_IN_ARGS = 1;
    static final int COMMAND_NAME_INDEX = 0;

    // Other constants
    static final String DELIMITER = " ";
    static final int LIMIT = 2;
    static final int IO_THREADS = 1;
    static final long NOTIFICATION_TIMEOUT = 3000;
    static final boolean DONT_WAIT = false;
    static final boolean DONT_DESTROY = false;
}
