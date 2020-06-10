package pt.kiko.krip.lang;

/**
 * The Context represents a "scope" in the code, it keeps the name, parent scope and the symbol table.
 */
public class Context {

	public SymbolTable symbolTable = new SymbolTable();
	public String displayName;
	public Context parent;
	public Position parentEntryPosition;

	public Context(String displayName) {
		this.displayName = displayName;
	}

	public Context(String displayName, Context parent) {
		this.displayName = displayName;
		this.parent = parent;
	}

	public Context(String displayName, Context parent, Position parentEntryPosition) {
		this.displayName = displayName;
		this.parent = parent;
		this.parentEntryPosition = parentEntryPosition;
	}

}
