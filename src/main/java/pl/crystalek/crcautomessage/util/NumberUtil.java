package pl.crystalek.crcautomessage.util;

import java.util.Optional;

public final class NumberUtil {

    private NumberUtil() {
    }

    public static Optional<Short> isShort(final String number) {
        final short returnNumber;
        try {
            returnNumber = Short.parseShort(number);
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
        return Optional.of(returnNumber);
    }

    public static Optional<Integer> isInt(final String number) {
        final int returnNumber;
        try {
            returnNumber = Integer.parseInt(number);
            if (returnNumber <= 0) {
                throw new NumberFormatException();
            }
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
        return Optional.of(returnNumber);
    }
}
