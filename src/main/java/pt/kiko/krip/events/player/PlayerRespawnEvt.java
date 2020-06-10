package pt.kiko.krip.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.PlayerObj;

import java.util.HashMap;

public class PlayerRespawnEvt extends KripEvent {

	static {
		Krip.registerEvent(new PlayerRespawnEvt());
	}

	public PlayerRespawnEvt() {
		super("PlayerRespawnEvent", PlayerRespawnEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof PlayerRespawnEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);

		eventObj.set("player", new PlayerObj(((PlayerRespawnEvent) event).getPlayer(), Krip.context));
		eventObj.set("location", new LocationObj(((PlayerRespawnEvent) event).getRespawnLocation(), Krip.context));

		return eventObj;
	}
}
