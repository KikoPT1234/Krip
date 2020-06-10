package pt.kiko.krip.objects;

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

public class PlayerObj extends ObjectValue {

	public OfflinePlayer player;

	public PlayerObj(@NotNull OfflinePlayer player, Context context) {
		super(new HashMap<>(), context);
		this.player = player;
		value.put("name", new StringValue(player.getName(), context));
		value.put("uuid", new StringValue(player.getUniqueId().toString(), context));

		value.put("ban", new BuiltInFunctionValue("ban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof StringValue || reason instanceof NullValue))
					return invalidType(reason, context);

				BanEntry entry = Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getUniqueId().toString(), reason.getValueString(), null, null);
				assert entry != null;
				if (player.isOnline()) Objects.requireNonNull(player.getPlayer()).kickPlayer(entry.getReason());
				return result.success(new NullValue(context.parent));
			}
		});

		value.put("unban", new BuiltInFunctionValue("unban", Collections.singletonList("reason"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> reason = context.symbolTable.get("reason");

				if (!(reason instanceof StringValue || reason instanceof NullValue))
					return invalidType(reason, context);

				Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(player.getUniqueId().toString());
				return result.success(new NullValue(context.parent));
			}
		});
	}
}
