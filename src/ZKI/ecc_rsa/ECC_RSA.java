package ZKI.ecc_rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ECC_RSA {

    private static final int LAST_CHECK_NUMBER = 1000;

    private String message;

    private int n;

    private int e;

    public ECC_RSA(String message) {
        this.message = message;
    }

    public BigInteger getSign() {
        int p = generateRandomSimpleNumber(LAST_CHECK_NUMBER);
        int q = generateRandomSimpleNumber(LAST_CHECK_NUMBER);

        while (p == q) {
            p = generateRandomSimpleNumber(LAST_CHECK_NUMBER);
        }

        n = p * q;

        int M = (p - 1) * (q - 1);

        int d = getMutuallySimpleNumber(M);

        e = getE(d, M);

        System.out.printf("p = %d q = %d\n", p, q);
        System.out.printf("n = %d\nd = %d\ne = %d\n", n, d, e);

        int m = message.hashCode();

        System.out.printf("h = %d\n", m);

        BigInteger s = new BigInteger(String.valueOf(m));
        s = s.pow(d);
        s = s.mod(new BigInteger(String.valueOf(n)));

        System.out.println("sign = " + s);

        return s;
    }

    public boolean checkMessage(String message, BigInteger s) {
        return message.hashCode() % n == s.pow(e).mod(BigInteger.valueOf(n)).intValue();
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
        for (int i = 2; i < LAST_CHECK_NUMBER * 10000; i++) {
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
