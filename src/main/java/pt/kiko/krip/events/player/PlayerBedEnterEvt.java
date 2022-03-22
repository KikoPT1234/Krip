package pt.kiko.krip.events.player;

import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerBedEnterEvt extends KripEvent<PlayerBedEnterEvent> {

	static {
		Krip.registerEvent(new PlayerBedEnterEvt());
	}

	public PlayerBedEnterEvt() {
		super("PlayerBedEnterEvent", PlayerBedEnterEvent.class);
	}

	@Override
	protected KripObject getEvent(@NotNull PlayerBedEnterEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("bed", new BlockObj(event.getBed(), Krip.context));
		eventObj.set("result", new KripString(event.getBedEnterResult().toString(), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));

		return eventObj;
	}
}
