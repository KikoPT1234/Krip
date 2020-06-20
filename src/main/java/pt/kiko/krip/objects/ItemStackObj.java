package pt.kiko.krip.objects;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.*;
import java.util.stream.Collectors;

public class ItemStackObj extends KripObject {

	public ItemStack itemStack;

	public ItemStackObj(@NotNull ItemStack itemStack, Context context) {
		super(new HashMap<>(), context);

		this.itemStack = itemStack;

		if (itemStack.getItemMeta() == null)
			itemStack.setItemMeta(Bukkit.getItemFactory().getItemMeta(itemStack.getType()));

		value.put("material", new MaterialObj(itemStack.getType(), context));

		value.put("getAmount", new KripJavaFunction("getAmount", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripNumber(itemStack.getAmount(), context));
			}
		});

		value.put("setAmount", new KripJavaFunction("setAmount", Collections.singletonList("amount"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> amount = context.symbolTable.get("amount");

				if (!(amount instanceof KripNumber)) return invalidType(amount, context);
				if (!((KripNumber) amount).isWhole())
					return result.failure(new RuntimeError(amount.startPosition, amount.endPosition, "Amount must be whole", context));

				itemStack.setAmount((int) (double) ((KripNumber) amount).getValue());

				return result.success(ItemStackObj.this);
			}
		});

		value.put("getMaxStackSize", new KripJavaFunction("getMaxStackSize", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripNumber(itemStack.getMaxStackSize(), context));
			}
		});

		value.put("getEnchantments", new KripJavaFunction("getEnchantments", Collections.emptyList(), context) {
			@Override
			public RuntimeResult run(Context context) {
				Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
				return new RuntimeResult().success(new KripList(enchantments.keySet().stream().map(enchantment -> new EnchantmentObj(enchantment, context)).collect(Collectors.toList()), context));
			}
		});

		value.put("getEnchantmentLevel", new KripJavaFunction("getEnchantmentLevel", Collections.singletonList("enchantment"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> enchantment = context.symbolTable.get("enchantment");

				if (!(enchantment instanceof EnchantmentObj || enchantment instanceof KripString))
					return invalidType(enchantment, context);

				if (enchantment instanceof EnchantmentObj)
					return result.success(new KripNumber(itemStack.getEnchantmentLevel(((EnchantmentObj) enchantment).enchantment), context));
				else {
					Enchantment ench;

					try {
						ench = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(enchantment.getValueString()));
					} catch (IllegalArgumentException e) {
						return result.failure(new RuntimeError(enchantment.startPosition, enchantment.endPosition, "Invalid enchantment", context));
					}
					if (ench == null)
						return result.failure(new RuntimeError(startPosition, endPosition, "Invalid enchantment", context));

					return result.success(new KripNumber(itemStack.getEnchantmentLevel(ench), context));
				}
			}
		});

		value.put("addEnchantment", new KripJavaFunction("addEnchantment", Arrays.asList("enchantmentName", "level"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> name = context.symbolTable.get("enchantmentName");
				KripValue<?> level = context.symbolTable.get("level");

				if (!(name instanceof KripString || name instanceof EnchantmentObj)) return invalidType(name, context);
				if (!(level instanceof KripNumber)) return invalidType(level, context);
				if (!((KripNumber) level).isWhole())
					return result.failure(new RuntimeError(level.startPosition, level.endPosition, "Level must be whole", context));

				Enchantment enchantment;

				if (name instanceof KripString) try {
					enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(name.getValueString()));
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Invalid enchantment", context));
				}
				else enchantment = ((EnchantmentObj) name).enchantment;
				if (enchantment == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid enchantment", context));

				try {
					itemStack.addEnchantment(enchantment, (int) (double) ((KripNumber) level).getValue());
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(startPosition, endPosition, e.getMessage(), context));
				}

				return result.success(ItemStackObj.this);
			}
		});

		value.put("removeEnchantment", new KripJavaFunction("removeEnchantment", Collections.singletonList("enchantmentName"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> name = context.symbolTable.get("enchantmentName");

				if (!(name instanceof KripString || name instanceof EnchantmentObj)) return invalidType(name, context);

				Enchantment enchantment;

				if (name instanceof KripString) try {
					enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(name.getValueString()));
				} catch (IllegalArgumentException e) {
					return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Invalid enchantment", context));
				}
				else enchantment = ((EnchantmentObj) name).enchantment;

				if (enchantment == null)
					return result.failure(new RuntimeError(startPosition, endPosition, "Invalid enchantment", context));

				itemStack.removeEnchantment(enchantment);

				return result.success(ItemStackObj.this);
			}
		});

		if (itemStack.getItemMeta() != null) {
			value.put("getDisplayName", new KripJavaFunction("getDisplayName", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					return new RuntimeResult().success(new KripString(itemStack.getItemMeta().getDisplayName(), context));
				}
			});

			value.put("setDisplayName", new KripJavaFunction("setDisplayName", Collections.singletonList("name"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> name = context.symbolTable.get("name");

					if (!(name instanceof KripString)) return invalidType(name, context);

					ItemMeta meta = itemStack.getItemMeta();
					meta.setDisplayName(name.getValueString());
					itemStack.setItemMeta(meta);

					return result.success(ItemStackObj.this);
				}
			});

			value.put("getLore", new KripJavaFunction("getLore", Collections.emptyList(), context) {
				@Override
				public RuntimeResult run(Context context) {
					List<String> lore = itemStack.getItemMeta().getLore();
					if (lore == null) return new RuntimeResult().success(new KripNull(context));
					else
						return new RuntimeResult().success(new KripList(lore.stream().map(loreString -> new KripString(loreString, context)).collect(Collectors.toList()), context));
				}
			});

			value.put("setLore", new KripJavaFunction("setLore", Collections.singletonList("name"), context) {
				@Override
				public RuntimeResult run(Context context) {
					RuntimeResult result = new RuntimeResult();
					KripValue<?> name = context.symbolTable.get("name");

					if (!(name instanceof KripList)) return invalidType(name, context);

					List<String> loreList = new ArrayList<>();

					for (KripValue<?> loreLine : ((KripList) name).getValue()) {
						if (!(loreLine instanceof KripString)) return invalidType(loreLine, context);

						loreList.add(loreLine.getValueString());
					}

					ItemMeta meta = itemStack.getItemMeta();
					meta.setLore(loreList);
					itemStack.setItemMeta(meta);

					return result.success(ItemStackObj.this);
				}
			});
		}

	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof ItemStackObj) {
			return new RuntimeResult().success(new KripBoolean(itemStack == ((ItemStackObj) other).itemStack || itemStack.equals(((ItemStackObj) other).itemStack), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

}
