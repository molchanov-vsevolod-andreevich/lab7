public class Command {

    private int commandType;
    private String args;

    public Command(String cmd) {
        String[] splittedCmd = cmd.split(" ");
        String commandName = splittedCmd[0];
        switch (commandName) {
            case "NOTIFY":
                
        }
    }

    public int getCommandType() {
        return commandType;
    }

    public String getArgs() {
        return args;
    }
}
