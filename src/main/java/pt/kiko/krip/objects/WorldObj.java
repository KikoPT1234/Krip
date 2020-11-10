package pt.kiko.krip.objects;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.material.BlockObj;
import pt.kiko.krip.objects.material.MaterialObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WorldObj extends KripObject {

    public World world;

    public WorldObj(@NotNull World world, Context context) {
        super(new HashMap<>(), context);

        this.world = world;

        value.put("name", new KripString(world.getName(), context));

        value.put("getPlayers", new KripJavaFunction("getPlayers", Collections.emptyList(), context) {
            @Override
            public RuntimeResult run(Context context) {
                List<Player> players = world.getPlayers();

                return new RuntimeResult().success(new KripList(players.stream().map(player -> new OnlinePlayerObj(player, context)).collect(Collectors.toList()), context));
            }
        });

        value.put("getBlock", new KripJavaFunction("getBlock", Arrays.asList("x", "y", "z"), context) {
            @Override
            public RuntimeResult run(Context context) {
                RuntimeResult result = new RuntimeResult();
                KripValue<?> x = context.symbolTable.get("x");
                KripValue<?> y = context.symbolTable.get("y");
                KripValue<?> z = context.symbolTable.get("z");

                if (!(x instanceof LocationObj || x instanceof KripNumber)) return invalidType(x, context);
                if (!(y instanceof KripNull || y instanceof KripNumber)) return invalidType(y, context);
                if (!(z instanceof KripNull || z instanceof KripNumber)) return invalidType(z, context);
                if (x instanceof LocationObj && y instanceof KripNumber) return invalidType(y, context);
                if (x instanceof LocationObj && z instanceof KripNumber) return invalidType(z, context);
                if (y instanceof KripNumber && z instanceof KripNull) return invalidType(z, context);

                if (x instanceof LocationObj)
                    return result.success(new BlockObj(world.getBlockAt(((LocationObj) x).location), context));
                else
                    return result.success(new BlockObj(world.getBlockAt(Integer.parseInt(x.getValueString()), Integer.parseInt(y.getValueString()), Integer.parseInt(z.getValueString())), context));
            }
        });

        value.put("setBlock", new KripJavaFunction("setBlock", Arrays.asList("x", "y", "z"), context) {
            @Override
            public RuntimeResult run(Context context) {
                RuntimeResult result = new RuntimeResult();
                KripValue<?> block = context.symbolTable.get("block");
                KripValue<?> x = context.symbolTable.get("x");
                KripValue<?> y = context.symbolTable.get("y");
                KripValue<?> z = context.symbolTable.get("z");

                if (!(block instanceof KripString || block instanceof MaterialObj)) return invalidType(block, context);
                if (!(x instanceof LocationObj || x instanceof KripNumber)) return invalidType(x, context);
                if (!(y instanceof KripNull || y instanceof KripNumber)) return invalidType(y, context);
                if (!(z instanceof KripNull || z instanceof KripNumber)) return invalidType(z, context);
                if (x instanceof LocationObj && y instanceof KripNumber) return invalidType(y, context);
                if (x instanceof LocationObj && z instanceof KripNumber) return invalidType(z, context);
                if (y instanceof KripNumber && z instanceof KripNull) return invalidType(z, context);

                Material material;
                if (block instanceof KripString) {
                    material = Material.getMaterial(block.getValueString());
                } else {
                    material = ((MaterialObj) block).material;
                }

                if (material == null)
                    return result.failure(new RuntimeError(startPosition, endPosition, "Material not found", context));

                if (x instanceof LocationObj) {
                    Block b = world.getBlockAt(((LocationObj) x).location);
                    b.setType(material);
                    return result.success(new BlockObj(b, context));
                } else {
                    assert y instanceof KripNumber;
                    Block b = world.getBlockAt((int) (double) ((KripNumber) x).getValue(), (int) (double) ((KripNumber) y).getValue(), (int) (double) ((KripNumber) z).getValue());
                    b.setType(material);
                    return result.success(new BlockObj(b, context));
                }
            }
        });
    }

    @Override
    public RuntimeResult equal(KripValue<?> other) {
        if (other instanceof WorldObj) {
            return new RuntimeResult().success(new KripBoolean(world == ((WorldObj) other).world || world.equals(((WorldObj) other).world), context));
        } else return new RuntimeResult().success(new KripBoolean(false, context));
    }
}
