package remoteserver.SystemControl;

public final class MediaKeys {

    private MediaKeys() {
    }

    static {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.load(System.getProperty("user.dir") + "\\src\\native\\MediaKeys.dll");
        }
    }

    public static native void volumeUp();

    public static native void volumeDown();

    public static native void mute();

    public static native void mediaNext();

    public static native void mediaPrevious();

    public static native void mediaPlayPause();

    public static native void mediaStop();
}
