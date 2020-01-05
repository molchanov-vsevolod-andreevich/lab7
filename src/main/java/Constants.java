import java.time.Duration;

class Constants {
    // Server constants
    static final String HOST = "localhost";

    // Messages
    static final String START_MESSAGE = "start!";
    static final String START_SERVER_MESSAGE = "Server online at http://localhost:8080/\nPress RETURN to stop...\n";
    static final String NOT_ENOUGH_ARGS_ERROR_MESSAGE = "Not enough arguments. Run program with arguments [startIdx] [endIdx] " +
            "[value1] [value2] ... [valueN], where N = endIdx - startIdx + 1";
    static final String REDIRECT_MESSAGE = "Redirect to ";
    static final String FETCH_MESSAGE = "fetch ";
    static final String WATCHER_MESSAGE = "Servers list has been updated";

    // Command Types
    static final int INVALID_COMMAND_TYPE = 0;
    static final int NOTIFY_COMMAND_TYPE = 1;
    static final int PUT_COMMAND_TYPE = 2;
    static final int GET_COMMAND_TYPE = 3;
    static final int QUIT_COMMAND_TYPE = 4;

    // HTTP query parameters
    static final String URL_PARAMETER_NAME = "url";
    static final String COUNT_PARAMETER_NAME = "count";

    // Other constants
    static final Duration TIMEOUT = Duration.ofMillis(5000);
    static final int SERVER_PORT_IDX_IN_ARGS = 0;

    static final long NOTIFICATION_TIMEOUT = 3000;
}
