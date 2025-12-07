
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static final int PARTS_OF_BODY = 6;
    public static final int START_GAME = 1;
    public static final int QUIT_GAME = 2;

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        boolean notQuit = true;
        while (notQuit) {
            printMenu();
            int choose = inputForMenuOption();

            switch (choose) {
                case START_GAME:
                    startRound();
                    break;

                case QUIT_GAME:
                    notQuit = false;
                    break;
            }
        }
    }

    public static void printMenu() {
        System.out.println("1. Начать игру");
        System.out.println("2. Выйти из игры");
    }

    public static int inputForMenuOption() {
        int result = 0;
        if (scanner.hasNextInt()) {
            result = scanner.nextInt();
            scanner.nextLine();
            if (result < 0 || result >= 2) {
                System.out.println("Можно выбрать 1 или 2 пункт!");
            }
        } else {
            String string = scanner.next();
            System.out.println("Данное выражение " + string
                    + " не подходит можно вводить только целые числа от 1 до 2!");
        }

        return result;
    }

    public static void startRound() {
        String randomWord = chooseRandomWord();
        gameLoop(randomWord);
    }

    public static String chooseRandomWord() {
        return "Машина".toLowerCase();
    }

    public static void gameLoop(String randomWord) {
        Set<String> mistakeLetters = new HashSet<>();
        Set<String> rightLetters = new HashSet<>();
        Set<String> allLetters = new HashSet<>();

        String wordForOutput = updateOutputWord(allLetters, randomWord);
        printStep(wordForOutput, mistakeLetters, " ");

        while (mistakeLetters.size() < PARTS_OF_BODY) {
            String letter = inputLetter(allLetters);
            allLetters.add(letter);

            if (letterInWord(randomWord, letter)) {
                rightLetters.add(letter);
                wordForOutput = updateOutputWord(rightLetters, randomWord);
            } else {
                mistakeLetters.add(letter);
            }

            if (Objects.equals(wordForOutput, randomWord)) {
                printStep(wordForOutput, mistakeLetters, letter);
                System.out.println("Вы победили!");
                break;
            }

            printStep(wordForOutput, mistakeLetters, letter);
        }

        if (mistakeLetters.size() == PARTS_OF_BODY) {
            System.out.println("Вы проиграли!");
        }
    }

    public static boolean letterInWord(String randomWord, String letter) {
        randomWord = randomWord.toLowerCase();
        letter = letter.toLowerCase();
        for (int i = 0; i < randomWord.length(); i++) {
            if (letter.charAt(0) == randomWord.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    public static String updateOutputWord(Set<String> rightLetters, String randomWord) {
        if (rightLetters.isEmpty()) {
            return "_".repeat(randomWord.length());
        }

        List<Character> lettersOfRandomWord = new ArrayList<>();
        for (int i = 0; i < randomWord.length(); i++) {
            lettersOfRandomWord.add(randomWord.toLowerCase().charAt(i));
        }

        for (int i = 0; i < lettersOfRandomWord.size(); i++) {
            if (!rightLetters.contains(Character.toString(lettersOfRandomWord.get(i)))) {
                lettersOfRandomWord.set(i, '_');
            }
        }

        String result = "";
        for (Character letter : lettersOfRandomWord) {
            result += letter;
        }

        return result;
    }

    public static String inputLetter(Set<String> allLetters) {
        String letter = "";
        do {
            System.out.println("Введите букву: ");
            letter = scanner.nextLine();

            if (letter.isBlank()) {
                System.out.println("Отсутствует буква!");
                continue;
            }

            if (!Character.isLetter(letter.charAt(0)) || letter.length() != 1) {
                System.out.println("Введите одну букву кириллицей!");
                continue;
            }

            if (allLetters.contains(letter.toLowerCase())) {
                System.out.println("Такая буква уже есть!");
            } else {
                break;
            }

        } while (true);

        return letter.toLowerCase();
    }

    public static void printStep(String wordForOutput, Set<String> mistakeLetters, String letter) {
        String[] partsBodyBegin = new String[]{" ", " ", " ", " ", " ", " "};
        String[] partsBodyEnd   = new String[]{"O", "|", "\\", "/", "/", "\\"};

        int countError = mistakeLetters.size();
        for (int i = 0; i < countError; i++) {
            partsBodyBegin[i] = partsBodyEnd[i];
        }

        String head = "  " + partsBodyBegin[0];
        String arms = " " + partsBodyBegin[2] + partsBodyBegin[1] + partsBodyBegin[3];
        String tail = "  " + partsBodyBegin[1];
        String legs = " " + partsBodyBegin[4] + " " + partsBodyBegin[5];

        String errorMessage = "";
        for (String mistakeLetter : mistakeLetters) {
            errorMessage += mistakeLetter + ", ";
        }

        System.out.printf("""
                    |-----|
                  %s     |     Слово: %s
                  %s    |     Ошибки (%d): %s
                  %s     |     Буква: %s
                  %s    |
                          |
                |---------|
                
                """, head, wordForOutput, arms, countError, errorMessage, tail, letter, legs);
    }
}
