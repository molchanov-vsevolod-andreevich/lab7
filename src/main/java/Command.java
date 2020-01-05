public class Command {

    private static final int NOTIFY_COMMAND_TYPE = 1;
    private static final int _COMMAND_TYPE = 1;
    private static final int NOTIFY_COMMAND_TYPE = 1;

    private int commandType;
    private String args;

    public Command(String cmd) {
        String[] splittedCmd = cmd.split(" ");
        String commandName = splittedCmd[0];
        switch (commandName) {
            case "NOTIFY":
                commandType =
        }
    }

    public int getCommandType() {
        return commandType;
    }

    public String getArgs() {
        return args;
    }
}
