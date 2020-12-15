package pl.crystalek.crcautomessage.utils;

public class NumberUtil {

    public static boolean isShort(final String number) {
        try {
            Short.parseShort(number);
        } catch (final NumberFormatException exception) {
            return false;
        }
        return true;
    }

    public static boolean isInt(final String number) {
        try {
            Integer.parseInt(number);
        } catch (final NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
