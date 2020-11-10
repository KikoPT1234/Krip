package pt.kiko.krip.events.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.entity.LivingEntityObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class EntityDeathEvt extends KripEvent<EntityDeathEvent> {

	static {
		Krip.registerEvent(new EntityDeathEvt());
	}

	public EntityDeathEvt() {
		super("EntityDeathEvent", EntityDeathEvent.class);
	}

    @Override
    protected KripObject getEvent(@NotNull EntityDeathEvent event) {
        KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);
        LivingEntity victim = event.getEntity();

        eventObj.set("victim", new LivingEntityObj(victim, Krip.context));
        eventObj.set("droppedExp", new KripNumber(event.getDroppedExp(), Krip.context));

        if (victim.getKiller() != null) eventObj.set("attacker", new OnlinePlayerObj(victim.getKiller(), Krip.context));

        return eventObj;
    }
}
