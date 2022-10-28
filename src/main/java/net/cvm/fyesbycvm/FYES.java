package net.cvm.fyesbycvm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FYES implements ModInitializer {
	public static final String MOD_ID = "FYES";
	public static final String MOD_PREFIX = "[FYES]";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ArrayList<String> bannedMobs = new ArrayList<>();
	public static MinecraftServer server;
	public static boolean isLog = false;
	private final FYESInitializer modInit = FYESInitializer.getInstance();

	@Override
	public void onInitialize() {
		modInit.initConfigFile();
		modInit.checkBannedMobsList();

		ServerLifecycleEvents.SERVER_STARTED.register(this::getMinecraftServer);

		ServerTickEvents.START_WORLD_TICK.register(tick -> {
			WorldMobsCleaner.findAndClearMobs(server);
		});
	}
	private void getMinecraftServer(MinecraftServer minecraftServer) {
		server = minecraftServer;
	}
}
