package pt.kiko.krip.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.NumberValue;
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

		Player victim = ((PlayerDeathEvent) event).getEntity();

		eventObj.set("victim", new OnlinePlayerObj(victim, Krip.context));
		eventObj.set("message", new StringValue(((PlayerDeathEvent) event).getDeathMessage(), Krip.context));
		eventObj.set("droppedExp", new NumberValue(((PlayerDeathEvent) event).getDroppedExp(), Krip.context));

		if (victim.getKiller() != null) eventObj.set("attacker", new OnlinePlayerObj(victim.getKiller(), Krip.context));

		return eventObj;
	}
}
