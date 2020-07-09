package pt.kiko.krip.objects.player;

import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class OfflinePlayerObj extends KripObject {

	public OfflinePlayer player;

	public OfflinePlayerObj(@NotNull OfflinePlayer player, Context context) {
		super(new HashMap<>(), context);
		this.player = player;
		value.put("name", new KripString(player.getName(), context));
		value.put("uuid", new KripString(player.getUniqueId().toString(), context));

		value.put("hasPlayedBefore", new KripJavaFunction("hasPlayedBefore", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripBoolean(player.hasPlayedBefore(), context));
			}
		});

		value.put("ban", new KripJavaFunction("ban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof KripString || reason instanceof KripNull))
					return invalidType(reason, context);

				BanEntry entry = Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getUniqueId().toString(), reason.getValueString(), null, null);
				assert entry != null;
				if (player.isOnline()) Objects.requireNonNull(player.getPlayer()).kickPlayer(entry.getReason());
				return result.success(new KripNull(context));
			}
		});

		value.put("unban", new KripJavaFunction("unban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof KripString || reason instanceof KripNull))
					return invalidType(reason, context);

				Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(player.getUniqueId().toString());
				return result.success(new KripNull(context));
			}
		});
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof OfflinePlayerObj) {
			return new RuntimeResult().success(new KripBoolean(player == ((OfflinePlayerObj) other).player || player.equals(((OfflinePlayerObj) other).player), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}
}
