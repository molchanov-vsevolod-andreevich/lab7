public class Command {

    private int commandType;
    private String args;

    public Command(String cmd) {
        String[] splittedCmd = cmd.split(Constants.DELIMITER);

        String commandName = splittedCmd[0];

        if (commandName.equals("PUT") &&
                splittedCmd.length == 3 &&
                isInteger(splittedCmd[1])) {
            commandType = Constants.PUT_COMMAND_TYPE;
            args = splittedCmd[1] + " " + splittedCmd[2];
        } else if (commandName.equals("GET") &&
                splittedCmd.length == 2 &&
                isInteger(splittedCmd[1])) {
            commandType = Constants.GET_COMMAND_TYPE;
            args = splittedCmd[1];
        } else if (commandName.equals("Q") &&
                splittedCmd.length == 1) {
            commandType = Constants.QUIT_COMMAND_TYPE;
        } else {
            commandType = Constants.INVALID_COMMAND_TYPE;
        }
    }

    public Command(String[] commandTypeAndArgs) {
        commandType = Integer.parseInt(commandTypeAndArgs[0]);
        args = commandTypeAndArgs[1];
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
}
