package pt.kiko.krip.objects;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripBoolean;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.HashMap;

public class MessageObj extends KripObject {

	String content;

	public MessageObj(@NotNull AsyncPlayerChatEvent event, Context context) {
		super(new HashMap<>(), context);

		content = event.getMessage();

		value.put("player", new OnlinePlayerObj(event.getPlayer(), context));
		value.put("content", new KripString(event.getMessage(), context));
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof MessageObj) {
			return new RuntimeResult().success(new KripBoolean(content.equals(((MessageObj) other).content), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

}
