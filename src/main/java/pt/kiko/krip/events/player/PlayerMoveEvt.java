package pt.kiko.krip.events.player;

import org.bukkit.event.player.PlayerMoveEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;
import java.util.Objects;

public class PlayerMoveEvt extends KripEvent<PlayerMoveEvent> {

	static {
		Krip.registerEvent(new PlayerMoveEvt());
	}

	public PlayerMoveEvt() {
		super("PlayerMoveEvent", PlayerMoveEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerMoveEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("to", new LocationObj(Objects.requireNonNull(event.getTo()), Krip.context));
		eventObj.set("from", new LocationObj(event.getFrom(), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		return eventObj;
	}
}
