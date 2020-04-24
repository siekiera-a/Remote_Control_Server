package remoteserver.SystemControl;

public class SystemControl {

    private Runtime runtime;

    public SystemControl() {
        runtime = Runtime.getRuntime();
    }

    public boolean hibernate() {
        return execCommandWithOneParameter('h');
    }

    public boolean restart() {
        return execCommandWithOneParameter('r');
    }

    public boolean logout() {
        return execCommandWithOneParameter('l');
    }

    public boolean abortShutdown() {
        return execCommandWithOneParameter('a');
    }

    public boolean shutdownImmadiately() {
        return shutdownWithDelay(0);
    }

    public boolean shutdownWithDelay(int delay) {
        ConsoleParameter parameter1 = new ConsoleParameter('s');
        ConsoleParameter parameter2 = new ConsoleParameter('t', delay);
        return shutdownCommand(new ConsoleParameter[] {parameter1, parameter2});
    }

    public boolean lock() {
        try {
            runtime.exec("rundll32 user32.dll,LockWorkStation");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean shutdownCommand(ConsoleParameter[] parameters) {
        if (runtime != null && parameters.length > 0) {
            try {
                String params = "";
                for (ConsoleParameter parameter : parameters) {
                    if (parameter.isValid()) {
                        params += " " + parameter.toString();
                    }
                }
                runtime.exec("shutdown" + params);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean execCommandWithOneParameter(char parameter) {
        ConsoleParameter param = new ConsoleParameter(parameter);
        return shutdownCommand(new ConsoleParameter[] {param});
    }
}
