package pt.kiko.krip.events.inventory;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.InventoryObj;
import pt.kiko.krip.objects.ItemStackObj;

import java.util.HashMap;

public class InventoryMoveEvt extends KripEvent {

	static {
		Krip.registerEvent(new InventoryMoveEvt());
	}

	public InventoryMoveEvt() {
		super("InventoryMoveEvent", InventoryMoveItemEvent.class);
	}

	@Override
	protected KripObject getEvent(Event event) {
		assert event instanceof InventoryMoveItemEvent;

		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("fromInv", new InventoryObj(((InventoryMoveItemEvent) event).getSource(), null, Krip.context));
		eventObj.set("toInv", new InventoryObj(((InventoryMoveItemEvent) event).getDestination(), null, Krip.context));
		eventObj.set("item", new ItemStackObj(((InventoryMoveItemEvent) event).getItem(), Krip.context));

		return eventObj;
	}
}
