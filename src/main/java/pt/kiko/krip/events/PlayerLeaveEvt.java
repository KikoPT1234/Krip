package pt.kiko.krip.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.EventInfo;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.PlayerObj;

import java.util.HashMap;

public class PlayerLeaveEvt extends EventInfo {

	static {
		Krip.registerEvent(new PlayerLeaveEvt());
	}

	public PlayerLeaveEvt() {
		super("playerLeave", PlayerQuitEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof PlayerQuitEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
		eventObj.set("player", new PlayerObj(((PlayerQuitEvent) event).getPlayer(), Krip.context));
		return eventObj;
	}
}
