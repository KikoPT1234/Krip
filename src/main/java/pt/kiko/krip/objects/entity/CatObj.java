package pt.kiko.krip.objects.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class CatObj extends LivingEntityObj {

	public CatObj(Cat cat, Context context) {
		super(cat, context);

		value.put("getType", new KripJavaFunction("getType", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripString(cat.getCatType().toString(), context));
			}
		});

		value.put("setType", new KripJavaFunction("setType", Collections.singletonList("type"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> type = context.symbolTable.get("type");

				if (!(type instanceof KripString)) return invalidType(type, context);

				Cat.Type catType;
				try {
					catType = Cat.Type.valueOf(type.getValueString());
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid cat type", context));
				}

				cat.setCatType(catType);

				return result.success(new KripNull(context));
			}
		});

		value.put("getCollarColor", new KripJavaFunction("getCollarColor", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripString(cat.getCollarColor().toString(), context));
			}
		});

		value.put("setCollarColor", new KripJavaFunction("setCollarColor", Collections.singletonList("color"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> color = context.symbolTable.get("color");

				if (!(color instanceof KripString)) return invalidType(color, context);

				DyeColor dyeColor;
				try {
					dyeColor = DyeColor.valueOf(color.getValueString());
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid color", context));
				}

				cat.setCollarColor(dyeColor);

				return result.success(new KripNull(context));
			}
		});
	}

}
