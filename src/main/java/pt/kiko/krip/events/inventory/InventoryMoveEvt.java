package pt.kiko.krip.events.inventory;

import org.bukkit.event.inventory.InventoryMoveItemEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.objects.inventory.InventoryObj;
import pt.kiko.krip.objects.material.ItemStackObj;

import java.util.HashMap;

public class InventoryMoveEvt extends KripEvent<InventoryMoveItemEvent> {

	static {
		Krip.registerEvent(new InventoryMoveEvt());
	}

	public InventoryMoveEvt() {
		super("InventoryMoveEvent", InventoryMoveItemEvent.class);
	}

	@Override
	protected KripObject getEvent(InventoryMoveItemEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("fromInv", new InventoryObj(event.getSource(), null, Krip.context));
		eventObj.set("toInv", new InventoryObj(event.getDestination(), null, Krip.context));
		eventObj.set("material", new ItemStackObj(event.getItem(), Krip.context));

		return eventObj;
	}
}
