package pt.kiko.krip.lang;

import com.sun.istack.internal.NotNull;

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

    public void advance(@NotNull char currentChar) {

        index++;
        col++;

        if (currentChar == '\n') {
            col = 0;
            line++;
        }
    }

    public Position copy() {
        return new Position(col, line, index, fileName, fileText);
    }

}
