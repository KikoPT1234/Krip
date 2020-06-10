package pt.kiko.krip.objects;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WorldObj extends ObjectValue {

    public WorldObj(@NotNull World world, Context context) {
        super(new HashMap<>(), context);
        value.put("name", new StringValue(world.getName(), context));

        value.put("getPlayers", new BuiltInFunctionValue("getPlayers", Collections.emptyList(), context) {
            @Override
            public RuntimeResult run(Context context) {
                List<Player> players = world.getPlayers();

                return new RuntimeResult().success(new ListValue(players.stream().map(player -> new OnlinePlayerObj(player, context)).collect(Collectors.toList()), context));
            }
        });

        value.put("getBlock", new BuiltInFunctionValue("getBlock", Arrays.asList("x", "y", "z"), context) {
            @Override
            public RuntimeResult run(Context context) {
                RuntimeResult result = new RuntimeResult();
                Value<?> x = context.symbolTable.get("x");
                Value<?> y = context.symbolTable.get("y");
                Value<?> z = context.symbolTable.get("z");

                if (!(x instanceof LocationObj || x instanceof NumberValue)) return invalidType(x, context);
                if (!(y instanceof NullValue || y instanceof NumberValue)) return invalidType(y, context);
                if (!(z instanceof NullValue || z instanceof NumberValue)) return invalidType(z, context);
                if (x instanceof LocationObj && y instanceof NumberValue) return invalidType(y, context);
                if (x instanceof LocationObj && z instanceof NumberValue) return invalidType(z, context);
                if (y instanceof NumberValue && z instanceof NullValue) return invalidType(z, context);

                if (x instanceof LocationObj)
                    return result.success(new BlockObj(world.getBlockAt(((LocationObj) x).location), context.parent));
                else
                    return result.success(new BlockObj(world.getBlockAt(Integer.parseInt(x.getValueString()), Integer.parseInt(y.getValueString()), Integer.parseInt(z.getValueString())), context.parent));
            }
        });
    }
}
