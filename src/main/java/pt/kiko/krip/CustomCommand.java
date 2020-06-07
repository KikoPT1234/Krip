package pt.kiko.krip;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BaseFunctionValue;
import pt.kiko.krip.lang.values.ListValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.lang.values.Value;
import pt.kiko.krip.objects.PlayerObj;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCommand implements TabExecutor {

	private final BaseFunctionValue function;
	private final Context context;
	public Command command;

	public CustomCommand(BaseFunctionValue function, Command command, Context context) {
		this.function = function;
		this.command = command;
		this.context = context;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		List<Value> argValues = Arrays.stream(args).map(arg -> new StringValue(arg, context)).collect(Collectors.toList());
		RuntimeResult result = function.execute(Arrays.asList(new PlayerObj((Player) commandSender, context), new ListValue(argValues, context)), context);
		if (result.error != null) {
			System.out.println(result.error.toString());
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		return Collections.emptyList();
	}
}
