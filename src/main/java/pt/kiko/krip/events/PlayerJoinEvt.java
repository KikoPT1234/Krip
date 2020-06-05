package pt.kiko.krip.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.EventInfo;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.PlayerObj;

import java.util.HashMap;

public class PlayerJoinEvt extends EventInfo {

	static {
		Krip.registerEvent(new PlayerJoinEvt());
	}

	public PlayerJoinEvt() {
		super("PlayerJoinEvent", PlayerJoinEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof PlayerJoinEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
		eventObj.set("player", new PlayerObj(((PlayerJoinEvent) event).getPlayer(), Krip.context));
		return eventObj;
	}
}
