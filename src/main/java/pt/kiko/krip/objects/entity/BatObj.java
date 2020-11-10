package pt.kiko.krip.objects.entity;

import org.bukkit.entity.Bat;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripBoolean;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class BatObj extends LivingEntityObj {

	public BatObj(Bat bat, Context context) {
		super(bat, context);

		value.put("isAwake", new KripJavaFunction("isAwake", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(bat.isAwake(), context));
			}
		});

		value.put("setAwake", new KripJavaFunction("setAwake", Collections.singletonList("awake"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> awake = context.symbolTable.get("awake");

				if (!(awake instanceof KripBoolean)) return invalidType(awake, context);

				bat.setAwake(((KripBoolean) awake).getValue());

				return result.success(new KripNull(context));
			}
		});
	}

}
