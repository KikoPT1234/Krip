package pt.kiko.krip.events.player;

import org.bukkit.event.player.PlayerBedLeaveEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerBedLeaveEvt extends KripEvent<PlayerBedLeaveEvent> {

	static {
		Krip.registerEvent(new PlayerBedLeaveEvt());
	}

	public PlayerBedLeaveEvt() {
		super("PlayerBedLeaveEvent", PlayerBedLeaveEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerBedLeaveEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("bed", new BlockObj(event.getBed(), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));

		return eventObj;
	}
}
