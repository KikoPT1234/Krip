package pt.kiko.krip.objects;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripBoolean;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripValue;

import java.util.HashMap;
import java.util.Objects;

public class LocationObj extends KripObject {

    public Location location;

    public LocationObj(@NotNull Location location, Context context) {
        super(new HashMap<>(), context);
        this.location = location;
        value.put("x", new KripNumber(location.getX(), context));
        value.put("y", new KripNumber(location.getY(), context));
        value.put("z", new KripNumber(location.getZ(), context));
        value.put("yaw", new KripNumber(location.getYaw(), context));
        value.put("pitch", new KripNumber(location.getPitch(), context));
        value.put("world", new WorldObj(Objects.requireNonNull(location.getWorld()), context));
    }

    @Override
    public RuntimeResult equal(KripValue<?> other) {
        if (other instanceof LocationObj) {
            return new RuntimeResult().success(new KripBoolean(location == ((LocationObj) other).location || location.equals(((LocationObj) other).location), context));
        } else return new RuntimeResult().success(new KripBoolean(false, context));
    }
}
