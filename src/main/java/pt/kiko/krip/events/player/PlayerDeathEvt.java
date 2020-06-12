package pt.kiko.krip.events.player;

import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerDeathEvt extends KripEvent {

	static {
		Krip.registerEvent(new PlayerDeathEvt());
	}

	public PlayerDeathEvt() {
		super("PlayerDeathEvent", PlayerDeathEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof PlayerDeathEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);

		eventObj.set("player", new OnlinePlayerObj(((PlayerDeathEvent) event).getEntity(), Krip.context));
		eventObj.set("message", new StringValue(((PlayerDeathEvent) event).getDeathMessage(), Krip.context));

		return eventObj;
	}
}
