package pt.kiko.krip.objects;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.HashMap;

public class BlockObj extends ObjectValue {

	public BlockObj(@NotNull Block block, Context context) {
		super(new HashMap<>(), context);

		value.put("location", new LocationObj(block.getLocation(), context));
		value.put("type", new StringValue(block.getType().toString(), context));
		value.put("destroy", new BuiltInFunctionValue("destroy", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				block.breakNaturally();
				return new RuntimeResult().success(new NullValue(context));
			}
		});

		value.put("setType", new BuiltInFunctionValue("setType", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> materialName = context.symbolTable.get("material");

				if (!(materialName instanceof StringValue)) return invalidType(materialName, context);

				Material material = Material.getMaterial(materialName.getValueString().replace(' ', '_').toUpperCase());

				if (material == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Material not found", context));

				block.setType(material);

				BlockObj.this.value.put("type", new StringValue(material.toString(), context));
				return result.success(new NullValue(context.parent));
			}
		});

		value.put("isEmpty", new BuiltInFunctionValue("isEmpty", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(block.isEmpty(), context.parent));
			}
		});

		value.put("isLiquid", new BuiltInFunctionValue("isLiquid", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(block.isLiquid(), context.parent));
			}
		});

		value.put("isPassable", new BuiltInFunctionValue("isPassable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(block.isPassable(), context.parent));
			}
		});
	}

}
