import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class SlangWordDictionary {
    private Map<String, List<String>> _dictionary;

    SlangWordDictionary() {
        _dictionary = new HashMap<>();
    }

    SlangWordDictionary(String filename) {
        _dictionary = new HashMap<>();

        //init buffered reader
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //next first line
        String line = "";
        try {
            if (br != null) {
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read data each line and add to map
        while (true) {
            try {
                if (br != null) {
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
            String[] parts = line.split("`", 2);
            String key = parts[0];
            List<String> values = new ArrayList<>();
            if (parts.length >= 2) {
                String s = "";
                for (int i = 0; i < parts[1].length(); i++) {
                    if (parts[1].charAt(i) != '|') {
                        s += parts[1].charAt(i);
                    }
                    else {
                        values.add(s);
                    }
                }
                values.add(s);
            }

            _dictionary.put(key, values);
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> get_dictionary() {
        return _dictionary;
    }

    public boolean FindBySlangWord(String word) {
        List<String> values = _dictionary.get(word);
        System.out.println(values);
        SaveHistory(word);
        return values != null;
    }

    public boolean FindByDefinition(String keyword) {
        SaveHistory(keyword);
        final boolean[] isExist = {false};
        _dictionary.forEach((key, values) -> {
            for (String s: values) {
                if (s.toLowerCase(Locale.ROOT).contains(keyword)) {
                    System.out.println(key + " " + _dictionary.get(key));
                    isExist[0] = true;
                    break;
                }
            }
        });
        return isExist[0];
    }

    public void SaveHistory(String keyword) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream("history.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (dos != null) {
                dos.writeUTF(keyword);
                dos.flush();
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowHistory() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream("history.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (dis != null) {
                    String s = dis.readUTF();
                    System.out.println(s);
                }
            } catch (EOFException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddSlangWord(String key, String definition) {
        if (_dictionary.containsKey(key)) {
            System.out.println("This word was in dictionary.");
            System.out.println(key + " " + _dictionary.get(key));
            if (_dictionary.get(key).contains(definition)) {
                System.out.println("This mean was exist !!!");
                return;
            }
            System.out.println("Do you want to overwrite or duplicate?");
            System.out.println("1. Overwrite");
            System.out.println("2. Duplicate");
            System.out.print("Your answer: ");
            Scanner scanner = new Scanner(System.in);
            int choose = scanner.nextInt();
            switch (choose) {
                case 1 -> {
                    List<String> values = new ArrayList<>();
                    values.add(definition);
                    _dictionary.put(key, values);
                    System.out.println("This word is added");
                    System.out.println(key + " " + _dictionary.get(key));
                    Save("slang.txt");
                }
                case 2 -> {
                    List<String> values = _dictionary.get(key);
                    values.add(definition);
                    _dictionary.put(key, values);
                    System.out.println("This word is added");
                    System.out.println(key + " " + _dictionary.get(key));
                    Save("slang.txt");
                }
                default -> System.out.println("Error answer!!!");
            }
        }
        else {
            List<String> values = new ArrayList<>();
            values.add(definition);
            _dictionary.put(key, values);
            System.out.println("This word is added");
            System.out.println(key + " " + _dictionary.get(key));
            FileWriter fw = null;
            try {
                fw = new FileWriter("slang.txt", true);
                fw.write('\n' + key + '`' + definition);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save(String filename) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);
            for (Map.Entry<String, List<String>> entry: _dictionary.entrySet()) {
                fw.write(entry.getKey() + "`");
                List<String> values = entry.getValue();
                int i = 0;
                for (String s: values) {
                    if (i != 0) {
                        fw.write('|');
                    }
                    fw.write(s);
                    i ++;
                }
                fw.write('\n');
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void EditSlangWord(String key) {
        if (_dictionary.containsKey(key)) {
            List<String> values = _dictionary.get(key);
            System.out.println(values);
            System.out.print("Which mean do you want to edit? (0");
            for (int i = 1; i < values.size(); i++) {
                System.out.print(", " + i);
            }
            System.out.println(")");
            System.out.print("Your answer: ");
            Scanner scanner = new Scanner(System.in);
            int choose;
            choose = scanner.nextInt();
            scanner.nextLine();
            if (choose < 0 || choose >= values.size()) {
                System.out.println("Index error !!!");
            }
            else {
                System.out.print("Edit: ");
                String change = scanner.nextLine();
                if (values.contains(change)) {
                    System.out.println("This mean was exist !!!");
                }
                else {
                    values.set(choose, change);
                    System.out.println("This word is edited");
                    System.out.println(key + " " + _dictionary.get(key));
                    Save("slang.txt");
                }
            }
        }
        else {
            System.out.println("No exist this slang word");
        }
    }

    public void DeleteSlangWord(String key) {
        if (_dictionary.containsKey(key)) {
            List<String> values = _dictionary.get(key);
            System.out.println(key + " " + values);
            System.out.println("Are you sure to delete this word? (Y/N)");
            String choose;
            Scanner scanner = new Scanner(System.in);
            choose = scanner.nextLine();
            switch (choose.toLowerCase(Locale.ROOT)) {
                case "y" -> {
                    _dictionary.remove(key);
                    System.out.println("This word is deleted");
                    Save("slang.txt");
                }
                case "n" -> {

                }
            }
        }
        else {
            System.out.println("No exist this slang word");
        }
    }

    public void Reset() {
        _dictionary = (new SlangWordDictionary("backup.txt"))._dictionary;
        try {
            Files.copy(new File("backup.txt").toPath(), new File("slang.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String RandomSlangWord() {
        Set<String> keys = _dictionary.keySet();
        String[] keyArray = new String[keys.size()];
        keys.toArray(keyArray);
        int index = (int)Math.floor(Math.random()*(keyArray.length + 1));
        return keyArray[index];
    }

    public void QuizWithSlangWord() {
        String key = RandomSlangWord();

        String word1, word2, word3;
        do {
            word1 = RandomSlangWord();
            word2 = RandomSlangWord();
            word3 = RandomSlangWord();
        } while (key.equals(word1) || key.equals(word2) || key.equals(word3));
        String[] answers = {key, word1, word2, word3};
        Arrays.sort(answers);

        System.out.println("Question: What is " + key + " mean?");
        String trueAnswer = "";
        for (int i = 0; i < answers.length; i++) {
            System.out.println((char)(i + 'A') + ". " + _dictionary.get(answers[i]));
            if (answers[i].equals(key)) {
                trueAnswer = String.valueOf((char)(i + 'A'));
            }
        }
        System.out.print("\nAnswer: ");
        String answer;
        Scanner scanner = new Scanner(System.in);
        answer = scanner.nextLine();
        if (answer.toUpperCase(Locale.ROOT).equals(trueAnswer)) {
            System.out.println("Good job");
        }
        else {
            System.out.println("Wrong");
            System.out.println("The answer is " + trueAnswer);
        }
    }

    public void QuizWithDefinition() {
        String key = RandomSlangWord();

        String word1, word2, word3;
        do {
            word1 = RandomSlangWord();
            word2 = RandomSlangWord();
            word3 = RandomSlangWord();
        } while (key.equals(word1) || key.equals(word2) || key.equals(word3));
        String[] answers = {key, word1, word2, word3};
        Arrays.sort(answers);

        System.out.println("Question: What is slang word of " + _dictionary.get(key) + " ?");
        String trueAnswer = "";
        for (int i = 0; i < answers.length; i++) {
            System.out.println((char)(i + 'A') + ". " + answers[i]);
            if (answers[i].equals(key)) {
                trueAnswer = String.valueOf((char)(i + 'A'));
            }
        }
        System.out.print("\nAnswer: ");
        String answer;
        Scanner scanner = new Scanner(System.in);
        answer = scanner.nextLine();
        if (answer.toUpperCase(Locale.ROOT).equals(trueAnswer)) {
            System.out.println("Good job");
        }
        else {
            System.out.println("Wrong");
            System.out.println("The answer is " + trueAnswer);
        }
    }
}
