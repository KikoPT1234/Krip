package pt.kiko.krip.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;
import java.util.Objects;

public class PlayerMoveEvt extends KripEvent {

    static {
        Krip.registerEvent(new PlayerMoveEvt());
    }

    public PlayerMoveEvt() {
        super("PlayerMoveEvent", PlayerMoveEvent.class);
    }

	@Override
	protected KripObject getEvent(Event event) {
		assert event instanceof PlayerMoveEvent;
		PlayerMoveEvent evt = (PlayerMoveEvent) event;

		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("to", new LocationObj(Objects.requireNonNull(evt.getTo()), Krip.context));
		eventObj.set("from", new LocationObj(evt.getFrom(), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(evt.getPlayer(), Krip.context));
		return eventObj;
	}
}
