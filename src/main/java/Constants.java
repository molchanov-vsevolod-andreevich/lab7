import java.time.Duration;

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

    // Command Types
    static final int INVALID_COMMAND_TYPE = 0;
    static final int NOTIFY_COMMAND_TYPE = 1;
    static final int PUT_COMMAND_TYPE = 2;
    static final int GET_COMMAND_TYPE = 3;
    static final int QUIT_COMMAND_TYPE = 4;
    static final int RESPONSE_COMMAND_TYPE = 5;

    // ZMQ Flags
    static final int DEFAULT_ZMQ_FLAG = 0;

    // Other constants
    static final String DELIMITER = " ";
    static final int LIMIT = 2;
    static final int IO_THREADS = 1;
    static final long NOTIFICATION_TIMEOUT = 3000;

    // Poller constants
    static final int POLLER_SIZE = 2;
}
