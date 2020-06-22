package pt.kiko.krip.variables.functions;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;
import pt.kiko.krip.objects.material.ItemStackObj;
import pt.kiko.krip.objects.material.MaterialObj;

import java.util.Arrays;

public class ItemStackFunc extends KripJavaFunction {

	static {
		Krip.registerValue("ItemStack", new ItemStackFunc());
	}

	public ItemStackFunc() {
		super("ItemStack", Arrays.asList("material", "amount"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> material = context.symbolTable.get("material");
		KripValue<?> amount = context.symbolTable.get("amount");

		if (!(material instanceof KripString || material instanceof MaterialObj)) return invalidType(material, context);
		if (!(amount instanceof KripNumber)) return invalidType(amount, context);
		if (!((KripNumber) amount).isWhole())
			return result.failure(new RuntimeError(amount.startPosition, amount.endPosition, "Amount must be whole", context));

		ItemStack itemStack;

		if (material instanceof KripString) {
			Material materialToReturn = Material.getMaterial(material.getValueString());

			if (materialToReturn == null)
				return result.failure(new RuntimeError(material.startPosition, material.endPosition, "Invalid material", context));

			itemStack = new ItemStack(materialToReturn, (int) (double) ((KripNumber) amount).getValue());
		} else
			itemStack = new ItemStack(((MaterialObj) material).material, (int) (double) ((KripNumber) amount).getValue());

		return result.success(new ItemStackObj(itemStack, context));
	}
}
