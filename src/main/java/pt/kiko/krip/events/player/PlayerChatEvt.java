package pt.kiko.krip.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.MessageObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerChatEvt extends KripEvent {

	static {
		Krip.registerEvent(new PlayerChatEvt());
	}

	public PlayerChatEvt() {
		super("PlayerChatEvent", AsyncPlayerChatEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof AsyncPlayerChatEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
		eventObj.set("message", new MessageObj(((AsyncPlayerChatEvent) event), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(((AsyncPlayerChatEvent) event).getPlayer(), Krip.context));
		return eventObj;
	}
}
