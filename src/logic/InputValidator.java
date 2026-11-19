package logic;

public class InputValidator {

    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isValidPositiveInt(String input) {
        if (!isNotEmpty(input)) {
            return false;
        }
        try {
            int value = Integer.parseInt(input.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidNonNegativeDouble(String input) {
        if (!isNotEmpty(input)) {
            return false;
        }
        try {
            double value = Double.parseDouble(input.trim());
            return !Double.isNaN(value) && !Double.isInfinite(value) && value >= 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
