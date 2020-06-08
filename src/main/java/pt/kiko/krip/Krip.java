package pt.kiko.krip;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.*;
import pt.kiko.krip.lang.results.LexResult;
import pt.kiko.krip.lang.results.ParseResult;
import pt.kiko.krip.lang.results.RunResult;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Krip extends JavaPlugin {

	public static Context context = new Context("<program>");
	public static Krip plugin;
	public static Map<String, EventInfo> events = new HashMap<>();
	public static List<String> registeredNames = new ArrayList<>();
	public static Map<String, String> commandNames = new HashMap<>();
	public static SimpleCommandMap commandMap;
	public static Map<String, Command> knownCommands;
	public static Map<String, List<BukkitTask>> tasks = new HashMap<>();

	public static Method syncCommandsMethod;

	public static RunResult run(String code, String fileName) {
		RunResult result = new RunResult();

		if (context.symbolTable == null) context.symbolTable = new SymbolTable();

		Lexer lexer = new Lexer(code, fileName);
		LexResult lexResult = lexer.makeTokens();
		if (lexResult.error != null) {
			System.out.println(lexResult.error.toString());
			return result.failure(lexResult.error);
		}

		Parser parser = new Parser(lexResult.list);
		ParseResult parseResult = parser.parse();

		if (parseResult.error != null) {
			System.out.println(parseResult.error.toString());
			return result.failure(parseResult.error);
		}

		RuntimeResult runtimeResult = Interpreter.visit(parseResult.node, context);

		if (runtimeResult.error != null) {
			System.out.println(runtimeResult.error.toString());
			return result.failure(runtimeResult.error);
		}
		return result.success(runtimeResult.value);
	}

	public static void loadClasses(@NotNull String packageName) throws ClassNotFoundException, IOException {

		String path = null;
		try {
			path = Krip.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		assert path != null;

		JarFile jarFile = new JarFile(path);
		Enumeration<JarEntry> entries = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + path + "!/" + packageName.replace('.', '/')) };
		URLClassLoader classLoader = URLClassLoader.newInstance(urls);
		assert classLoader != null;

		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();

			if (je.isDirectory() || !je.getName().endsWith(".class")) continue;

			String className = je.getName().substring(0, je.getName().length() - 6);
			className = className.replace("/", ".");

			if (!className.startsWith(packageName)) continue;

			Class.forName(className);
		}
	}

	public static void registerValue(String name, Value<?> value) {
		context.symbolTable.set(name, value, true);
		registeredNames.add(name);
	}

	public static void registerEvent(EventInfo eventInfo) {
		events.put(eventInfo.name, eventInfo);
		plugin.getServer().getPluginManager().registerEvent(eventInfo.event, eventInfo, EventPriority.NORMAL, (listener, event) -> eventInfo.execute(event), plugin);
	}

	public File pluginFolder;
	public File scriptFolder;

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {

		Krip.plugin = this;

		try {
			SimplePluginManager pluginManager = (SimplePluginManager) Bukkit.getPluginManager();
			final Field commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			final Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
			commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getPluginManager());
			knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

			Class<?> craftServer;
			String revision = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			craftServer = Class.forName("org.bukkit.craftbukkit." + revision + ".CraftServer");

			syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
			syncCommandsMethod.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean created = true;

		pluginFolder = getDataFolder();
		scriptFolder = new File(pluginFolder, "scripts");
		if (!scriptFolder.exists()) created = scriptFolder.mkdirs();
		if (!created) getServer().getLogger().warning("Failed to create plugin/script directory");

		try {
			loadClasses("pt.kiko.krip.variables");
			loadClasses("pt.kiko.krip.events");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		new KripCommand();

		for (File file : scriptFolder.listFiles()) {
			String code = loadFile(file);
			run(code, file.getName());
		}

		getServer().getLogger().info("Krip enabled!");
	}

	public String loadFile(File file) {
		StringBuilder code = new StringBuilder();

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				code.append(data).append("\n");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return code.toString();
	}
}