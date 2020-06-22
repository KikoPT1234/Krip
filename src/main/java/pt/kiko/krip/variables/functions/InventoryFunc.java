package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.inventory.InventoryObj;

import java.util.Arrays;

public class InventoryFunc extends KripJavaFunction {

	static {
		Krip.registerValue("Inventory", new InventoryFunc());
	}

	public InventoryFunc() {
		super("Inventory", Arrays.asList("typeOrSize", "title"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> typeOrSize = context.symbolTable.get("typeOrSize");
		KripValue<?> title = context.symbolTable.get("title");

		if (!(typeOrSize instanceof KripString || typeOrSize instanceof KripNumber))
			return invalidType(typeOrSize, context);
		if (typeOrSize instanceof KripNumber && !((KripNumber) typeOrSize).isWhole())
			return result.failure(new RuntimeError(typeOrSize.startPosition, typeOrSize.endPosition, "Size must be whole", context));
		if (!(title instanceof KripString || title instanceof KripNull)) return invalidType(title, context);

		Inventory inventory;

		if (typeOrSize instanceof KripString) {
			InventoryType inventoryType;
			try {
				inventoryType = InventoryType.valueOf(typeOrSize.getValueString());
			} catch (IllegalArgumentException e) {
				return result.failure(new RuntimeError(typeOrSize.startPosition, typeOrSize.endPosition, "Invalid inventory type", context));
			}

			if (title instanceof KripString)
				inventory = Bukkit.createInventory(null, inventoryType, title.getValueString());
			else inventory = Bukkit.createInventory(null, inventoryType);
		} else {
			if (title instanceof KripString)
				inventory = Bukkit.createInventory(null, (int) (double) ((KripNumber) typeOrSize).getValue() * 9, title.getValueString());
			else inventory = Bukkit.createInventory(null, (int) (double) ((KripNumber) typeOrSize).getValue() * 9);
		}

		return result.success(new InventoryObj(inventory, title instanceof KripString ? title.getValueString() : null, context));
	}
}
