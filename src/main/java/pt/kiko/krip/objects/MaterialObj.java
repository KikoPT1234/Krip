package pt.kiko.krip.objects;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.HashMap;

public class MaterialObj extends KripObject {

	public Material material;

	public MaterialObj(@NotNull Material material, Context context) {
		super(new HashMap<>(), context);

		this.material = material;

		value.put("name", new KripString(material.toString(), context));
		if (material.isBlock()) value.put("hardness", new KripNumber(material.getHardness(), context));
		value.put("maxStackSize", new KripNumber(material.getMaxStackSize(), context));
		value.put("maxDurability", new KripNumber(material.getMaxDurability(), context));
		if (material.isBlock()) value.put("blastResistance", new KripNumber(material.getBlastResistance(), context));

		value.put("isAir", new KripJavaFunction("isAir", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isAir(), context));
			}
		});

		value.put("isBlock", new KripJavaFunction("isBlock", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isBlock(), context));
			}
		});

		value.put("isBurnable", new KripJavaFunction("isBurnable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isBurnable(), context));
			}
		});

		value.put("isEdible", new KripJavaFunction("isEdible", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isEdible(), context));
			}
		});

		value.put("isFlammable", new KripJavaFunction("isFlammable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isFlammable(), context));
			}
		});

		value.put("isFuel", new KripJavaFunction("isFuel", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isFuel(), context));
			}
		});

		value.put("isInteractable", new KripJavaFunction("isInteractable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isInteractable(), context));
			}
		});

		value.put("isOccluding", new KripJavaFunction("isOccluding", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isOccluding(), context));
			}
		});

		value.put("isItem", new KripJavaFunction("isItem", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isItem(), context));
			}
		});

		value.put("isSolid", new KripJavaFunction("isSolid", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isSolid(), context));
			}
		});

		value.put("isRecord", new KripJavaFunction("isRecord", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(material.isRecord(), context));
			}
		});
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof MaterialObj) {
			return new RuntimeResult().success(new KripBoolean(material == ((MaterialObj) other).material || material.equals(((MaterialObj) other).material), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

}
