package pt.kiko.krip.objects;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.ListValue;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;

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
    }
}
