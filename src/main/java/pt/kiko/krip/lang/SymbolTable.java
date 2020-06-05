package pt.kiko.krip.lang;

import pt.kiko.krip.lang.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

	private boolean checking = false;
	public SymbolTable parent;
	public HashMap<String, Value> symbols = new HashMap<>();
	private ArrayList<String> constants = new ArrayList<>();

	public SymbolTable() {}

	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}

	public SymbolTable(SymbolTable parent, HashMap<String, Value> symbols, ArrayList<String> constants) {
		this.parent = parent;
		this.symbols= symbols;
		this.constants = constants;
	}

	public Value get(String name) {
		Value value = symbols.get(name);
		if (value == null && parent != null && !checking) {
			checking = true;
			value = parent.get(name);
			checking = false;
		}
		return value;
	}

	public boolean set(String name, Value value, Boolean isFinal) {
		if (isConstant(name)) return false;
		symbols.put(name, value);
		if (isFinal) constants.add(name);
		return true;
	}

	public void remove(String name) {
		if (isConstant(name)) constants.remove(name);
		symbols.remove(name);
	}

	public boolean setExisting(String name, Value value) {
		if (isConstantParents(name)) return false;
		if (symbols.containsKey(name)) {
			symbols.put(name, value);
			return true;
		} else if (parent != null) return parent.setExisting(name, value);
		else return false;
	}

	public boolean has(String name) {
		return symbols.containsKey(name);
	}

	public boolean isConstant(String name) {
		return constants.contains(name);
	}

	public boolean isConstantParents(String name) {
		boolean isConstant = isConstant(name);

		if (!isConstant && this.parent != null) return this.parent.isConstantParents(name);
		else return isConstant;
	}

	public SymbolTable copy() {
		return new SymbolTable(parent, symbols, constants);
	}

}
