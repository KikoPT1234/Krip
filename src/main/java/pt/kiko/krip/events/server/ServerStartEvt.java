package pt.kiko.krip.events.server;

import org.bukkit.event.Event;
import org.bukkit.event.server.ServerLoadEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;

import java.util.HashMap;

public class ServerStartEvt extends KripEvent {

	static {
		Krip.registerEvent(new ServerStartEvt());
	}

	public ServerStartEvt() {
		super("ServerStartEvent", ServerLoadEvent.class);
	}

	@Override
	protected KripObject getEvent(Event event) {
		return new KripObject(new HashMap<>(), Krip.context);
	}
}
