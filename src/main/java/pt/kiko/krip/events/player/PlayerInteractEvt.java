package pt.kiko.krip.events.player;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.objects.BlockObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerInteractEvt extends KripEvent {

	static {
		Krip.registerEvent(new PlayerInteractEvt());
	}

	public PlayerInteractEvt() {
		super("PlayerInteractEvent", PlayerInteractEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof PlayerInteractEvent;

		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);

		eventObj.set("action", new StringValue(((PlayerInteractEvent) event).getAction().toString(), Krip.context));
		eventObj.set("player", new OnlinePlayerObj(((PlayerInteractEvent) event).getPlayer(), Krip.context));
		Block block = ((PlayerInteractEvent) event).getClickedBlock();
		if (block != null) eventObj.set("clickedBlock", new BlockObj(block, Krip.context));

		return eventObj;
	}
}
