package remoteserver.SystemControl;

/**
 * @TODO recompile C file with correct function name
 */
public final class MediaKeys {

    private MediaKeys() {}

    static {
        System.load(System.getProperty("user.dir") + "\\src\\native\\MediaKeys.dll");
    }

    public static native void volumeUp();
    public static native void VolumeDown();
    public static native void mute();
    public static native void mediaNext();
    public static native void mediaPrevious();
    public static native void mediaPlayPause();
    public static native void mediaStop();
}
