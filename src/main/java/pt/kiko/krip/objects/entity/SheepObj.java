package pt.kiko.krip.objects.entity;

import org.bukkit.entity.Sheep;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripBoolean;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class SheepObj extends LivingEntityObj {

	public SheepObj(Sheep sheep, Context context) {
		super(sheep, context);

		value.put("isSheared", new KripJavaFunction("isSheared", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(sheep.isSheared(), context));
			}
		});

		value.put("setSheared", new KripJavaFunction("setSheared", Collections.singletonList("sheared"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> sheared = context.symbolTable.get("sheared");

				if (!(sheared instanceof KripBoolean)) return invalidType(sheared, context);

				sheep.setSheared(((KripBoolean) sheared).getValue());

				return result.success(new KripNull(context));
			}
		});
	}

}
