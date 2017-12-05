import java.io.*;
import java.util.*;

public class HighScores {
    private static final int NUM_HIGH_SCORES = 5;
    private static final int MAX_NAME_LENGTH = 20;
    private static final String FILE_NAME = "files/highScores.txt";
    private Map<Integer, String> scores;

    HighScores() throws IOException {
        scores = new TreeMap<>();

        FileReader fileReader = new FileReader(FILE_NAME);
        Reader reader = new BufferedReader(fileReader);
        WordScanner in = new WordScanner(reader);
        readScores(in);
        reader.close();

    }

    public static boolean isValidName(String name) {
        return name.length() < MAX_NAME_LENGTH && WordScanner.isWord(name);
    }

    private void readScores(WordScanner in) throws NoSuchElementException, NumberFormatException, IOException {

        while(in.hasNext()) {
            String w = in.next();
            int i = Integer.parseInt(in.next());
            addScore(w, i);
        }
    }

    public void addScore(String playerName, int score) throws IOException {
        Set<Integer> keySet = scores.keySet();

        if (keySet.size() < NUM_HIGH_SCORES) {
            scores.put(score, playerName);
        } else {
            int minValue = Integer.MAX_VALUE;
            for (Integer s : keySet) {
                if (s < minValue) {
                    minValue = s;
                }
            }

            if (keySet.contains(score) && score == minValue) {
                scores.put(score, playerName);
            } else if (score > minValue) {
                scores.put(score, playerName);
                scores.remove(minValue);
            }
        }

        Writer out = new BufferedWriter(new FileWriter(FILE_NAME));
        writeScores(out);
        out.close();
    }

    public boolean isHighScore(int score) {
        Set<Integer> keySet = scores.keySet();
        int minValue = Integer.MAX_VALUE;
        for (Integer s : keySet) {
            if (s < minValue) {
                minValue = s;
            }
        }

        return (keySet.contains(score) && score == minValue) || score > minValue;
    }

    private void writeScores(Writer out) throws IOException {
        Set<Integer> keySet = scores.keySet();
        List<Integer> scoreList = new ArrayList<>(keySet);
        Collections.sort(scoreList, Collections.reverseOrder());
        for (Integer s : scoreList) {
            out.write(scores.get(s) + " " + s + "\n");
        }
    }

    public void printScores() {
        Set<Integer> keySet = scores.keySet();
        List<Integer> scoreList = new ArrayList<>(keySet);
        Collections.sort(scoreList, Collections.reverseOrder());
        int i = 1;
        for (Integer s : scoreList) {
            System.out.println(i + ". "+ scores.get(s) + ": " + s);
            i++;
        }
    }
}
