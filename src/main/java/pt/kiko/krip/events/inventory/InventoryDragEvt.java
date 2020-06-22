package pt.kiko.krip.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripList;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.inventory.InventoryObj;
import pt.kiko.krip.objects.material.ItemStackObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;
import java.util.stream.Collectors;

public class InventoryDragEvt extends KripEvent<InventoryDragEvent> {

	static {
		Krip.registerEvent(new InventoryDragEvt());
	}

	public InventoryDragEvt() {
		super("InventoryDragEvent", InventoryDragEvent.class);
	}

	@Override
	protected KripObject getEvent(InventoryDragEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("player", new OnlinePlayerObj((Player) event.getWhoClicked(), Krip.context));
		eventObj.set("inventory", new InventoryObj(event.getInventory(), null, Krip.context));
		eventObj.set("items", new KripList(event.getNewItems().values().stream().map(itemStack -> new ItemStackObj(itemStack, Krip.context)).collect(Collectors.toList()), Krip.context));
		eventObj.set("slots", new KripList(event.getInventorySlots().stream().map(slot -> new KripNumber(slot, Krip.context)).collect(Collectors.toList()), Krip.context));
		eventObj.set("rawSlots", new KripList(event.getRawSlots().stream().map(slot -> new KripNumber(slot, Krip.context)).collect(Collectors.toList()), Krip.context));
		eventObj.set("type", new KripString(event.getType().toString(), Krip.context));

		return eventObj;
	}
}
