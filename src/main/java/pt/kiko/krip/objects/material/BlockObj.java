package pt.kiko.krip.objects.material;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.LocationObj;

import java.util.Collections;
import java.util.HashMap;

public class BlockObj extends KripObject {

	public Block block;

	public BlockObj(@NotNull Block block, Context context) {
		super(new HashMap<>(), context);

		this.block = block;

		value.put("location", new LocationObj(block.getLocation(), context));
		value.put("type", new KripString(block.getType().toString(), context));
		value.put("destroy", new KripJavaFunction("destroy", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				block.breakNaturally();
				return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("setType", new KripJavaFunction("setType", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> materialName = context.symbolTable.get("material");

				if (!(materialName instanceof KripString)) return invalidType(materialName, context);

				Material material = Material.getMaterial(materialName.getValueString().replace(' ', '_').toUpperCase());

				if (material == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Material not found", context));

				block.setType(material);

				BlockObj.this.value.put("type", new KripString(material.toString(), context));
				return result.success(new KripNull(context));
			}
		});

		value.put("isEmpty", new KripJavaFunction("isEmpty", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(block.isEmpty(), context));
			}
		});

		value.put("isLiquid", new KripJavaFunction("isLiquid", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(block.isLiquid(), context));
			}
		});

		value.put("isPassable", new KripJavaFunction("isPassable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(block.isPassable(), context));
			}
		});
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof BlockObj) {
			return new RuntimeResult().success(new KripBoolean(block == ((BlockObj) other).block || block.equals(((BlockObj) other).block), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}
}
