public class Command {

    private static final int INVALID_COMMAND_TYPE = 0;
    private static final int NOTIFY_COMMAND_TYPE = 1;
    private static final int PUT_COMMAND_TYPE = 2;
    private static final int GET_COMMAND_TYPE = 3;
    private static final int EXIT_COMMAND_TYPE = 4;

    private int commandType;
    private String args;

    public Command(String cmd) {
        String[] splittedCmd = cmd.split(" ");

        String commandName = splittedCmd[0];
        switch (commandName) {
            case "NOTIFY":
                commandType = 1;
                break;
            case     
        }
    }

    public int getCommandType() {
        return commandType;
    }

    public String getArgs() {
        return args;
    }
}
