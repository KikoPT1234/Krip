package pt.kiko.krip.lang;

import com.sun.istack.internal.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Position {

    public int col;
    public int line;
    public int index;
    public String fileName;
    public String fileText;

    public Position(int col, int line, int index, String fileName, String fileText) {
        this.col = col;
        this.line = line;
        this.index = index;
        this.fileName = fileName;
        this.fileText = fileText;
    }

    public Position advance(@NotNull char currentChar) {

        index++;
        col++;

        Pattern pattern = Pattern.compile("[;\\n]");
        Matcher matcher = pattern.matcher(String.valueOf(currentChar));
        if (matcher.matches()) {
            col = 0;
            line++;
        }

        return this;
    }

    public Position copy() {
        return new Position(col, line, index, fileName, fileText);
    }

}
