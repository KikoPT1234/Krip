package pt.kiko.krip.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.DateObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class KripEvent<T extends Event> implements Listener {

	public String name;
	public List<KripBaseFunction> functions = new ArrayList<>();
	public Class<? extends Event> event;

	public KripEvent(String name, Class<T> event) {
		this.name = name;
		this.event = event;
	}

	public void addFunction(KripBaseFunction function) {
		functions.add(function);
	}

	public void execute(T event) {
		KripObject eventObj = getEvent(event);
		eventObj.set("isCancellable", new KripBoolean(event instanceof Cancellable, Krip.context));

		eventObj.set("createdAt", new DateObj(Krip.context));

		if (event instanceof Cancellable && !((Cancellable) event).isCancelled()) {
			eventObj.set("cancel", new KripJavaFunction("cancel", new ArrayList<>(), Krip.context) {
				@Override
				public RuntimeResult run(Context context) {
					((Cancellable) event).setCancelled(true);
					return new RuntimeResult().success(new KripNull(context));
				}
			});
			eventObj.set("isCancelled", new KripJavaFunction("isCancelled", Collections.emptyList(), Krip.context) {
				@Override
				public RuntimeResult run(Context context) {
					return new RuntimeResult().success(new KripBoolean(((Cancellable) event).isCancelled(), context));
				}
			});
		}
		functions.forEach(function -> {
			RuntimeResult result = new RuntimeResult();
			result.register(function.execute(Collections.singletonList(eventObj), function.context));
			if (result.error != null) System.out.println(result.error.toString());
		});
	}

	protected abstract KripObject getEvent(T event);

}
