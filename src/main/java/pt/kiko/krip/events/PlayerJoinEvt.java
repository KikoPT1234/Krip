package pt.kiko.krip.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.EventInfo;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerJoinEvt extends EventInfo {

	static {
		Krip.registerEvent(new PlayerJoinEvt());
	}

	public PlayerJoinEvt() {
		super("playerJoin", PlayerJoinEvent.class);
	}

	@Override
	protected void populateArgs(Event event) {
		assert event instanceof PlayerJoinEvent;
		Map<String, Value> map = new HashMap<>();

		Player player = ((PlayerJoinEvent) event).getPlayer();

		map.put("name", new StringValue(player.getName(), Krip.context));
		map.put("displayName", new StringValue(player.getDisplayName(), Krip.context));
		map.put("uuid", new StringValue(player.getUniqueId().toString(), Krip.context));
		map.put("chat", new BuiltInFunctionValue("chat", Collections.singletonList("message"), Krip.context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value message = context.symbolTable.get("message");

				if (!(message instanceof StringValue)) return result.failure(new RuntimeError(message.startPosition, message.endPosition, "Invalid type", context));

				player.chat(message.getValue());

				return result.success(new NullValue(context.parent));
			}
		});

		args.add(new ObjectValue(map, Krip.context));
	}
}
