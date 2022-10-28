package net.cvm.fyesbycvm.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static net.cvm.fyesbycvm.FYES.*;

@Mixin(SpawnRestriction.class)
public class MobsSpawnRestrictor {
    @Inject(method = "canSpawn", at = @At("RETURN"), cancellable = true)
    private static void restrictMobs(EntityType<?> entityType,
                                         ServerWorldAccess world,
                                         SpawnReason spawnReason,
                                         BlockPos position,
                                         Random random,
                                         CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue()                                                   // if game is going to spawn a mob
                && bannedMobs                                                       // and it is in a restriction list
                .contains(Registry.ENTITY_TYPE.getId(entityType)
                        .toString())) {
            info.setReturnValue(false);                                             // override return value to false
            if (isLog) {
                LOGGER.info(MOD_PREFIX + " Restricting "
                        + Registry.ENTITY_TYPE.getId(entityType)
                        + " from spawning. Spawn reason: " + spawnReason.toString());
            }
        }
    }
}
