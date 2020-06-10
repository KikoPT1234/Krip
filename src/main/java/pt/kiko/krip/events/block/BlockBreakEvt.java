package pt.kiko.krip.events.block;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.BlockObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class BlockBreakEvt extends KripEvent {

	static {
		Krip.registerEvent(new BlockBreakEvt());
	}

	public BlockBreakEvt() {
		super("BlockBreakEvent", BlockBreakEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof BlockBreakEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
		eventObj.set("player", new OnlinePlayerObj(((BlockBreakEvent) event).getPlayer(), Krip.context));
		eventObj.set("block", new BlockObj(((BlockBreakEvent) event).getBlock(), Krip.context));
		return eventObj;
	}
}
