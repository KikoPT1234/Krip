package pt.kiko.krip.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class LivingEntityObj extends EntityObj {

	public LivingEntityObj(LivingEntity entity, Context context) {
		super(entity, context);

		value.put("getEyeLocation", new BuiltInFunctionValue("getEyeLocation", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();

				if (entity.isDead())
					return result.failure(new RuntimeError(startPosition, endPosition, "Entity is dead", context));
				return new RuntimeResult().success(new LocationObj(entity.getEyeLocation(), context));
			}
		});

		value.put("addEffect", new BuiltInFunctionValue("addEffect", Arrays.asList("name", "duration", "amplifier", "particles"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");
				Value<?> duration = context.symbolTable.get("duration");
				Value<?> amplifier = context.symbolTable.get("amplifier");
				Value<?> particles = context.symbolTable.get("particles");

				if (!(name instanceof StringValue)) return invalidType(name, context);
				if (!(duration instanceof NumberValue || duration instanceof NullValue))
					return invalidType(duration, context);
				if (!(amplifier instanceof NumberValue || amplifier instanceof NullValue))
					return invalidType(amplifier, context);
				if (!(particles instanceof BooleanValue || particles instanceof NullValue))
					return invalidType(particles, context);

				if (duration instanceof NumberValue && ((NumberValue) duration).getValue() != Math.floor(((NumberValue) duration).getValue()))
					return result.failure(new RuntimeError(duration.startPosition, duration.endPosition, "Number must be whole", context));
				if (amplifier instanceof NumberValue && ((NumberValue) amplifier).getValue() != Math.floor(((NumberValue) amplifier).getValue()))
					return result.failure(new RuntimeError(amplifier.startPosition, amplifier.endPosition, "Number must be whole", context));

				PotionEffectType type = PotionEffectType.getByName(name.getValueString().replace(' ', '_').toUpperCase());

				if (type == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid potion type", context));

				int potionDuration = duration instanceof NumberValue ? (int) (double) ((NumberValue) duration).getValue() : 600;
				int potionAmplifier = amplifier instanceof NumberValue ? (int) (double) ((NumberValue) amplifier).getValue() : 0;
				boolean potionParticles = particles instanceof BooleanValue ? ((BooleanValue) particles).getValue() : true;

				PotionEffect effect = new PotionEffect(type, potionDuration, potionAmplifier, false, potionParticles);

				Bukkit.getScheduler().runTask(Krip.plugin, () -> effect.apply(entity));

				return result.success(new NullValue(context));
			}
		});

		value.put("removeEffect", new BuiltInFunctionValue("removeEffect", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue)) return invalidType(name, context);

				PotionEffectType type = PotionEffectType.getByName(name.getValueString().replace(' ', '_').toUpperCase());
				if (type == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid potion type", context));

				entity.removePotionEffect(type);

				return result.success(new NullValue(context));
			}
		});

		value.put("clearEffects", new BuiltInFunctionValue("clearEffects", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				Collection<PotionEffect> effects = entity.getActivePotionEffects();

				effects.forEach(effect -> entity.removePotionEffect(effect.getType()));

				return new RuntimeResult().success(new NullValue(context));
			}
		});

		value.put("setCollidable", new BuiltInFunctionValue("setCollidable", Collections.singletonList("collidable"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> collidable = context.symbolTable.get("collidable");

				if (!(collidable instanceof BooleanValue)) return invalidType(collidable, context);

				entity.setCollidable(((BooleanValue) collidable).getValue());

				return result.success(new NullValue(context));
			}
		});

		value.put("setAI", new BuiltInFunctionValue("setAI", Collections.singletonList("ai"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> ai = context.symbolTable.get("ai");

				if (!(ai instanceof BooleanValue)) return invalidType(ai, context);

				entity.setAI(((BooleanValue) ai).getValue());

				return result.success(new NullValue(context));
			}
		});

		value.put("damage", new BuiltInFunctionValue("damage", Collections.singletonList("damage"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> damage = context.symbolTable.get("damage");

				if (!(damage instanceof NumberValue)) return invalidType(damage, context);

				entity.damage(((NumberValue) damage).getValue());

				return result.success(new NullValue(context));
			}
		});

		value.put("kill", new BuiltInFunctionValue("kill", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				while (!entity.isDead()) {
					entity.damage(10000);
				}
				return new RuntimeResult().success(new NullValue(context));
			}
		});

		value.put("isSwimming", new BuiltInFunctionValue("isSwimming", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(entity.isSwimming(), context));
			}
		});

		value.put("isCollidable", new BuiltInFunctionValue("isCollidable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(entity.isCollidable(), context));
			}
		});

		value.put("isLeashed", new BuiltInFunctionValue("isLeashed", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(entity.isLeashed(), context));
			}
		});

		value.put("isGliding", new BuiltInFunctionValue("isGliding", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(entity.isGliding(), context));
			}
		});

		value.put("isRiptiding", new BuiltInFunctionValue("isRiptiding", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(entity.isRiptiding(), context));
			}
		});

		value.put("isSleeping", new BuiltInFunctionValue("isSleeping", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new BooleanValue(entity.isSleeping(), context));
			}
		});
	}

}
