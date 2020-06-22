package pt.kiko.krip.objects.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.LocationObj;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class LivingEntityObj extends EntityObj {

	public LivingEntityObj(LivingEntity entity, Context context) {
		super(entity, context);

		value.put("getEyeLocation", new KripJavaFunction("getEyeLocation", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();

				if (entity.isDead())
					return result.failure(new RuntimeError(startPosition, endPosition, "Entity is dead", context));
				return new RuntimeResult().success(new LocationObj(entity.getEyeLocation(), context));
			}
		});

		value.put("addEffect", new KripJavaFunction("addEffect", Arrays.asList("name", "duration", "amplifier", "particles"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> name = context.symbolTable.get("name");
				KripValue<?> duration = context.symbolTable.get("duration");
				KripValue<?> amplifier = context.symbolTable.get("amplifier");
				KripValue<?> particles = context.symbolTable.get("particles");

				if (!(name instanceof KripString)) return invalidType(name, context);
				if (!(duration instanceof KripNumber || duration instanceof KripNull))
					return invalidType(duration, context);
				if (!(amplifier instanceof KripNumber || amplifier instanceof KripNull))
					return invalidType(amplifier, context);
				if (!(particles instanceof KripBoolean || particles instanceof KripNull))
					return invalidType(particles, context);

				if (duration instanceof KripNumber && ((KripNumber) duration).getValue() != Math.floor(((KripNumber) duration).getValue()))
					return result.failure(new RuntimeError(duration.startPosition, duration.endPosition, "Number must be whole", context));
				if (amplifier instanceof KripNumber && ((KripNumber) amplifier).getValue() != Math.floor(((KripNumber) amplifier).getValue()))
					return result.failure(new RuntimeError(amplifier.startPosition, amplifier.endPosition, "Number must be whole", context));

				PotionEffectType type = PotionEffectType.getByName(name.getValueString().replace(' ', '_').toUpperCase());

				if (type == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid potion type", context));

				int potionDuration = duration instanceof KripNumber ? (int) (double) ((KripNumber) duration).getValue() : 600;
				int potionAmplifier = amplifier instanceof KripNumber ? (int) (double) ((KripNumber) amplifier).getValue() : 0;
				boolean potionParticles = particles instanceof KripBoolean ? ((KripBoolean) particles).getValue() : true;

				PotionEffect effect = new PotionEffect(type, potionDuration, potionAmplifier, false, potionParticles);

				Bukkit.getScheduler().runTask(Krip.plugin, () -> effect.apply(entity));

				return result.success(new KripNull(context));
			}
		});

		value.put("removeEffect", new KripJavaFunction("removeEffect", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> name = context.symbolTable.get("name");

				if (!(name instanceof KripString)) return invalidType(name, context);

				PotionEffectType type = PotionEffectType.getByName(name.getValueString().replace(' ', '_').toUpperCase());
				if (type == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid potion type", context));

				entity.removePotionEffect(type);

				return result.success(new KripNull(context));
			}
		});

		value.put("clearEffects", new KripJavaFunction("clearEffects", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				Collection<PotionEffect> effects = entity.getActivePotionEffects();

				effects.forEach(effect -> entity.removePotionEffect(effect.getType()));

				return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("setCollidable", new KripJavaFunction("setCollidable", Collections.singletonList("collidable"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> collidable = context.symbolTable.get("collidable");

				if (!(collidable instanceof KripBoolean)) return invalidType(collidable, context);

				entity.setCollidable(((KripBoolean) collidable).getValue());

				return result.success(new KripNull(context));
			}
		});

		value.put("setAI", new KripJavaFunction("setAI", Collections.singletonList("ai"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> ai = context.symbolTable.get("ai");

				if (!(ai instanceof KripBoolean)) return invalidType(ai, context);

				entity.setAI(((KripBoolean) ai).getValue());

				return result.success(new KripNull(context));
			}
		});

		value.put("damage", new KripJavaFunction("damage", Collections.singletonList("damage"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> damage = context.symbolTable.get("damage");

				if (!(damage instanceof KripNumber)) return invalidType(damage, context);

				entity.damage(((KripNumber) damage).getValue());

				return result.success(new KripNull(context));
			}
		});

		value.put("kill", new KripJavaFunction("kill", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				while (!entity.isDead()) {
					entity.damage(10000);
				}
				return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("isSwimming", new KripJavaFunction("isSwimming", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(entity.isSwimming(), context));
			}
		});

		value.put("isCollidable", new KripJavaFunction("isCollidable", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(entity.isCollidable(), context));
			}
		});

		value.put("isLeashed", new KripJavaFunction("isLeashed", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(entity.isLeashed(), context));
			}
		});

		value.put("isGliding", new KripJavaFunction("isGliding", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(entity.isGliding(), context));
			}
		});

		value.put("isRiptiding", new KripJavaFunction("isRiptiding", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(entity.isRiptiding(), context));
			}
		});

		value.put("isSleeping", new KripJavaFunction("isSleeping", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(entity.isSleeping(), context));
			}
		});
	}

}
