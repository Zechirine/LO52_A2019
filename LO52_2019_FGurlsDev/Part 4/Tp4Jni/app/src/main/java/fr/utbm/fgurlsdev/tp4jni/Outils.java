package fr.utbm.fgurlsdev.tp4jni;

public class Outils{
    public static boolean isCorrectContent(String str){
        try {
            Integer num = Integer.parseInt(str);
            return (num>=0 && num<=10);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
