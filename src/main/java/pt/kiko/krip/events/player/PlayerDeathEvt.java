package pt.kiko.krip.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class PlayerDeathEvt extends KripEvent<PlayerDeathEvent> {

	static {
		Krip.registerEvent(new PlayerDeathEvt());
	}

	public PlayerDeathEvt() {
		super("PlayerDeathEvent", PlayerDeathEvent.class);
	}

	@Override
	protected KripObject getEvent(PlayerDeathEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		Player victim = event.getEntity();

		eventObj.set("victim", new OnlinePlayerObj(victim, Krip.context));
		eventObj.set("message", new KripString(event.getDeathMessage(), Krip.context));
		eventObj.set("droppedExp", new KripNumber(event.getDroppedExp(), Krip.context));

		if (victim.getKiller() != null) eventObj.set("attacker", new OnlinePlayerObj(victim.getKiller(), Krip.context));

		return eventObj;
	}
}
