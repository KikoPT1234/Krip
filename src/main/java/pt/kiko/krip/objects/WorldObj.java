package pt.kiko.krip.objects;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.StringValue;

import java.util.HashMap;

public class WorldObj extends ObjectValue {

    public WorldObj(@NotNull World world, Context context) {
        super(new HashMap<>(), context);
        value.put("name", new StringValue(world.getName(), context));
    }
}
