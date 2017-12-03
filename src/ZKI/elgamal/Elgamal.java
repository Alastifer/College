package ZKI.elgamal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Elgamal {

    private static final Random random = new Random();

    private static final int LAST_CHECK_NUMBER = 20;

    private String message;

    public Elgamal(String message) {
        this.message = message;
    }

    public String encrypt() {
        long p = generateRandomSimpleNumber(LAST_CHECK_NUMBER); //37
        long g = getPrimitiveRoot(p);
        long x = random.nextInt((int) (p - 2)) + 2; //5;
        long y = (long) (Math.pow(g, x) % p);
        long k = getMutuallySimpleNumber((int) p); //7;

        int[] numbers = Arrays.stream(message.split(" ")).mapToInt(Integer::parseInt).toArray();

        long a = (long) Math.pow(g, k) % p;
        long[] b = Arrays.stream(numbers).mapToLong(operand -> (long) (Math.pow(y, k) * operand) % p).toArray();

        System.out.printf("p = %d\n" +
                          "g = %d\n" +
                          "x = %d\n" +
                          "y = %d\n" +
                          "k = %d\n\n", p, g, x, y, k);

        System.out.println("a = " + a);
        System.out.println(Arrays.toString(b));

        return a + "\n" + Arrays.toString(b);
    }

    private int generateRandomSimpleNumber(int lastCheckNumber) {
        List<Integer> simpleNumbers = getSimpleNumbers(lastCheckNumber);
        int numbersAmount = simpleNumbers.size();
        int rand = new Random().nextInt(numbersAmount);

        if (getPrimitiveRoot(simpleNumbers.get(rand)) == -1) {
            return generateRandomSimpleNumber(lastCheckNumber);
        }

        return simpleNumbers.get(rand);
    }

    private List getSimpleNumbers(int lastCheckNumber) {
        boolean[] numbers = new boolean[lastCheckNumber];
        Arrays.fill(numbers, true);
        numbers[1] = false;

        for (int i = 2; i < lastCheckNumber; i++) {
            if (numbers[i]) {
                for (int j = i * i; j < lastCheckNumber; j += i) {
                    numbers[j] = false;
                }
            }
        }

        List<Integer> list = new ArrayList<>();

        for (int i = 2; i < lastCheckNumber; i++) {
            if (numbers[i]) {
                list.add(i);
            }
        }

        return list;
    }

    private int getPrimitiveRoot(long p) {
        for (int i = 1; i < p - 1; i++) {
            if (Math.pow(i, p - 1) % p == 1) {
                boolean isPrimitive = true;
                for (int j = (int) (p - 2); j >= 1; j--) {
                    if (Math.pow(i, j) % p == 1) {
                        isPrimitive = false;
                        break;
                    }
                }

                if (isPrimitive) {
                    return i;
                }
            }
        }

        return -1;
    }

    private int getMutuallySimpleNumber(int number) {
        int randValue = random.nextInt(number - 4) + 2;

        while (gcd(number, randValue) != 1) {
            randValue = random.nextInt(number - 4) + 2;
        }


        return randValue;
    }

    private int gcd(int number, int i) {
        if (i == 0) {
            return number;
        }

        return gcd(i, number % i);
    }

}
