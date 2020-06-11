package pt.kiko.krip;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class KripEvent implements Listener {

	public String name;
	public List<BaseFunctionValue> functions = new ArrayList<>();
	public Class<? extends Event> event;

	public KripEvent(String name, Class<? extends Event> event) {
		this.name = name;
		this.event = event;
	}

	public void addFunction(BaseFunctionValue function) {
		functions.add(function);
	}

	public void execute(Event event) {
		ObjectValue eventObj = getEvent(event);
		eventObj.set("isCancellable", new BooleanValue(event instanceof Cancellable, Krip.context));
		if (event instanceof Cancellable && !((Cancellable) event).isCancelled())
			eventObj.set("cancel", new BuiltInFunctionValue("cancel", new ArrayList<>(), Krip.context) {
				@Override
				public RuntimeResult run(Context context) {
					((Cancellable) event).setCancelled(true);
					return new RuntimeResult().success(new NullValue(context.parent));
				}
			});
		functions.forEach(function -> {
			RuntimeResult result = new RuntimeResult();
			result.register(function.execute(Collections.singletonList(eventObj), function.context));
			if (result.error != null) System.out.println(result.error.toString());
		});
	}

	protected abstract ObjectValue getEvent(Event event);

}
