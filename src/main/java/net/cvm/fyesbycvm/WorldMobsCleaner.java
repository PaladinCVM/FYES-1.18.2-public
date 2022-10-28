package net.cvm.fyesbycvm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

import static net.cvm.fyesbycvm.FYES.*;

public class WorldMobsCleaner {
    public static void findAndClearMobs(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        List<ServerPlayerEntity> players = world.getPlayers();

        if (!players.isEmpty()) {
            for (ServerPlayerEntity player : players) {
                world.getEntitiesByClass(MobEntity.class, new Box(player.getBlockPos()).expand(1000), MobEntity::isAlive)
                    .stream()
                    .filter(entity -> bannedMobs.contains(Registry.ENTITY_TYPE.getId(entity.getType()).toString()))
                    .forEach(entity -> {
                        entity.remove(Entity.RemovalReason.DISCARDED);
                        if (isLog) {
                            LOGGER.info(MOD_PREFIX + Registry.ENTITY_TYPE.getId(entity.getType()) + " has been deleted as already spawned");
                        }
                    });
            }
        }
    }

}
