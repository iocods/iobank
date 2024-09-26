package iocode.web.app.util;

/**
 * This utility class provides methods for generating random numbers.
 */
public class RandomUtil {

    /**
     * Generates a random number of the specified length.
     *
     * @param length the length of the random number to be generated.
     * @return a random number of the specified length.
     * @throws NumberFormatException if the generated number cannot be parsed to a long.
     */
    public Long generateRandom(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());
    }
}
