package pt.kiko.krip.objects;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;

import java.util.HashMap;

public class MessageObj extends ObjectValue {

	public MessageObj(@NotNull AsyncPlayerChatEvent event, Context context) {
		super(new HashMap<>(), context);
		value.put("player", new OnlinePlayerObj(event.getPlayer(), context));
		value.put("content", new StringValue(event.getMessage(), context));
	}

}
