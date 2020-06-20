package pt.kiko.krip.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pt.kiko.krip.Krip;
import pt.kiko.krip.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.InventoryObj;
import pt.kiko.krip.objects.ItemStackObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.HashMap;

public class InventoryClickEvt extends KripEvent {

	static {
		Krip.registerEvent(new InventoryClickEvt());
	}

	public InventoryClickEvt() {
		super("InventoryClickEvent", InventoryClickEvent.class);
	}

	@Override
	protected KripObject getEvent(Event event) {
		assert event instanceof InventoryClickEvent;

		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("player", new OnlinePlayerObj((Player) ((InventoryClickEvent) event).getWhoClicked(), Krip.context));
		eventObj.set("action", new KripString(((InventoryClickEvent) event).getAction().toString(), Krip.context));
		eventObj.set("clickType", new KripString(((InventoryClickEvent) event).getClick().toString(), Krip.context));
		eventObj.set("inventory", new InventoryObj(((InventoryClickEvent) event).getInventory(), null, Krip.context));
		Inventory inventory = ((InventoryClickEvent) event).getClickedInventory();
		if (inventory != null) {
			eventObj.set("clickedInventory", new InventoryObj(inventory, null, Krip.context));
			ItemStack itemStack = ((InventoryClickEvent) event).getCurrentItem();
			if (itemStack != null) eventObj.set("item", new ItemStackObj(itemStack, Krip.context));
		}
		eventObj.set("slotType", new KripString(((InventoryClickEvent) event).getSlotType().toString(), Krip.context));

		return eventObj;
	}
}
