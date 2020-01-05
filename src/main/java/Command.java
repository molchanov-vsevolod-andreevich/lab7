public class Command {

    private int commandType;
    private String args;

    public Command(String cmd) {
        String[] splittedCmd = cmd.split(" ");

        String commandName = splittedCmd[0];
        if (commandName.equals("NOTIFY") && splittedCmd.length < 3) {

        } else if (commandName.equals("PUT") && splittedCmd.length < 3) {

        } else {

        }
        switch (commandName) {
            case "NOTIFY" && splittedCmd.length < 3:
                if () {
                    commandType = Constants.INVALID_COMMAND_TYPE;
                }
                commandType = Constants.NOTIFY_COMMAND_TYPE;
                break;
            case "PUT":
                commandType = Constants.PUT_COMMAND_TYPE;
                break;
            case "GET":
                commandType = Constants.GET_COMMAND_TYPE;
                break;
            case "Q":
                commandType = Constants.QUIT_COMMAND_TYPE;
                break;
            default:
                commandType = Constants.INVALID_COMMAND_TYPE;
                break;
        }
    }

    public int getCommandType() {
        return commandType;
    }

    public String getArgs() {
        return args;
    }
}
