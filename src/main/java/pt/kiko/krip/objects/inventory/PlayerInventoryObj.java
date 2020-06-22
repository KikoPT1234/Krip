package pt.kiko.krip.objects.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.material.ItemStackObj;
import pt.kiko.krip.objects.material.MaterialObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerInventoryObj extends InventoryObj {

	public PlayerInventoryObj(PlayerInventory inventory, Context context) {
		super(inventory, null, context);

		value.put("getItemInMainHand", new KripJavaFunction("getItemInMainHand", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new ItemStackObj(inventory.getItemInMainHand(), context));
			}
		});

		value.put("setItemInMainHand", new KripJavaFunction("setItemInMainHand", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("material");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.setItemInMainHand(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj)
					inventory.setItemInMainHand(new ItemStack(((MaterialObj) item).material));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setItemInMainHand(new ItemStack(material, 1));
				}

				return result.success(PlayerInventoryObj.this);
			}
		});

		value.put("getItemInOffhand", new KripJavaFunction("getItemInOffhand", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new ItemStackObj(inventory.getItemInOffHand(), context));
			}
		});

		value.put("setItemInOffhand", new KripJavaFunction("setItemInOffhand", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("material");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.setItemInOffHand(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj)
					inventory.setItemInOffHand(new ItemStack(((MaterialObj) item).material));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setItemInOffHand(new ItemStack(material, 1));
				}

				return result.success(PlayerInventoryObj.this);
			}
		});

		value.put("getArmorContents", new KripJavaFunction("getArmorContents", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripList(Arrays.stream(inventory.getArmorContents()).map(itemStack -> new ItemStackObj(itemStack, context)).collect(Collectors.toList()), context));
			}
		});

		value.put("setArmorContents", new KripJavaFunction("setArmorContents", Collections.singletonList("items"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> items = context.symbolTable.get("items");

				if (!(items instanceof KripList)) return invalidType(items, context);
				if (((KripList) items).getValue().size() != 4)
					return result.failure(new RuntimeError(items.startPosition, items.endPosition, "Invalid list size", context));

				List<ItemStack> itemStacks = new ArrayList<>();

				for (KripValue<?> item : ((KripList) items).getValue()) {
					if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
						invalidType(item, context);

					if (item instanceof ItemStackObj) itemStacks.add(((ItemStackObj) item).itemStack);
					else if (item instanceof MaterialObj)
						itemStacks.add(new ItemStack(((MaterialObj) item).material));
					else {
						Material material = Material.getMaterial(item.getValueString());

						if (material == null)
							return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

						itemStacks.add(new ItemStack(material, 1));
					}
				}

				inventory.setArmorContents(itemStacks.toArray(new ItemStack[]{}));

				return result.success(PlayerInventoryObj.this);
			}
		});

		value.put("getHelmet", new KripJavaFunction("getHelmet", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				ItemStack stack = inventory.getHelmet();
				if (stack != null) return new RuntimeResult().success(new ItemStackObj(stack, context));
				else return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("setHelmet", new KripJavaFunction("setHelmet", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("material");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.setHelmet(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj) inventory.setHelmet(new ItemStack(((MaterialObj) item).material));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setHelmet(new ItemStack(material, 1));
				}

				return result.success(PlayerInventoryObj.this);
			}
		});

		value.put("getChestplate", new KripJavaFunction("getChestplate", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				ItemStack stack = inventory.getChestplate();
				if (stack != null) return new RuntimeResult().success(new ItemStackObj(stack, context));
				else return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("setChestplate", new KripJavaFunction("setChestplate", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("material");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.setChestplate(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj)
					inventory.setChestplate(new ItemStack(((MaterialObj) item).material));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setChestplate(new ItemStack(material, 1));
				}

				return result.success(PlayerInventoryObj.this);
			}
		});

		value.put("getLeggings", new KripJavaFunction("getLeggings", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				ItemStack stack = inventory.getLeggings();
				if (stack != null) return new RuntimeResult().success(new ItemStackObj(stack, context));
				else return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("setLeggings", new KripJavaFunction("setLeggings", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("material");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.setLeggings(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj)
					inventory.setLeggings(new ItemStack(((MaterialObj) item).material));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setLeggings(new ItemStack(material, 1));
				}

				return result.success(PlayerInventoryObj.this);
			}
		});

		value.put("getBoots", new KripJavaFunction("getBoots", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				ItemStack stack = inventory.getBoots();
				if (stack != null) return new RuntimeResult().success(new ItemStackObj(stack, context));
				else return new RuntimeResult().success(new KripNull(context));
			}
		});

		value.put("setBoots", new KripJavaFunction("setBoots", Collections.singletonList("material"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> item = context.symbolTable.get("material");

				if (!(item instanceof ItemStackObj || item instanceof MaterialObj || item instanceof KripString))
					invalidType(item, context);

				if (item instanceof ItemStackObj) inventory.setBoots(((ItemStackObj) item).itemStack);
				else if (item instanceof MaterialObj) inventory.setBoots(new ItemStack(((MaterialObj) item).material));
				else {
					Material material = Material.getMaterial(item.getValueString());

					if (material == null)
						return result.failure(new RuntimeError(item.startPosition, item.endPosition, "Invalid material", context));

					inventory.setBoots(new ItemStack(material, 1));
				}

				return result.success(PlayerInventoryObj.this);
			}
		});
	}

}
