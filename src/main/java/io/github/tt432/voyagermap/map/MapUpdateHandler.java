package io.github.tt432.voyagermap.map;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class MapUpdateHandler {
    public static final MapMap map = new MapMap();

    @SubscribeEvent
    public static void onEvent(ChunkEvent.Load event) {
        ChunkAccess chunk = event.getChunk();
        LevelAccessor level = event.getLevel();

        map.update(level, chunk);
    }
}
