package org.consumersunion.stories;

public class Random {
    private static final char[] SYMBOLS;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) {
            tmp.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            tmp.append(ch);
        }
        SYMBOLS = tmp.toString().toCharArray();
    }

    private static final java.util.Random RANDOM = new java.util.Random();

    private static char[] BUF;

    public static String string(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }

        BUF = new char[length];

        for (int idx = 0; idx < BUF.length; ++idx) {
            BUF[idx] = SYMBOLS[RANDOM.nextInt(SYMBOLS.length)];
        }
        return new String(BUF);
    }

    public static String string() {
        int length = integer(1, 20);
        return string(length);
    }

    public static int integer(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
