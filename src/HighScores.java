import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class HighScores {
    private static final int NUM_HIGH_SCORES = 5;
    private static final int MAX_NAME_LENGTH = 20;
    private static final String FILE_NAME = "/highScores.txt";
    private Map<Integer, String> scores;

    public HighScores() throws IOException{
        scores = new TreeMap<>();
        InputStream is = Class.class.getResourceAsStream(FILE_NAME);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        ScoreScanner sc = new ScoreScanner(br);
        readScores(sc);
    }

    public static boolean isValidName(String name) {
        return name.length() < MAX_NAME_LENGTH && ScoreScanner.isWord(name);
    }

    private void readScores(ScoreScanner in) throws NoSuchElementException, NumberFormatException, IOException {
        while(in.hasNext()) {
            String w = in.next();
            int i = Integer.parseInt(in.next());
            addScore(w, i);
        }
    }

    public String[] getScores() {
        Set<Integer> keySet = scores.keySet();
        List<Integer> scoreList = new ArrayList<>(keySet);
        scoreList.sort(Collections.reverseOrder());
        String[] output = new String[NUM_HIGH_SCORES];

        int i = 0;
        for (Integer s : scoreList) {
            output[i] = (i + 1) + ". "+ scores.get(s) + ": " + s;
            i++;
        }

        int k = 0;
        for (String s : output) {
            if (s == null) {
                output[k] = "";
            }
            k++;
        }

        return output;
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

        URL resourceUrl = getClass().getResource(FILE_NAME);
        File file = null;
        try {
            file = new File(resourceUrl.toURI());
        } catch (URISyntaxException ignored) {
        }
        OutputStream output = new FileOutputStream(file);

        Writer out = new BufferedWriter(new OutputStreamWriter(output));
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

        if(keySet.size() == 0) {
            minValue = 0;
        }

        return keySet.size() < NUM_HIGH_SCORES || (keySet.contains(score) && score == minValue)
                || score > minValue;
    }

    private void writeScores(Writer out) throws IOException {
        Set<Integer> keySet = scores.keySet();
        List<Integer> scoreList = new ArrayList<>(keySet);
        Collections.sort(scoreList, Collections.reverseOrder());
        for (Integer s : scoreList) {
            out.write("\"" + scores.get(s) + "\" " + s + "\n");
        }
    }
}