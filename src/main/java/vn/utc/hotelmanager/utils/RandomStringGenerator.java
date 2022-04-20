package vn.utc.hotelmanager.utils;

import java.util.Random;

public class RandomStringGenerator {
    public static String getString(int len) {
        Random random = new Random();
        int firstLetter = 97;
        int lastLetter = 122;

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int val = firstLetter + random.nextInt(lastLetter - firstLetter + 1);

            // Randomly capitalize a character
            if (val % 2 == 0)
                val -= 32;
            sb.append((char) val);
        }

        return sb.toString();
    }
}
