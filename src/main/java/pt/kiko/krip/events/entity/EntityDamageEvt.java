package pt.kiko.krip.events.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.NumberValue;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.objects.BlockObj;
import pt.kiko.krip.objects.EntityObj;
import pt.kiko.krip.objects.LivingEntityObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class EntityDamageEvt extends KripEvent {

	static {
		Krip.registerEvent(new EntityDamageEvt());
	}

	public EntityDamageEvt() {
		super("EntityDamageEvent", EntityDamageEvent.class);
	}

	@Override
	protected ObjectValue getEvent(Event event) {
		assert event instanceof EntityDamageEvent;
		ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);

		Entity entity = ((EntityDamageEvent) event).getEntity();
		if (entity instanceof Player) eventObj.set("victim", new OnlinePlayerObj((Player) entity, Krip.context));
		else if (entity instanceof LivingEntity)
			eventObj.set("victim", new LivingEntityObj((LivingEntity) entity, Krip.context));
		else eventObj.set("victim", new EntityObj(entity, Krip.context));
		eventObj.set("cause", new StringValue(((EntityDamageEvent) event).getCause().toString(), Krip.context));
		eventObj.set("damage", new NumberValue(((EntityDamageEvent) event).getDamage(), Krip.context));
		eventObj.set("finalDamage", new NumberValue(((EntityDamageEvent) event).getFinalDamage(), Krip.context));

		if (event instanceof EntityDamageByEntityEvent) {
			Entity attacker = ((EntityDamageByEntityEvent) event).getDamager();

			if (attacker instanceof Player)
				eventObj.set("attacker", new OnlinePlayerObj((Player) attacker, Krip.context));
			else if (attacker instanceof LivingEntity)
				eventObj.set("attacker", new LivingEntityObj((LivingEntity) attacker, Krip.context));
			else eventObj.set("attacker", new EntityObj(attacker, Krip.context));
		} else if (event instanceof EntityDamageByBlockEvent) {
			Block block = ((EntityDamageByBlockEvent) event).getDamager();
			if (block != null) eventObj.set("block", new BlockObj(block, Krip.context));
		}

		return eventObj;
	}
}
