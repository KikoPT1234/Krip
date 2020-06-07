package pt.kiko.krip.objects;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.values.NumberValue;
import pt.kiko.krip.lang.values.ObjectValue;

import java.util.HashMap;
import java.util.Objects;

public class LocationObj extends ObjectValue {

    public LocationObj(@NotNull Location location, Context context) {
        super(new HashMap<>(), context);
        value.put("x", new NumberValue(location.getX(), context));
        value.put("y", new NumberValue(location.getY(), context));
        value.put("z", new NumberValue(location.getZ(), context));
        value.put("yaw", new NumberValue(location.getYaw(), context));
        value.put("pitch", new NumberValue(location.getPitch(), context));
        value.put("world", new WorldObj(Objects.requireNonNull(location.getWorld()), context));
    }
}
