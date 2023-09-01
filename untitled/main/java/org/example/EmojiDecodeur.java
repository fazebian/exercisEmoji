package org.example;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EmojiDecodeur {

    public static void main(String[] args)   {

        Scanner scanner = new Scanner(System.in);
        String inputFileName = "";
        boolean isValidPath = false;

        while (!isValidPath) {
            System.out.print("Entrez le chemin du fichier  : ");
            //  src/main/ressources/texte.txt
            inputFileName = scanner.nextLine();

            if (isValidFilePath(inputFileName)) {
                isValidPath = true;
            } else {
                System.out.println("Chemin du fichier invalide. Veuillez reessayer.");
            }
        }

        try {
            String input = readFile(inputFileName);
            String decodedText = decodeEmojis(input);

            File inputFile = new File(inputFileName);
            String parentDirectory = inputFile.getParent();
            String outputFileName = parentDirectory + File.separator + "fichier_decode.txt";

            if (fileExists(outputFileName)) {
                System.out.print("Le fichier de sortie existe deja. Voulez-vous le remplacer ? (Oui/Non) : ");
                String response = scanner.nextLine();
                if (!response.equalsIgnoreCase("Oui")) {
                    System.out.println("Operation annulee. Le fichier de sortie n'a pas ete modifie.");
                    return;
                }
            }

            writeFile(outputFileName, decodedText);
            System.out.println("Emojis decodes avec succes et enregistres dans " + outputFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidFilePath(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String decodeEmojis(String input) {
        String regex = "[😀😂🥺🚀🐱]"; // TODO a remplacer une expression reguliere globale ?
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(input);

        var result = new StringBuilder();
        while (matcher.find()) {
            String emoji = matcher.group();
            String emojiName = getEmojiReplacement(emoji);
            matcher.appendReplacement(result, emojiName);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private static String getEmojiReplacement(String emoji) {
        return switch (emoji) {
            case "😀" -> ":smile:";
            case "😂" -> ":joy:";
            case "🥺" -> ":pleading_face:";
            case "🚀" -> ":rocket:";
            case "🐱" -> ":cat_face:";
            default -> "emoji";
        };
    }

    private static String readFile(String fileName) throws IOException {
        var content = new StringBuilder();
        try (var reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static void writeFile(String fileName, String content) throws IOException {
        try (var writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }
}
