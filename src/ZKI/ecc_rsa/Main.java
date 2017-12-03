package ZKI.ecc_rsa;

import ZKI.gost.IO;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        IO IO = new IO("input.txt", "output.txt");
        String message = IO.readMessageFromFile();

        ECC_RSA eccRsa = new ECC_RSA(message);

        BigInteger sign = eccRsa.getSign();

        if (eccRsa.checkMessage(message + "   ", sign)) {
            System.out.println("Подпись верна");
        } else {
            System.out.println("Подпись неверна");
        }
    }

}
