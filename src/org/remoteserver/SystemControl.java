package org.remoteserver;

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
        CmdParameter parameter1 = new CmdParameter('s');
        CmdParameter parameter2 = new CmdParameter('t', delay);
        return shutdownCommand(new CmdParameter[] {parameter1, parameter2});
    }

    public boolean lock() {
        try {
            runtime.exec("rundll32 user32.dll,LockWorkStation");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean shutdownCommand(CmdParameter[] parameters) {
        if (runtime != null && parameters.length > 0) {
            try {
                String params = "";
                for (CmdParameter parameter : parameters) {
                    if (parameter.isValidParameter()) {
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
        CmdParameter param = new CmdParameter(parameter);
        return shutdownCommand(new CmdParameter[] {param});
    }
}
