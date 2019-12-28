import java.time.Duration;

class ZookeeperAppConstants {
    // Actors constants
    static final String ACTOR_SYSTEM_NAME = "routes";
    static final String CACHE_ACTOR_NAME = "cacheActor";

    // Server constants
    static final String HOST = "localhost";

    // Zookeeper constants
    static final String ZOOKEEPER_PORT = "2181";
    static final String ZOOKEEPER_SERVER = "127.0.0.1";
    static final int ZOOKEEPER_SESSION_TIMEOUT = 5000;
    static final String SERVERS_NODE = "/servers";
    static final String SERVERS_NODES_PATH = SERVERS_NODE + "/";
    static final String SERVER_NODE = SERVERS_NODES_PATH + "s";

    // Messages
    static final String START_MESSAGE = "start!";
    static final String START_SERVER_MESSAGE = "Server online at http://localhost:8080/\nPress RETURN to stop...\n";
    static final String NOT_ENOUGH_ARGS_ERROR_MESSAGE = "Not enough arguments. Run program with argument [serverPort]";
    static final String REDIRECT_MESSAGE = "Redirect to ";
    static final String FETCH_MESSAGE = "fetch ";
    static final String WATCHER_MESSAGE = "Servers list has been updated";

    // HTTP query parameters
    static final String URL_PARAMETER_NAME = "url";
    static final String COUNT_PARAMETER_NAME = "count";

    // Other constants
    static final Duration TIMEOUT = Duration.ofMillis(5000);
    static final int SERVER_PORT_IDX_IN_ARGS = 0;
}
