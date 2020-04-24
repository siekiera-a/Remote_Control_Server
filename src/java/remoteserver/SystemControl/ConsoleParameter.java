package remoteserver.SystemControl;

import java.nio.CharBuffer;

public class ConsoleParameter {

    private final static char[] allowedParameters = {
        'l', // log off
        's', // shutdown
        'r', // restart
        'a', // abort shutdown
        'h', // hibernate
        't'  // set timeout
    };

    private char parameter;

    private int timeout = -1;

    private final static int MAX_TIMEOUT = 315_360_000;

    private boolean valid;

    public ConsoleParameter(char parameter, int timeout) {
        if (parameterAllowed(parameter)) {
            this.parameter = Character.toLowerCase(parameter);
            valid = true;
            if (parameter == 't') {
                if (timeout >= 0) {
                    this.timeout = Math.min(timeout, MAX_TIMEOUT);
                } else {
                    valid = false;
                }
            }
        }
    }

    public ConsoleParameter(char parameter) {
        this(parameter, -1);
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        if (valid) {
            String result = "-" + parameter;
            return timeout >= 0 ? (result + " " + timeout) : result;
        }
        return "";
    }

    private boolean parameterAllowed(char c) {
        char character = Character.toLowerCase(c);

        return CharBuffer.wrap(allowedParameters)
            .chars()
            .anyMatch(x -> x == character);
    }

}
