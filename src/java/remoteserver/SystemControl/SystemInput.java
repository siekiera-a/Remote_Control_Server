package remoteserver.SystemControl;

import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import remoteserver.SystemControl.exceptions.RobotException;

import java.awt.event.InputEvent;
import java.util.Arrays;

/**
 * This class allows you to emit user input from keyboard and mouse
 */
public final class SystemInput {

    private static SystemInput systemInput;

    private final Robot robot;

    public enum MouseButton {
        LPM, PPM, SCROLL_WHEEL
    }

    public enum Shortcut {
        CTRL_Z,
        CTRL_X,
        CTRL_C,
        CTRL_V,
        CTRL_A,
        CTRL_D,
        CTRL_Y,
        CTRL_S,
        CTRL_P,
        ALT_F4,
        WINDOWS_D,
        SHIFT_DELETE
    }

    /**
     * @throws RobotException if system does not allow to control input or you have not permissions
     */
    private SystemInput() throws RobotException {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RobotException("System does not allow to control input", RobotException.ErrorCode.NOT_ALLOWED);
        } catch (SecurityException e) {
            throw new RobotException("You have not permissions to control input", RobotException.ErrorCode.NO_PERMISSION);
        }
    }

    /**
     * Get instance of SystemInput class
     *
     * @throws RobotException if system does not allow to control input or you have not permissions
     */
    public static SystemInput getInstance() throws RobotException {
        if (systemInput == null) {
            systemInput = new SystemInput();
        }

        return systemInput;
    }

    /**
     * Press and release keyboard key
     *
     * @param keyCode (e.g. KeyEvent.VK_A)
     * @return true if keyCode is valid, false otherwise
     */
    public boolean clickKey(int keyCode) {
        try {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Press and release mouse key
     */
    public void mouseClick(MouseButton button) {
        int buttonKey;

        if (button == MouseButton.LPM) {
            buttonKey = InputEvent.BUTTON1_DOWN_MASK;
        } else if (button == MouseButton.SCROLL_WHEEL) {
            buttonKey = InputEvent.BUTTON2_DOWN_MASK;
        } else {
            buttonKey = InputEvent.BUTTON3_DOWN_MASK;
        }

        robot.mousePress(buttonKey);
        robot.mouseRelease(buttonKey);
    }

    /**
     * Use shortcut from list
     */
    public void useShortcut(Shortcut shortcut) {
        String[] keys = shortcut.name()
            .replace("CTRL", "CONTROL")
            .split("_");

        int[] keyCodes = Arrays.stream(keys)
            .mapToInt(key -> AWTKeyStroke.getAWTKeyStroke(key).getKeyCode())
            .toArray();

        pressKeys(keyCodes);
        releaseKeys(keyCodes);
    }

    /**
     * Move mouse pointer from current location by x, y
     */
    public void mouseMove(int x, int y) {
        Point currentPosition = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(x + currentPosition.x, y + currentPosition.y);
    }

    private void pressKeys(int[] keys) {
        for (int key : keys) {
            robot.keyPress(key);
        }
    }

    private void releaseKeys(int[] keys) {
        for (int key : keys) {
            robot.keyRelease(key);
        }
    }

}
