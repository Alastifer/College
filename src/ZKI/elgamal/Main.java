package ZKI.elgamal;

import ZKI.gost.IO;

public class Main {

    public static void main(String[] args) {
        IO IO = new IO("input.txt", "output.txt");
        String message = IO.readMessageFromFile();

        String cryptedMessage = new Elgamal(message).encrypt();

        IO.writeStringInFile(cryptedMessage);
    }

}
