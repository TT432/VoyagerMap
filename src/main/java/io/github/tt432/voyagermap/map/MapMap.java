package io.github.tt432.voyagermap.map;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TT432
 */
public class MapMap {
    final Map<Long, MapChunk> chunkMap = new HashMap<>();

    public void update(LevelAccessor level, ChunkAccess chunk) {
        ChunkPos pos = chunk.getPos();
        int x = pos.x;
        int y = pos.z;
        long packed = ((long) x) << 32 | y;

        if (!chunkMap.containsKey(packed)) {
            MapChunk newChunk = new MapChunk(x, y);
            newChunk.updateColor(level, chunk, chunkMap.get(packed - 1));
            chunkMap.put(packed, newChunk);

            if (chunkMap.get(packed + 1) != null) {
                chunkMap.get(packed + 1).updateColor(level, chunk, newChunk);
            }
        }
    }
}
