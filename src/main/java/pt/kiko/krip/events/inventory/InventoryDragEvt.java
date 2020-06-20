package pt.kiko.krip.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripList;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.InventoryObj;
import pt.kiko.krip.objects.ItemStackObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;
import java.util.stream.Collectors;

public class InventoryDragEvt extends KripEvent {

	static {
		Krip.registerEvent(new InventoryDragEvt());
	}

	public InventoryDragEvt() {
		super("InventoryDragEvent", InventoryDragEvent.class);
	}

	@Override
	protected KripObject getEvent(Event event) {
		assert event instanceof InventoryDragEvent;

		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("player", new OnlinePlayerObj((Player) ((InventoryDragEvent) event).getWhoClicked(), Krip.context));
		eventObj.set("inventory", new InventoryObj(((InventoryDragEvent) event).getInventory(), null, Krip.context));
		eventObj.set("items", new KripList(((InventoryDragEvent) event).getNewItems().values().stream().map(itemStack -> new ItemStackObj(itemStack, Krip.context)).collect(Collectors.toList()), Krip.context));
		eventObj.set("slots", new KripList(((InventoryDragEvent) event).getInventorySlots().stream().map(slot -> new KripNumber(slot, Krip.context)).collect(Collectors.toList()), Krip.context));
		eventObj.set("rawSlots", new KripList(((InventoryDragEvent) event).getRawSlots().stream().map(slot -> new KripNumber(slot, Krip.context)).collect(Collectors.toList()), Krip.context));
		eventObj.set("type", new KripString(((InventoryDragEvent) event).getType().toString(), Krip.context));

		return eventObj;
	}
}
