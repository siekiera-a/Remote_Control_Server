package remoteserver.SystemControl;

public class CmdParameter {
    private final char[] allowedParameters = { 'l', 's', 'r', 'a', 'h', 't'};
    private char parameter;
    private int value = -1;
    // Max value allowed for t parameter
    private final int maxValue = 315360000;
    private boolean isValid = false;

    public CmdParameter(char parameter, int value) {
        if (isParameterAllowed(Character.toLowerCase(parameter))) {
            this.parameter = Character.toLowerCase(parameter);
            if (parameter == 't' && value >= 0) {
                this.value = Math.min(value, maxValue);
            }
            isValid = true;
        }
    }

    public CmdParameter(char parameter) {
        this(parameter, -1);
    }

    private boolean isParameterAllowed(char c) {
        boolean result = false;
        for (char parameter : allowedParameters) {
            if (parameter == c) {
               result = true;
               break;
            }
        }
        return result;
    }

    public boolean isValidParameter() {
        return isValid;
    }

    @Override
    public String toString() {
        if (isValid) {
            String result = "-" + parameter;
            if (value >= 0) {
                result += " " + value;
            }
            return result;
        } else return "";
    }

}
