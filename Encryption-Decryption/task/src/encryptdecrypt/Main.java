package encryptdecrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//import java.util.Scanner;
abstract class Crpyt {

    public void act(String action, String data, int key) {
        // write your code here ...
        if (action.equals("dec")) {
            decrypt(data, key);
        } else {
            encrypt(data, key);
        }

    }

    public abstract void encrypt(String data, int key);

    public abstract void decrypt(String data, int key);


}

class Unicode extends Crpyt {
    //    StringBuilder output = new StringBuilder();
    @Override
    public void encrypt(String data, int key) {
        for (int i = 0; i < data.length(); i++) {
            char index = (char) (data.charAt(i) + key);
            Main.output.append(index);
        }
    }

    @Override
    public void decrypt(String data, int key) {
        for (int i = 0; i < data.length(); i++) {
            char index = (char) (data.charAt(i) - key);
            Main.output.append(index);
        }
    }
}

class Shift extends Crpyt {

    StringBuilder alpha = new StringBuilder("abcdefghijklmnopqrstuvwxyz");

    @Override
    public void encrypt(String data, int key) {
        for (int i = 0; i < data.length(); i++) {
            boolean upper = Character.isUpperCase(data.charAt(i));
//            int index = alpha.indexOf(String.valueOf(data.charAt(i)));
            char letter;
            if (upper) {
                letter = Character.toLowerCase(data.charAt(i));
            } else {
                letter = data.charAt(i);
            }
            int index = alpha.indexOf(String.valueOf(letter));

            if (String.valueOf(letter).equals(" ")) {
                Main.output.append(" ");
                continue;
            } else if (index == -1) {
                Main.output.append(data.charAt(i));
                continue;
            }
            int position = index + key;
            while (position > 25) {
                position = position - 25 - 1;
            }
            char c = alpha.charAt(position);
            if (upper) {
                Main.output.append(Character.toUpperCase(c));
            } else {
                Main.output.append(c);
            }
        }
    }

    @Override
    public void decrypt(String data, int key) {
        for (int i = 0; i < data.length(); i++) {
            boolean upper = Character.isUpperCase(data.charAt(i));
//            int index = alpha.indexOf(String.valueOf(data.charAt(i)));
            char letter;
            if (upper) {
                letter = Character.toLowerCase(data.charAt(i));
            } else {
                letter = data.charAt(i);
            }
            int index = alpha.indexOf(String.valueOf(letter));

            if (String.valueOf(letter).equals(" ")) {
                Main.output.append(" ");
                continue;
            } else if (index == -1) {
                Main.output.append(data.charAt(i));
                continue;
            }
            int position = index - key;
            while (position < 0 ) {
                position = 25 - Math.abs(position) + 1;
            }
            char c = alpha.charAt(position);
            if (upper) {
                Main.output.append(Character.toUpperCase(c));
            } else {
                Main.output.append(c);
            }
        }

    }
}

public class Main {
    public static StringBuilder output = new StringBuilder();


    public static void main(String[] args) {
        String action = args[List.of(args).indexOf("-mode") + 1];
        String inFile = args[List.of(args).indexOf("-in") + 1];
        String outFile = args[List.of(args).indexOf("-out") + 1];
        String data = args[List.of(args).indexOf("-data") + 1];
        String type = args[List.of(args).indexOf("-alg") + 1];

        int key = Integer.parseInt(args[List.of(args).indexOf("-key") + 1]);

        if (List.of(args).contains("-in")) {
            try {
                data = readFileAsString(inFile);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            data = args[List.of(args).indexOf("-data") + 1];
        }

        Crpyt crpyt;
        if ("unicode".equals(type)) {
            crpyt = new Unicode();
            crpyt.act(action, data, key);
        } else {
            crpyt = new Shift();
            crpyt.act(action, data, key);
        }


        if (List.of(args).contains("-out")) {
            try (FileWriter writer = new FileWriter(outFile)) {
                writer.write(output.toString());
            } catch (IOException e) {
                System.out.println("Error" + e.getMessage());
            }
        } else {
            System.out.println(output);
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}