package remoteserver.SystemControl;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * methods return true if action executed successfully, otherwise false
 */
public class SystemService {

    private final Runtime runtime;

    public SystemService() {
        runtime = Runtime.getRuntime();
    }

    public boolean hibernate() {
        return shutdownCommand('h');
    }

    public boolean restart() {
        return shutdownCommand('r');
    }

    public boolean restartWithTimeout(int timeout) {
        return withTimeout('r', timeout);
    }

    public boolean logout() {
        return shutdownCommand('l');
    }

    public boolean abort() {
        return shutdownCommand('a');
    }

    /**
     *  shutdown immediately
     */
    public boolean shutdown() {
        return withTimeout('s', 0);
    }

    public boolean shutdownWithTimeout(int timeout) {
        return withTimeout('s', timeout);
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
        String params;

        if (parameters.length == 1) {
            params = parameters[0].toString();
        } else {
            params = Arrays.stream(parameters)
                .filter(ConsoleParameter::isValid)
                .map(ConsoleParameter::toString)
                .collect(Collectors.joining(" "));
        }

        try {
            if (!params.isBlank()) {
                runtime.exec("shutdown " + params);
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    private boolean shutdownCommand(char parameter) {
        return shutdownCommand(new ConsoleParameter[]{new ConsoleParameter(parameter)});
    }

    private boolean withTimeout(char parameter, int timeout) {
        return shutdownCommand(new ConsoleParameter[]{
            new ConsoleParameter(parameter),
            new ConsoleParameter('t', timeout)
        });
    }
}
