import java.time.Duration;

class Constants {
    // Server constants
    static final String HOST = "localhost";
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

    // Command Types
    static final int INVALID_COMMAND_TYPE = 0;
    static final int NOTIFY_COMMAND_TYPE = 1;
    static final int PUT_COMMAND_TYPE = 2;
    static final int GET_COMMAND_TYPE = 3;
    static final int QUIT_COMMAND_TYPE = 4;
    static final int RESPONSE_COMMAND_TYPE = 5;

    // ZeroFlags

    // HTTP query parameters
    static final String URL_PARAMETER_NAME = "url";
    static final String COUNT_PARAMETER_NAME = "count";

    // Other constants
    static final Duration TIMEOUT = Duration.ofMillis(5000);
    static final int SERVER_PORT_IDX_IN_ARGS = 0;
    static final String DELIMITER = " ";
    static final int LIMIT = 2;

    static final long NOTIFICATION_TIMEOUT = 3000;
}
