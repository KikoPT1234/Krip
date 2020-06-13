package pt.kiko.krip.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;
import java.util.Collections;

public class OnlinePlayerObj extends PlayerObj {

	public OnlinePlayerObj(Player player, Context context) {
		super(player, context);

		value.put("world", new WorldObj(player.getWorld(), context));

		value.put("chat", new BuiltInFunctionValue("chat", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> message = context.symbolTable.get("message");

				if (!(message instanceof StringValue))
					return invalidType(message, context);

				player.chat(message.getValueString());

				return result.success(new NullValue(context));
			}
		});

		value.put("send", new BuiltInFunctionValue("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> message = context.symbolTable.get("message");

				if (!(message instanceof StringValue)) return invalidType(message, context);

				player.sendMessage(message.getValueString());
				return result.success(new NullValue(context));
			}
		});

		value.put("hasPermission", new BuiltInFunctionValue("hasPermission", Collections.singletonList("permission"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> permission = context.symbolTable.get("permission");

				if (!(permission instanceof StringValue))
					return invalidType(permission, context);

				return result.success(new BooleanValue(player.hasPermission(permission.getValueString()), context));
			}
		});

		value.put("setDisplayName", new BuiltInFunctionValue("setDisplayName", Collections.singletonList("name"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> name = context.symbolTable.get("name");

				if (!(name instanceof StringValue))
					return invalidType(name, context);

				player.setDisplayName(name.getValueString());

				return result.success(new NullValue(context));
			}
		});

		value.put("kick", new BuiltInFunctionValue("kick", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof StringValue || reason instanceof NullValue))
					return invalidType(reason, context);

				player.kickPlayer(reason.getValueString());
				return result.success(new NullValue(context));
			}
		});

		value.put("kill", new BuiltInFunctionValue("kill", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				player.damage(10000);
				return new RuntimeResult().success(new NullValue(context));
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

				Bukkit.getScheduler().runTask(Krip.plugin, () -> effect.apply(player));

				return result.success(new NullValue(context));
			}
		});
	}

}
