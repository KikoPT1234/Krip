package pt.kiko.krip.lang;

import pt.kiko.krip.lang.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The SymbolTable is responsible for holding symbols (variables) during execution
 */
public class SymbolTable {

	/**
	 * The parent symbol table
	 */
	public SymbolTable parent;
	/**
	 * The map holding the symbols
	 */
	public HashMap<String, Value<?>> symbols = new HashMap<>();
	/**
	 * A list of strings containing the name of the constant variables
	 */
	public ArrayList<String> constants = new ArrayList<>();
	/**
	 * Whether the get function is running, to avoid stack overflowing
	 */
	private boolean checking = false;

	/**
	 * Normal constructor
	 */
	public SymbolTable() {
	}

	/**
	 * Constructor with parent
	 *
	 * @param parent The parent of this table
	 */
	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}

	/**
	 * Constructor with parent, position of parent and list holding constant names
	 *
	 * @param parent    The parent of this table
	 * @param symbols   The symbols
	 * @param constants The constants
	 */
	public SymbolTable(SymbolTable parent, HashMap<String, Value<?>> symbols, ArrayList<String> constants) {
		this.parent = parent;
		this.symbols = symbols;
		this.constants = constants;
	}

	/**
	 * Gets the symbol with the name of the provided string, if any
	 *
	 * @param name The name of the symbol
	 * @return The symbol if any, null otherwise
	 */
	public Value<?> get(String name) {
		Value<?> value = symbols.get(name);
		if (value == null && parent != null && !checking) {
			checking = true;
			value = parent.get(name);
			checking = false;
		}
		return value;
	}

	/**
	 * Sets a new symbol with the specified name to the specified value
	 *
	 * @param name    The name of the symbol
	 * @param value   The value of the symbol
	 * @param isFinal Whether the symbol is constant (initialized with the const keyword)
	 * @return true if successful, false otherwise
	 */
	public boolean set(String name, Value<?> value, Boolean isFinal) {
		if (isConstant(name)) return false;
		symbols.put(name, value);
		if (isFinal) constants.add(name);
		return true;
	}

	/**
	 * Removes a symbol from the table
	 *
	 * @param name The name of the symbol
	 */
	public void remove(String name) {
		if (isConstant(name)) constants.remove(name);
		symbols.remove(name);
	}

	/**
	 * Sets an existing symbol to the new value
	 *
	 * @param name  The name of the symbol
	 * @param value The value to set
	 * @return true if successful, false otherwise
	 */
	public boolean setExisting(String name, Value<?> value) {
		if (isConstantParents(name)) return false;
		if (symbols.containsKey(name)) {
			symbols.put(name, value);
			return true;
		} else if (parent != null) return parent.setExisting(name, value);
		else return false;
	}

	/**
	 * Checks whether a symbol exists
	 *
	 * @param name The name of the symbol
	 * @return Boolean of whether the symbol exists
	 */
	public boolean has(String name) {
		return symbols.containsKey(name);
	}

	/**
	 * Checks whether the symbol is constant
	 *
	 * @param name The name of the symbol
	 * @return Boolean of whether the symbol is constant
	 */
	public boolean isConstant(String name) {
		return constants.contains(name);
	}

	/**
	 * Checks whether the symbol is constant, including parents
	 *
	 * @param name The name of the symbol
	 * @return Boolean of whether the symbol is constant
	 */
	public boolean isConstantParents(String name) {
		boolean isConstant = isConstant(name);

		if (!isConstant && this.parent != null) return this.parent.isConstantParents(name);
		else return isConstant;
	}

	/**
	 * Returns a copy of this table
	 *
	 * @return The copy
	 */
	public SymbolTable copy() {
		return new SymbolTable(parent, symbols, constants);
	}

}
