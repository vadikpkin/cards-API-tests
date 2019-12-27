package util;

import java.util.Random;

public class DataHelper {
    public static int getRandomMoneyInt() {
        Random rand = new Random();
        return (1 + rand.nextInt(10000 - 1 + 1));
    }

    public static int getRandomMoneyIntOutOfBounds() {
        Random rand = new Random();
        int a = 0;

        do {
            a = (1 + rand.nextInt(100000 - 10000));
        } while (a < 11000);

        return a;
    }
}
