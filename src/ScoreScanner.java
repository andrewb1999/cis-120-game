import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ScoreScanner implements Iterator<String> {
    private Reader r;
    private int c;
    private boolean isName;

    public ScoreScanner(Reader initR) {
        r = initR;
        c = 0;
        isName = true;
        skipNonLetters();
    }

    private void skipNonLetters() {
        try {
            c = r.read(); // returns -1 at the end of the file
            while ((!isValidCharacter(c) || c == (int) ' ') && c != -1) {
                c = r.read();
            }
        } catch (IOException e) {
            c = -1; // use -1 for other IOExceptions
        }
    }

    public static boolean isValidCharacter(int c) {
        return c != 10 && (Character.isLetter(c) || Character.isDigit(c) || c == (int) '\"' || c == (int) ' ');
    }

    public static boolean isWord(String s) {
        if (s == null || s.length() == 0)
            return false;

        for (int i = 0; i < s.length(); i++) {
            if (!isValidCharacter(s.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public boolean hasNext() {
        return (c != -1);
    }

    public String next() {
        if (c == -1) throw new NoSuchElementException();

        StringWriter buf = new StringWriter();
        try {
            if (isName) {

                while (c != (int) '\"') {
                    System.out.println("Here1");
                    c = r.read();
                }

                c = r.read();
                while (isValidCharacter(c)) {
                    buf.write(c);
                    c = r.read();
                    if (c == (int) '\"') {
                        break;
                    }

                }
            } else {
                while (Character.isDigit(c)) {
                    buf.write(c);
                    c = r.read();
                }
            }
        } catch (IOException e) {
            throw new NoSuchElementException();
        }

        isName = !isName;
        skipNonLetters();
        return buf.toString();
    }

    /**
     * Unsupported by this iterator.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}