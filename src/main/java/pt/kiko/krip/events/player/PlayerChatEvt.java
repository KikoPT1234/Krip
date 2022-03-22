package pt.kiko.krip.events.player;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.MessageObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerChatEvt extends KripEvent<AsyncPlayerChatEvent> {

	static {
		Krip.registerEvent(new PlayerChatEvt());
	}

	public PlayerChatEvt() {
		super("PlayerChatEvent", AsyncPlayerChatEvent.class);
	}

	@Override
	protected KripObject getEvent(AsyncPlayerChatEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("message", new MessageObj(event, Krip.context));
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		return eventObj;
	}
}
