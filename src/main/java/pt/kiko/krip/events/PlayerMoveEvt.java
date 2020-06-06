package pt.kiko.krip.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.EventInfo;
import pt.kiko.krip.lang.values.NullValue;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.objects.LocationObj;
import pt.kiko.krip.objects.PlayerObj;

import java.util.HashMap;

public class PlayerMoveEvt extends EventInfo {

    public PlayerMoveEvt() {
        super("PlayerMoveEvent", PlayerMoveEvent.class);
    }

    @Override
    protected ObjectValue getEvent(Event event) {
        assert event instanceof PlayerMoveEvent;
        PlayerMoveEvent evt = (PlayerMoveEvent)event;

        ObjectValue eventObj = new ObjectValue(new HashMap<>(), Krip.context);
        eventObj.set("to", new LocationObj(evt.getTo(), Krip.context));
        eventObj.set("from", new LocationObj(evt.getFrom(), Krip.context));
        eventObj.set("player", new PlayerObj(evt.getPlayer(), Krip.context));
        return eventObj;
    }
}
