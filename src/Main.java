import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SlangWordDictionary dictionary = new SlangWordDictionary("slang.txt");

        String choose;
        do {
            System.out.println("---------------------* Slang Word *--------------------");
            System.out.println("| 1. Find by slang word                               |");
            System.out.println("| 2. Find by definition                               |");
            System.out.println("| 3. Show history search                              |");
            System.out.println("| 4. Add a slang word                                 |");
            System.out.println("| 5. Edit a slang word                                |");
            System.out.println("| 6. Delete a slang word                              |");
            System.out.println("| 7. Reset dictionary                                 |");
            System.out.println("| 8. Random a slang word                              |");
            System.out.println("| 9. Quiz: Choose definition match with slang word    |");
            System.out.println("| 10. Quiz: Choose slang word match with definition   |");
            System.out.println("| 11. Exit                                            |");
            System.out.println("-------------------------------------------------------");
            System.out.print("Choose: ");
            Scanner scanner = new Scanner(System.in);
            choose = scanner.nextLine();
            switch (choose) {
                case "1" -> {
                    System.out.println("***** Find by slang word *****");
                    System.out.print("Enter slang word: ");
                    String keyword = scanner.nextLine();
                    if(!dictionary.FindBySlangWord(keyword)) {
                        System.out.println("404 Not Found");
                    }
                }
                case "2" -> {
                    System.out.println("***** Find by definition *****");
                    System.out.print("Enter definition: ");
                    String definition = scanner.nextLine();
                    if(!dictionary.FindByDefinition(definition)) {
                        System.out.println("404 Not Found");
                    }
                }
                case "3" -> {
                    System.out.println("***** Show history search *****");
                    dictionary.ShowHistory();
                }
                case "4" -> {
                    System.out.println("***** Add a slang word *****");
                    System.out.print("Enter slang word: ");
                    String key = scanner.nextLine();
                    System.out.print("Enter definition: ");
                    String definition = scanner.nextLine();
                    dictionary.AddSlangWord(key, definition);
                }
                case "5" -> {
                    System.out.println("***** Edit a slang word *****");
                    System.out.print("Enter slang word: ");
                    String key = scanner.nextLine();
                    dictionary.EditSlangWord(key);
                }
                case "6" -> {
                    System.out.println("***** Delete a slang word *****");
                    System.out.print("Enter slang word: ");
                    String key = scanner.nextLine();
                    dictionary.DeleteSlangWord(key);
                }
                case "7" -> {
                    System.out.println("***** Reset dictionary *****");
                    dictionary.Reset();
                }
                case "8" -> {
                    System.out.println("***** Random a slang word *****");
                    String key = dictionary.RandomSlangWord();
                    System.out.println(key + " " + dictionary.get_dictionary().get(key));
                }
                case "9" -> {
                    System.out.println("Quiz: Choose definition match with slang word");
                    dictionary.QuizWithSlangWord();
                }
                case "10" -> {
                    System.out.println("Quiz: Choose slang word match with definition");
                    dictionary.QuizWithDefinition();
                }
                case "11" -> {}
                default -> System.out.println("Program do not has this function!!!");
            }
            System.out.println("Press 'Enter' to continue...");
            scanner.nextLine();
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        } while (!choose.equals("11"));
    }
}

