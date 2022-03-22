package pt.kiko.krip.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pt.kiko.krip.Krip;
import pt.kiko.krip.events.KripEvent;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.objects.inventory.InventoryObj;
import pt.kiko.krip.objects.material.ItemStackObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.HashMap;

public class InventoryClickEvt extends KripEvent<InventoryClickEvent> {

	static {
		Krip.registerEvent(new InventoryClickEvt());
	}

	public InventoryClickEvt() {
		super("InventoryClickEvent", InventoryClickEvent.class);
	}

	@Override
	protected KripObject getEvent(InventoryClickEvent event) {
		KripObject eventObj = new KripObject(new HashMap<>(), Krip.context);

		eventObj.set("player", new OnlinePlayerObj((Player) event.getWhoClicked(), Krip.context));
		eventObj.set("action", new KripString(event.getAction().toString(), Krip.context));
		eventObj.set("clickType", new KripString(event.getClick().toString(), Krip.context));
		eventObj.set("inventory", new InventoryObj(event.getInventory(), null, Krip.context));
		Inventory inventory = event.getClickedInventory();
		if (inventory != null) {
			eventObj.set("clickedInventory", new InventoryObj(inventory, null, Krip.context));
			ItemStack itemStack = event.getCurrentItem();
			if (itemStack != null) eventObj.set("material", new ItemStackObj(itemStack, Krip.context));
		}
		eventObj.set("slotType", new KripString(event.getSlotType().toString(), Krip.context));

		return eventObj;
	}
}
