package pt.kiko.krip.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.EventInfo;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.MessageObj;
import pt.kiko.krip.objects.PlayerObj;

import java.util.HashMap;

public class PlayerChatEvt extends EventInfo {

	static {
		Krip.registerEvent(new PlayerChatEvt());
	}

	public PlayerChatEvt() {
		super("playerChat", AsyncPlayerChatEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof AsyncPlayerChatEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
		eventObj.set("message", new MessageObj(((AsyncPlayerChatEvent) event), Krip.context));
		eventObj.set("player", new PlayerObj(((AsyncPlayerChatEvent) event).getPlayer(), Krip.context));
		return eventObj;
	}
}
