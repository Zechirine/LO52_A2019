package fr.utbm.fgurlsdev.tp4jni;

public class NDKTools {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native static String stringFromJNI();
    public native static String sayHi();
    public native static String readBtn(String numStr);
    public native static String writeBtn(String numStr);
    public native static String directionBtn(String direction);
}
