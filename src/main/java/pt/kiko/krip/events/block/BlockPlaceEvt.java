package pt.kiko.krip.events.block;

import org.bukkit.event.block.BlockPlaceEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class BlockPlaceEvt extends KripEvent<BlockPlaceEvent> {

	static {
		Krip.registerEvent(new BlockPlaceEvt());
	}

	public BlockPlaceEvt() {
		super("BlockPlaceEvent", BlockPlaceEvent.class);
	}

	@Override
	protected KripObject getEvent(BlockPlaceEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		eventObj.set("block", new BlockObj(event.getBlockPlaced(), Krip.context));
		eventObj.set("placedAgainst", new BlockObj(event.getBlockAgainst(), Krip.context));
		return eventObj;
	}
}
