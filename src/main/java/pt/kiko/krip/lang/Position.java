package pt.kiko.krip.lang;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the position of a piece of code in the file
 */
public class Position {

	/**
	 * The column
	 */
	public int col;

	/**
	 * The line
	 */
	public int line;

	/**
	 * The index
	 */
	public int index;

	/**
	 * The file name
	 */
	public String fileName;

	/**
	 * The file text
	 */
	public String fileText;

	/**
	 * @param col      The column
	 * @param line     The line
	 * @param index    The index
	 * @param fileName The file name
	 * @param fileText The file text
	 */
	public Position(int col, int line, int index, String fileName, String fileText) {
		this.col = col;
		this.line = line;
		this.index = index;
		this.fileName = fileName;
		this.fileText = fileText;
	}

	/**
	 * Advances to the next column/line
	 *
	 * @param currentChar The current character, used to check if it should advance to the next line
	 */
	public void advance(@NotNull char currentChar) {

		index++;
		col++;

		if (currentChar == '\n') {
			col = 0;
			line++;
		}
	}

	/**
	 * @return A new instance of this class
	 */
	public Position copy() {
		return new Position(col, line, index, fileName, fileText);
	}

}
