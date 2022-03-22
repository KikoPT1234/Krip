package pt.kiko.krip.events.player;

import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerInteractEvt extends KripEvent<PlayerInteractEvent> {

	static {
		Krip.registerEvent(new PlayerInteractEvt());
	}

	public PlayerInteractEvt() {
		super("PlayerInteractEvent", PlayerInteractEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerInteractEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("action", new KripString(event.getAction().toString(), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		Block block = event.getClickedBlock();
		if (block != null) eventObj.set("clickedBlock", new BlockObj(block, Krip.context));

		return eventObj;
	}
}
