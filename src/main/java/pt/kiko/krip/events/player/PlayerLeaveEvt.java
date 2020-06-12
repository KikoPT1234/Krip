package pt.kiko.krip.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerLeaveEvt extends KripEvent {

	static {
		Krip.registerEvent(new PlayerLeaveEvt());
	}

	public PlayerLeaveEvt() {
		super("PlayerLeaveEvent", PlayerQuitEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof PlayerQuitEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(((PlayerQuitEvent) event).getPlayer(), Krip.context));
		return eventObj;
	}
}
