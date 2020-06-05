package pt.kiko.krip.lang;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class EventInfo implements Listener {

	public String name;
	public List<BaseFunctionValue> functions = new ArrayList<>();
	protected List<Value> args = new ArrayList<>();
	public Class<? extends Event> event;

	public EventInfo(String name, Class<? extends Event> event) {
		this.name = name;
		this.event = event;
	}

	public void addFunction(BaseFunctionValue function) {
		functions.add(function);
	}

	public void execute(Event event) {
		ObjectValue eventObj = getEvent(event);
		if (event instanceof Cancellable && !((Cancellable) event).isCancelled())
			eventObj.set("cancel", new BuiltInFunctionValue("cancel", new ArrayList<>(), Krip.context) {
				@Override
				public RuntimeResult run(Context context) {
					((Cancellable) event).setCancelled(true);
					return new RuntimeResult().success(new NullValue(context.parent));
				}
			});
		functions.forEach(function -> new BukkitRunnable() {
			@Override
			public void run() {
				RuntimeResult result = new RuntimeResult();
				result.register(function.execute(Collections.singletonList(eventObj), function.context));
				if (result.error != null) System.out.println(result.error.toString());
			}
		}.run());
	}

	protected abstract ObjectValue getEvent(Event event);

}
