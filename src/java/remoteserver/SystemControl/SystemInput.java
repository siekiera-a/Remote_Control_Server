package remoteserver.SystemControl;

import java.awt.AWTException;
import java.awt.AWTKeyStroke;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import remoteserver.SystemControl.exceptions.RobotException;

import java.awt.event.InputEvent;


/**
    This class allows you to emit user input from keyboard and mouse
 */
public class SystemInput {


    private Robot robot = null;

    public enum MouseButton {
        LPM, PPM, SCROLL_WHEEL
    }

    public enum Shortcut {
        CTRL_Z, CTRL_X, CTRL_C, CTRL_V, CTRL_A, CTRL_D, CTRL_Y, CTRL_S, CTRL_P, ALT_F4, WINDOWS_D, SHIFT_DELETE
    }

    /**
     * Create class Robot instance
     * @throws RobotException if system does not allow to control input or you have not permission
     */
    public SystemInput() throws RobotException  {
        try {
            robot = new Robot();
        }
        catch(AWTException e) {
            throw new RobotException("System does not allow to control input", RobotException.ErrorCode.NOT_ALLOWED);
        }
        catch (SecurityException e) {
            throw new RobotException("You have not permission to control input", RobotException.ErrorCode.NO_PERMISSION);
        }
    }


    /**
     Press and Release Key from keyboard
     @param keyCode Int from KeyEvent (e.g. KeyEvent.VK_A)
     @return true if keyCode is valid, false otherwise
    */
    public boolean clickKey(int keyCode) {
        if (robot == null) return false;

        try {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Use shortcut from list
     */
    public void useShortcut(Shortcut shortcut) {
        if(robot == null) return;

        String[] keys = shortcut.name().toUpperCase().split("_");
        if (keys[0].equals("CTRL")) keys[0] = "CONTROL";
        int[] keyCodes = new int[keys.length];
        for (int i = 0; i < keyCodes.length; i++) {
            keyCodes[i] = AWTKeyStroke.getAWTKeyStroke(keys[i]).getKeyCode();
        }
        pressKeys(keyCodes);
        releaseKeys(keyCodes);
    }

    private void pressKeys(int[] keys) {
        if (robot == null) return;

        for (int key : keys) {
            robot.keyPress(key);
        }
    }

    private void releaseKeys(int[] keys) {
        if (robot == null) return;

        for (int key : keys) {
            robot.keyRelease(key);
        }
    }

    public void mouseClick(MouseButton button) {
        if (robot == null) return;

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
     * @param button 1 = LPM, 2 = SCROLL_WHEEL, 3 = PPM
     */
    public void mouseClick(int button) {
        if (robot == null) return;

        int buttonKey;
        switch (button) {
            case 1:
                buttonKey = InputEvent.BUTTON1_DOWN_MASK;
                break;
            case 2:
                buttonKey = InputEvent.BUTTON2_DOWN_MASK;
                break;
            case 3:
                buttonKey = InputEvent.BUTTON3_DOWN_MASK;
                break;
            default:
                buttonKey = 0;
        }

        if (buttonKey != 0) {
            robot.mousePress(buttonKey);
            robot.mouseRelease(buttonKey);
        }
    }

    /**
     * Moves mouse from current location by x, y
     */
    public void mouseMove(int x, int y) {
        if (robot == null) return;
        Point currentPosition = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(x + currentPosition.x,y + currentPosition.y);
    }

}
