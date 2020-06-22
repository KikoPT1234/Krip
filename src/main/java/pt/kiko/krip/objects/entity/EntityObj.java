package pt.kiko.krip.objects.entity;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.WorldObj;

import java.util.Collections;
import java.util.HashMap;

public class EntityObj extends KripObject {

	Entity entity;

	public EntityObj(@NotNull Entity entity, Context context) {
		super(new HashMap<>(), context);

		this.entity = entity;

		value.put("type", new KripString(entity.getType().toString(), context));

		value.put("getLocation", new KripJavaFunction("getLocation", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new LocationObj(entity.getLocation(), context));
			}
		});

		value.put("getWorld", new KripJavaFunction("getWorld", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new WorldObj(entity.getWorld(), context));
			}
		});
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof EntityObj) {
			return new RuntimeResult().success(new KripBoolean(entity == ((EntityObj) other).entity || entity.equals(((EntityObj) other).entity), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

}
