package pt.kiko.krip.objects;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripBoolean;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripValue;

import java.util.HashMap;

public class EnchantmentObj extends KripObject {

	public Enchantment enchantment;

	public EnchantmentObj(@NotNull Enchantment enchantment, Context context) {
		super(new HashMap<>(), context);

		this.enchantment = enchantment;

		value.put("maxLevel", new KripNumber(enchantment.getMaxLevel(), context));
		value.put("startLevel", new KripNumber(enchantment.getStartLevel(), context));
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof EnchantmentObj) {
			return new RuntimeResult().success(new KripBoolean(enchantment == ((EnchantmentObj) other).enchantment || enchantment.equals(((EnchantmentObj) other).enchantment), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

}
