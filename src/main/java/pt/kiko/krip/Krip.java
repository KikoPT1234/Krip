package pt.kiko.krip;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
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

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Main class
 *
 * @author KikoPT1234
 * @version 0.1.0
 */

public class Krip extends JavaPlugin {

	public static Context context = new Context("<program>");
	public static HashMap<String, Value<?>> globals = new HashMap<>();
	public static Krip plugin;
	public static Map<String, KripEvent> events = new HashMap<>();
	public static List<String> registeredNames = new ArrayList<>();
	public static Map<String, String> commandNames = new HashMap<>();
	public static SimpleCommandMap commandMap;
	public static Map<String, Command> knownCommands;
	public static Map<String, List<BukkitTask>> tasks = new HashMap<>();

	public static Method syncCommandsMethod;
	public static File pluginFolder;
	public static File scriptFolder;
	public static File variableFile;

	public static Economy economy;
	public static Chat chat;
	public static Permission permission;

	/**
	 * Runs the specified code
	 *
	 * @param code     The code to run
	 * @param fileName The file name to display on errors
	 * @return A RunResult instance
	 * @see RunResult
	 */
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

	public static void registerValue(String name, Value<?> value) {
		context.symbolTable.set(name, value, true);
		registeredNames.add(name);
	}

	public static void registerEvent(KripEvent kripEvent) {
		events.put(kripEvent.name, kripEvent);
		plugin.getServer().getPluginManager().registerEvent(kripEvent.event, kripEvent, EventPriority.NORMAL, (listener, event) -> {
			if (!kripEvent.event.isInstance(event)) return;
			kripEvent.execute(event);
		}, plugin);
	}

	/**
	 * Loads all classes on the specified package name
	 *
	 * @param packageName The name of the package
	 */

	public static void loadClasses(@NotNull String packageName) {

		String path = null;
		try {
			path = Krip.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		assert path != null;

		try {
			JarFile jarFile = new JarFile(path);
			Enumeration<JarEntry> entries = jarFile.entries();

			URL[] urls = {new URL("jar:file:" + path + "!/" + packageName.replace('.', '/'))};
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
		} catch (ClassNotFoundException | IOException e) {
			plugin.getLogger().warning("Error while loading classes in package " + packageName);
			e.printStackTrace();
		}
	}

	/**
	 * @param file The File instance to load
	 * @return The file data
	 */

	public static @NotNull String loadFile(File file) {
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

	private static void loadVault() {
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		if (vault == null) return;

		RegisteredServiceProvider<Economy> econRsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (econRsp == null) return;
		plugin.getLogger().info("Vault found, enabling support");
		economy = econRsp.getProvider();

		RegisteredServiceProvider<Chat> chatRsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
		if (chatRsp == null) return;
		chat = chatRsp.getProvider();

		RegisteredServiceProvider<Permission> permRsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
		if (permRsp == null) return;
		permission = permRsp.getProvider();

		plugin.getLogger().info("Vault loaded!");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {

		Krip.plugin = this;

		loadVault();

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
		variableFile = new File(pluginFolder, "globals");
		if (!scriptFolder.exists()) created = scriptFolder.mkdirs();
		if (!created) getLogger().warning("Failed to create plugin/script directory");
		if (!variableFile.exists()) {
			try {
				created = variableFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!created) getLogger().warning("Failed to create plugin/script directory");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(variableFile));
			if (reader.readLine() != null) {
				FileInputStream fileInputStream = new FileInputStream(variableFile);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				globals = (HashMap<String, Value<?>>) objectInputStream.readObject();
				globals.forEach((name, global) -> {
					global.setContext(context);
					context.symbolTable.set(name, global, false);
					registeredNames.add(name);
				});
				objectInputStream.close();
				fileInputStream.close();
			}

		} catch (IOException | ClassNotFoundException exception) {
			exception.printStackTrace();
		}

		loadClasses("pt.kiko.krip.variables");
		loadClasses("pt.kiko.krip.events");

		new KripCommand();

		for (File file : scriptFolder.listFiles()) {
			String code = loadFile(file);
			try {
				run(code, file.getName());
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().warning("There has been an error with Krip, please report it at https://github.com/KikoPT1234/Krip/issues");
			}
		}

		getServer().getLogger().info("Krip enabled!");
	}

	@Override
	public void onDisable() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(variableFile);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(globals);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}