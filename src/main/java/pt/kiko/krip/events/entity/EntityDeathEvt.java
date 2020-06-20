package pt.kiko.krip.events.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.LivingEntityObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class EntityDeathEvt extends KripEvent {

	static {
		Krip.registerEvent(new EntityDeathEvt());
	}

	public EntityDeathEvt() {
		super("EntityDeathEvent", EntityDeathEvent.class);
	}

	@Override
	protected KripObject getEvent(Event event) {
		assert event instanceof EntityDeathEvent;
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
		LivingEntity victim = ((EntityDeathEvent) event).getEntity();

		eventObj.set("victim", new LivingEntityObj(victim, Krip.context));
		eventObj.set("droppedExp", new KripNumber(((EntityDeathEvent) event).getDroppedExp(), Krip.context));

		if (victim.getKiller() != null) eventObj.set("attacker", new OnlinePlayerObj(victim.getKiller(), Krip.context));

		return eventObj;
	}
}
