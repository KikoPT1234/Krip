package pt.kiko.krip.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryObj extends KripObject {

	public Inventory inventory;

	public InventoryObj(@NotNull Inventory inventory, @Nullable String name, Context context) {
		super(new HashMap<>(), context);

		this.inventory = inventory;

		value.put("type", new KripString(inventory.getType().toString(), context));

		if (name != null) value.put("name", new KripString(name, context));

		value.put("getLocation", new KripJavaFunction("getLocation", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				Location location = inventory.getLocation();
				return new RuntimeResult().success(location == null ? new KripNull(context) : new LocationObj(location, context));
			}
		});

		value.put("getMaxStackSize", new KripJavaFunction("getMaxStackSize", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripNumber(inventory.getMaxStackSize(), context));
			}
		});

		value.put("setMaxStackSize", new KripJavaFunction("setMaxStackSize", Collections.singletonList("size"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> size = context.symbolTable.get("size");

				if (!(size instanceof KripNumber)) return invalidType(size, context);
				if (!((KripNumber) size).isWhole())
					return result.failure(new RuntimeError(size.startPosition, size.endPosition, "Slot number must be whole", context));

				inventory.setMaxStackSize((int) (double) ((KripNumber) size).getValue());

				return result.success(InventoryObj.this);
			}
		});

		value.put("getItems", new KripJavaFunction("getItems", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripList(Arrays.stream(inventory.getContents()).map(item -> new ItemStackObj(item, context)).collect(Collectors.toList()), context));
			}
		});

		value.put("setItems", new KripJavaFunction("setItems", Collections.singletonList("items"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> items = context.symbolTable.get("items");

				if (!(items instanceof KripList)) return invalidType(items, context);

				List<ItemStack> itemStacks = new ArrayList<>();

				for (KripValue<?> item : ((KripList) items).getValue()) {
					if (!(item instanceof KripString || item instanceof ItemStackObj || item instanceof MaterialObj))
						return invalidType(item, context);

					if (item instanceof KripString) {

						Material material = Material.getMaterial(item.getValueString());

						if (material == null)
							return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

						itemStacks.add(new ItemStack(material));
					} else if (item instanceof MaterialObj)
						itemStacks.add(new ItemStack(((MaterialObj) item).material));
					else itemStacks.add(((ItemStackObj) item).itemStack);
				}

				inventory.setContents(itemStacks.toArray(new ItemStack[]{}));

				return result.success(InventoryObj.this);
			}
		});

		value.put("getStorageContents", new KripJavaFunction("getStorageContents", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripList(Arrays.stream(inventory.getStorageContents()).map(item -> new ItemStackObj(item, context)).collect(Collectors.toList()), context));
			}
		});

		value.put("setStorageContents", new KripJavaFunction("setStorageContents", Collections.singletonList("items"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> items = context.symbolTable.get("items");

				if (!(items instanceof KripList)) return invalidType(items, context);

				List<ItemStack> itemStacks = new ArrayList<>();

				for (KripValue<?> item : ((KripList) items).getValue()) {
					if (!(item instanceof KripString || item instanceof ItemStackObj || item instanceof MaterialObj))
						return invalidType(item, context);

					if (item instanceof KripString) {

						Material material = Material.getMaterial(item.getValueString());

						if (material == null)
							return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

						itemStacks.add(new ItemStack(material));
					} else if (item instanceof MaterialObj)
						itemStacks.add(new ItemStack(((MaterialObj) item).material));
					else itemStacks.add(((ItemStackObj) item).itemStack);
				}

				inventory.setStorageContents(itemStacks.toArray(new ItemStack[]{}));

				return result.success(InventoryObj.this);
			}
		});

		value.put("getItem", new KripJavaFunction("getItem", Collections.singletonList("slot"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> slot = context.symbolTable.get("slot");

				if (!(slot instanceof KripNumber)) return invalidType(slot, context);
				if (!((KripNumber) slot).isWhole())
					return result.failure(new RuntimeError(slot.startPosition, slot.endPosition, "Slot number must be whole", context));

				ItemStack itemStack = inventory.getItem((int) (double) ((KripNumber) slot).getValue());

				if (itemStack == null) return result.success(InventoryObj.this);
				else return result.success(new ItemStackObj(itemStack, context));
			}
		});

		value.put("setItem", new KripJavaFunction("setItem", Arrays.asList("item", "amount"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("item");
				KripValue<?> amount = context.symbolTable.get("amount");
				int itemAmt;

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					return invalidType(item, context);
				if (!(amount instanceof KripNumber || amount instanceof KripNull)) return invalidType(amount, context);
				if (amount instanceof KripNumber && !((KripNumber) amount).isWhole())
					return result.failure(new RuntimeError(amount.startPosition, amount.endPosition, "Amount must be whole", context));

				itemAmt = amount instanceof KripNull ? 1 : (int) (double) ((KripNumber) amount).getValue();

				if (item instanceof ItemStackObj) inventory.setItem(itemAmt, ((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj)
					inventory.setItem(itemAmt, new ItemStack(((MaterialObj) item).material, 1));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setItem(itemAmt, new ItemStack(material, 1));
				}

				return result.success(InventoryObj.this);
			}
		});

		value.put("remove", new KripJavaFunction("remove", Collections.singletonList("item"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("item");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					return invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.remove(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj) inventory.remove(((MaterialObj) item).material);
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.remove(material);
				}

				return result.success(InventoryObj.this);
			}
		});

		value.put("removeItems", new KripJavaFunction("removeItems", Collections.singletonList("items"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> items = context.symbolTable.get("items");

				if (!(items instanceof KripList)) return invalidType(items, context);

				List<ItemStack> itemStacks = new ArrayList<>();

				for (KripValue<?> item : ((KripList) items).getValue()) {
					if (!(item instanceof KripString || item instanceof ItemStackObj || item instanceof MaterialObj))
						return invalidType(item, context);

					if (item instanceof KripString) {

						Material material = Material.getMaterial(item.getValueString());

						if (material == null)
							return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

						itemStacks.add(new ItemStack(material));
					} else if (item instanceof MaterialObj)
						itemStacks.add(new ItemStack(((MaterialObj) item).material));
					else itemStacks.add(((ItemStackObj) item).itemStack);
				}

				inventory.removeItem(itemStacks.toArray(new ItemStack[]{}));

				return result.success(InventoryObj.this);
			}
		});

		value.put("all", new KripJavaFunction("all", Collections.singletonList("item"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("item");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					return invalidType(item, context);

				List<KripValue<?>> objects;
				if (item instanceof ItemStackObj)
					objects = inventory.all(((ItemStackObj) item).itemStack).entrySet().stream().map(entry -> {
						KripObject value = new KripObject(new HashMap<>(), context);

						value.set("slot", new KripNumber(entry.getKey(), context));
						value.set("stack", new ItemStackObj(entry.getValue(), context));

						return value;
					}).collect(Collectors.toList());
				else {
					Material material;

					if (item instanceof MaterialObj) material = ((MaterialObj) item).material;
					else {
						material = Material.getMaterial(item.getValueString());

						if (material == null)
							return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));
					}

					objects = inventory.all(material).entrySet().stream().map(entry -> {
						KripObject value = new KripObject(new HashMap<>(), context);

						value.set("slot", new KripNumber(entry.getKey(), context));
						value.set("stack", new ItemStackObj(entry.getValue(), context));

						return value;
					}).collect(Collectors.toList());
				}

				return result.success(new KripList(objects, context));
			}
		});

		value.put("addItems", new KripJavaFunction("addItems", Collections.singletonList("items"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> items = context.symbolTable.get("items");

				if (!(items instanceof KripList)) return invalidType(items, context);

				List<ItemStack> itemStacks = new ArrayList<>();

				KripList itemList = (KripList) items;

				for (KripValue<?> item : itemList.getValue()) {
					if (!(item instanceof KripString || item instanceof ItemStackObj || item instanceof MaterialObj))
						return invalidType(item, context);

					if (item instanceof KripString) {

						Material material = Material.getMaterial(item.getValueString());

						if (material == null)
							return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

						itemStacks.add(new ItemStack(material));
					} else if (item instanceof MaterialObj)
						itemStacks.add(new ItemStack(((MaterialObj) item).material));
					else itemStacks.add(((ItemStackObj) item).itemStack);
				}

				inventory.addItem(itemStacks.toArray(new ItemStack[]{}));

				return result.success(InventoryObj.this);
			}
		});

		value.put("clear", new KripJavaFunction("clear", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				inventory.clear();

				return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("contains", new KripJavaFunction("contains", Arrays.asList("item", "amount"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("item");
				KripValue<?> amount = context.symbolTable.get("amount");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					return invalidType(item, context);
				if (!(amount instanceof KripNumber || amount instanceof KripNull)) return invalidType(amount, context);
				if (amount instanceof KripNumber && !((KripNumber) amount).isWhole())
					return result.failure(new RuntimeError(amount.startPosition, amount.endPosition, "Amount must be whole", context));

				if (item instanceof ItemStackObj) {
					if (amount instanceof KripNumber)
						return result.success(new KripBoolean(inventory.contains(((ItemStackObj) item).itemStack, (int) (double) ((KripNumber) amount).getValue()), context));
					else
						return result.success(new KripBoolean(inventory.contains(((ItemStackObj) item).itemStack), context));
				} else if (item instanceof MaterialObj) {
					if (amount instanceof KripNumber)
						return result.success(new KripBoolean(inventory.contains(((MaterialObj) item).material, (int) (double) ((KripNumber) amount).getValue()), context));
					else
						return result.success(new KripBoolean(inventory.contains(((MaterialObj) item).material), context));
				} else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					if (amount instanceof KripNumber)
						return result.success(new KripBoolean(inventory.contains(material, (int) (double) ((KripNumber) amount).getValue()), context));
					else return result.success(new KripBoolean(inventory.contains(material), context));
				}
			}
		});

		value.put("first", new KripJavaFunction("first", Collections.singletonList("item"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("item");

				if (!(item instanceof MaterialObj || item instanceof ItemStackObj || item instanceof KripString))
					return invalidType(item, context);

				if (item instanceof ItemStackObj)
					return result.success(new KripNumber(inventory.first(((ItemStackObj) item).itemStack), context));
				else if (item instanceof MaterialObj)
					return result.success(new KripNumber(inventory.first(((MaterialObj) item).material), context));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					return result.success(new KripNumber(inventory.first(material), context));
				}
			}
		});

		value.put("firstEmptySlot", new KripJavaFunction("firstEmptySlot", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripNumber(inventory.firstEmpty(), context));
			}
		});
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof InventoryObj) {
			return new RuntimeResult().success(new KripBoolean(inventory == ((InventoryObj) other).inventory || inventory.equals(((InventoryObj) other).inventory), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

}
