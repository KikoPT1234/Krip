package pt.kiko.krip.events.block;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.BlockObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class BlockPlaceEvt extends KripEvent {

	static {
		Krip.registerEvent(new BlockPlaceEvt());
	}

	public BlockPlaceEvt() {
		super("BlockPlaceEvent", BlockPlaceEvent.class);
	}

	@Override
	protected KripObject getEvent(Event event) {
		assert event instanceof BlockPlaceEvent;
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(((BlockPlaceEvent) event).getPlayer(), Krip.context));
		eventObj.set("block", new BlockObj(((BlockPlaceEvent) event).getBlockPlaced(), Krip.context));
		eventObj.set("placedAgainst", new BlockObj(((BlockPlaceEvent) event).getBlockAgainst(), Krip.context));
		return eventObj;
	}
}
