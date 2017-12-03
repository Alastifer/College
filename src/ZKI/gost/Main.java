package ZKI.gost;

import ZKI.gost.Gost;

public class Main {

    public static void main(String[] args) {
        IO IO = new IO("input.txt", "output.txt");
        String message = IO.readMessageFromFile();
        String key = IO.readKeyFromFile();

        Gost Gost = new Gost();
        Gost.setMessage(message);
        Gost.setKey(key);

        String cypherText = Gost.encrypt();

        IO.writeStringInFile(cypherText);
    }

}
