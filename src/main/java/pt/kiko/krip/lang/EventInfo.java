package pt.kiko.krip.lang;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import pt.kiko.krip.lang.values.BaseFunctionValue;
import pt.kiko.krip.lang.values.Value;

import java.util.ArrayList;
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
		populateArgs(event);
		functions.forEach(function -> function.execute(args));
	}

	protected abstract void populateArgs(Event event);

}
