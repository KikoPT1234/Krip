package pt.kiko.krip.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.EventInfo;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.PlayerObj;

import java.util.HashMap;
import java.util.Objects;

public class PlayerMoveEvt extends EventInfo {

    static {
        Krip.registerEvent(new PlayerMoveEvt());
    }

    public PlayerMoveEvt() {
        super("PlayerMoveEvent", PlayerMoveEvent.class);
    }

    @Override
    protected ObjectValue getEvent(Event event) {
        assert event instanceof PlayerMoveEvent;
        PlayerMoveEvent evt = (PlayerMoveEvent) event;

        ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
        eventObj.set("to", new LocationObj(Objects.requireNonNull(evt.getTo()), Krip.context));
        eventObj.set("from", new LocationObj(evt.getFrom(), Krip.context));
        eventObj.set("player", new PlayerObj(evt.getPlayer(), Krip.context));
        return eventObj;
    }
}
