public class Command {

    // Command Types
    static final int INVALID_COMMAND_TYPE = 0;
    static final int NOTIFY_COMMAND_TYPE = 1;
    static final int PUT_COMMAND_TYPE = 2;
    static final int GET_COMMAND_TYPE = 3;
    static final int QUIT_COMMAND_TYPE = 4;
    static final int RESPONSE_COMMAND_TYPE = 5;

    // Needed Command Lengths
    static final int PUT_COMMAND_NEEDED_LENGTH = 3;
    static final int GET_COMMAND_NEEDED_LENGTH = 2;
    static final int QUIT_COMMAND_NEEDED_LENGTH = 1;

    // Command Indexes
    static final int KEY_INDEX_IN_COMMAND = 1;
    static final int VALUE_INDEX_IN_COMMAND = 2;
    static final int COMMAND_TYPE_INDEX = 0;
    static final int ARGS_INDEX = 1;

    // Responses
    static final String KEY_ISNT_VALID_RESPONSE = "Key is not Valid";
    static final String VALUE_PUTTED_RESPONSE = "Value has been putted";


    private int commandType;
    private String args;

    public Command(String cmd) {
        String[] splittedCmd = cmd.split(Constants.DELIMITER, Constants.LIMIT);

        System.out.println(splittedCmd);

        String commandName = splittedCmd[Constants.COMMAND_NAME_INDEX];

        if (commandName.equals("PUT") &&
                splittedCmd.length == PUT_COMMAND_NEEDED_LENGTH &&
                isInteger(splittedCmd[KEY_INDEX_IN_COMMAND])) {
            commandType = PUT_COMMAND_TYPE;
            args = splittedCmd[KEY_INDEX_IN_COMMAND] + " " + splittedCmd[VALUE_INDEX_IN_COMMAND];
        } else if (commandName.equals("GET") &&
                splittedCmd.length == GET_COMMAND_NEEDED_LENGTH &&
                isInteger(splittedCmd[KEY_INDEX_IN_COMMAND])) {
            commandType = GET_COMMAND_TYPE;
            args = splittedCmd[KEY_INDEX_IN_COMMAND];
        } else if (commandName.equals("Q") &&
                splittedCmd.length == QUIT_COMMAND_NEEDED_LENGTH) {
            commandType = QUIT_COMMAND_TYPE;
        } else {
            commandType = INVALID_COMMAND_TYPE;
        }
    }

    public Command(String[] commandTypeAndArgs) {
        commandType = Integer.parseInt(commandTypeAndArgs[COMMAND_TYPE_INDEX]);
        args = commandTypeAndArgs[ARGS_INDEX];
    }

    public Command(int commandType, String args) {
        this.commandType = commandType;
        this.args = args;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public int getCommandType() {
        return commandType;
    }

    public String getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return commandType + " " + args;
    }

    public String prettyPrinting() {
        String commandName;
        if (commandType == NOTIFY_COMMAND_TYPE) {
            commandName = "NOTIFY";
        } else if (commandType == PUT_COMMAND_TYPE) {
            commandName = "PUT";
        } else if (commandType == GET_COMMAND_TYPE) {
            commandName = "GET";
        } else if (commandType == QUIT_COMMAND_TYPE) {
            commandName = "Q";
        } else if (commandType == RESPONSE_COMMAND_TYPE) {
            commandName = "RESPONSE";
        } else {
            commandName = "INVALID";
        }

        return commandName + " " + args;
    }
}
