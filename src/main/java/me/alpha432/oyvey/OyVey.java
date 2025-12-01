package me.alpha432.oyvey;

import me.alpha432.oyvey.manager.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OyVey implements ModInitializer, ClientModInitializer {
    public static final String NAME = "Ignite Client";
    public static final String VERSION = "1.0.0 - " + SharedConstants.getGameVersion().getName();

    public static float TIMER = 1f;

    public static final Logger LOGGER = LogManager.getLogger("Ignite");
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    @Override public void onInitialize() {
        eventManager = new EventManager();
        serverManager = new ServerManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();
    }

@Override
public void onInitializeClient() {
    // Ensure all managers are initialized
    if (eventManager == null) eventManager = new EventManager();
    if (moduleManager == null) moduleManager = new ModuleManager();
    if (serverManager == null) serverManager = new ServerManager();
    if (rotationManager == null) rotationManager = new RotationManager();
    if (positionManager == null) positionManager = new PositionManager();
    if (friendManager == null) friendManager = new FriendManager();
    if (colorManager == null) colorManager = new ColorManager();
    if (commandManager == null) commandManager = new CommandManager();
    if (speedManager == null) speedManager = new SpeedManager();
    if (holeManager == null) holeManager = new HoleManager();

    // Now it’s safe to call init methods
    eventManager.init();
    moduleManager.init();

    if (configManager == null) configManager = new ConfigManager();
    configManager.load();
    colorManager.init();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
}

