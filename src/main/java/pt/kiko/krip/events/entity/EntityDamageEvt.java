package pt.kiko.krip.events.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.entity.EntityObj;
import pt.kiko.krip.objects.entity.LivingEntityObj;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class EntityDamageEvt extends KripEvent<EntityDamageEvent> {

	static {
		Krip.registerEvent(new EntityDamageEvt());
	}

	public EntityDamageEvt() {
		super("EntityDamageEvent", EntityDamageEvent.class);
	}

	@Override
	protected KripObject getEvent(EntityDamageEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		Entity entity = event.getEntity();
		if (entity instanceof Player) eventObj.set("victim", new OnlinePlayerObj((Player) entity, Krip.context));
		else if (entity instanceof LivingEntity)
			eventObj.set("victim", new LivingEntityObj((LivingEntity) entity, Krip.context));
		else eventObj.set("victim", new EntityObj(entity, Krip.context));
		eventObj.set("cause", new KripString(event.getCause().toString(), Krip.context));
		eventObj.set("damage", new KripNumber(event.getDamage(), Krip.context));
		eventObj.set("finalDamage", new KripNumber(event.getFinalDamage(), Krip.context));

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
