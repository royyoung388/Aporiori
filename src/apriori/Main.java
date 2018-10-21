package apriori;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        byte[][] commodity = {{1, 0, 1, 1, 0}, {0, 1, 1, 0, 1}, {1, 1, 1, 0, 1}, {0, 1, 0, 0, 1}};

        long seed = Calendar.getInstance().getTimeInMillis();
        Random random = new Random(seed);

        int dataSize = (int) (1000 + 1000 * random.nextDouble());
        int goodSize = (int) (10 + 10 * random.nextDouble());

//        int dataSize = 15;
//        int goodSize = 5;

        System.out.println("dataSize = " + dataSize);
        System.out.println("goodSize = " + goodSize);

        byte[][] commodity = new byte[dataSize][goodSize];

        for (int i = 0; i < dataSize; i++) {
            for (int j = 0; j < goodSize; j++) {
                commodity[i][j] = (byte) random.nextInt(2);
            }
        }

//        System.out.println("commodity = " + Arrays.deepToString(commodity));

        Aporiori aporiori = new Aporiori(commodity, 0.8, 0.05);
        aporiori.calc();
    }
}
