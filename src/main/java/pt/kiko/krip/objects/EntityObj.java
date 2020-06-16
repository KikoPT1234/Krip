package pt.kiko.krip.objects;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;

import java.util.Collections;
import java.util.HashMap;

public class EntityObj extends ObjectValue {

	Entity entity;

	public EntityObj(@NotNull Entity entity, Context context) {
		super(new HashMap<>(), context);

		this.entity = entity;

		value.put("type", new StringValue(entity.getType().toString(), context));

		value.put("getLocation", new BuiltInFunctionValue("getLocation", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new LocationObj(entity.getLocation(), context));
			}
		});

		value.put("getWorld", new BuiltInFunctionValue("getWorld", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new WorldObj(entity.getWorld(), context));
			}
		});
	}

}
