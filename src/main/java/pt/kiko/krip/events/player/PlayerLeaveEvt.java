package pt.kiko.krip.events.player;

import org.bukkit.event.player.PlayerQuitEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerLeaveEvt extends KripEvent<PlayerQuitEvent> {

	static {
		Krip.registerEvent(new PlayerLeaveEvt());
	}

	public PlayerLeaveEvt() {
		super("PlayerLeaveEvent", PlayerQuitEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerQuitEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		return eventObj;
	}
}
