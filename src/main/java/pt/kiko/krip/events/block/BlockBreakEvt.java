package pt.kiko.krip.events.block;

import org.bukkit.event.block.BlockBreakEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class BlockBreakEvt extends KripEvent<BlockBreakEvent> {

	static {
		Krip.registerEvent(new BlockBreakEvt());
	}

	public BlockBreakEvt() {
		super("BlockBreakEvent", BlockBreakEvent.class);
	}

	@Override
	protected KripObject getEvent(BlockBreakEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(event.getPlayer(), Krip.context));
		eventObj.set("block", new BlockObj(event.getBlock(), Krip.context));
		return eventObj;
	}
}
