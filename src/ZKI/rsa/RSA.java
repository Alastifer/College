package ZKI.rsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RSA {

    private static final int LAST_CHECK_NUMBER = 10;

    private String message;

    private int n;

    private int e;

    public RSA(String message) {
        this.message = message;
    }

    public String encrypt() {
        int p = generateRandomSimpleNumber(LAST_CHECK_NUMBER);
        int q = generateRandomSimpleNumber(LAST_CHECK_NUMBER);

        while (p == q)
        {
            p = generateRandomSimpleNumber(LAST_CHECK_NUMBER);
        }

        n = p * q;

        int m = (p - 1) * (q - 1);

        int d = getMutuallySimpleNumber(m);

        e = getE(d, m);

        System.out.printf("p = %d\nq = %d\nn = %d\nm = %d\nd = %d\ne = %d\n", p, q, n, m, d, e);

        String[] blocks = getBlocks(message);

        System.out.println(Arrays.toString(blocks));

        return changeBlocks(blocks);
    }

    private String changeBlocks(String[] blocks) {
        StringBuilder answer = new StringBuilder();

        for (String block : blocks) {
            int blockValue = Integer.parseInt(block, 2);
            blockValue = (int) (Math.pow(blockValue, e) % n);
            answer.append(Integer.toBinaryString(blockValue));
        }

        return answer.toString();
    }

    private String[] getBlocks(String message) {
        int messageLength = message.length();

        StringBuilder messageBuilder = new StringBuilder(message);
        while (messageLength % n != 0) {
            messageBuilder.insert(0, "0");
            messageLength++;
        }
        
        message = messageBuilder.toString();
        
        String[] blocks = new String[n];
        
        int symbolesInBlock = messageLength / n;
        for (int i = 0; i < n; i++) {
            blocks[i] = message.substring(i * symbolesInBlock, (i + 1) * symbolesInBlock);
        }

        return blocks;
    }

    private int getE(int d, int m) {
        for (int i = 2; i < LAST_CHECK_NUMBER * 10; i++) {
            if (i * d % m == 1) {
                return i;
            }
        }

        return -1;
    }

    private int generateRandomSimpleNumber(int lastCheckNumber) {
        List<Integer> simpleNumbers = getSimpleNumbers(lastCheckNumber);
        int numbersAmount = simpleNumbers.size();
        int rand = new Random().nextInt(numbersAmount);

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

    private int getMutuallySimpleNumber(int number) {
        for (int i = new Random().nextInt(LAST_CHECK_NUMBER - 2) + 2; i < LAST_CHECK_NUMBER; i++) {
            if (gcd(number, i) == 1) {
                return i;
            }
        }

        return -1;
    }

    private int gcd(int number, int i) {
        if (i == 0) {
            return number;
        }

        return gcd(i, number % i);
    }

}
