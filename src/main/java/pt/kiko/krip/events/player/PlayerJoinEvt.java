package pt.kiko.krip.events.player;

import org.bukkit.event.player.PlayerJoinEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerJoinEvt extends KripEvent<PlayerJoinEvent> {

	static {
		Krip.registerEvent(new PlayerJoinEvt());
	}

	public PlayerJoinEvt() {
		super("PlayerJoinEvent", PlayerJoinEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerJoinEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		return eventObj;
	}
}
