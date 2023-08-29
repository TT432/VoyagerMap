package io.github.tt432.voyagermap.map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author TT432
 */
public class MapChunk {
    private static final int CHUNK_BLOCK_COUNT = 16;
    private static final int CHUNK_MAX_IDX = CHUNK_BLOCK_COUNT * CHUNK_BLOCK_COUNT;

    int x;
    int y;
    final byte[] colorMap = new byte[CHUNK_MAX_IDX * 3];
    /**
     * Because the current {@link DimensionType#MAX_Y} of 2031. <br>
     * the heightMap is temporarily stored as a short.
     */
    final short[] heightMap = new short[CHUNK_MAX_IDX];

    public MapChunk(int chunkX, int chunkY) {
        this.x = chunkX;
        this.y = chunkY;
    }

    public void updateColor(LevelAccessor level, ChunkAccess levelChunk, @Nullable MapChunk preChunk) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();

        for (int blockX = 0; blockX < CHUNK_BLOCK_COUNT; blockX++) {
            for (int blockY = 0; blockY < CHUNK_BLOCK_COUNT; blockY++) {
                int aBlockPosX = x * CHUNK_BLOCK_COUNT + blockX;
                int aBlockPosY = y * CHUNK_BLOCK_COUNT + blockY;

                mutableBlockPos.set(aBlockPosX, 0, aBlockPosY);
                int currY = levelChunk.getHeight(Heightmap.Types.WORLD_SURFACE, mutableBlockPos.getX(), mutableBlockPos.getZ()) + 1;

                Result result = getSinglePoint(level, mutableBlockPos, mutableBlockPos1, levelChunk, currY);
                MapColor mapColor = result.blockstate().getMapColor(level, mutableBlockPos);
                MapColor.Brightness brightness = getBrightness(blockX, blockY, preChunk, result, mapColor);

                int col = mapColor.col;
                int idx = blockX * CHUNK_BLOCK_COUNT + blockY;
                heightMap[idx] = (short) result.currY();
                byte r = (byte) ((col >> 16 & 255) * brightness.modifier / 255);
                byte g = (byte) ((col >> 8 & 255) * brightness.modifier / 255);
                byte b = (byte) ((col & 255) * brightness.modifier / 255);
                colorMap[idx * 3] = r;
                colorMap[idx * 3 + 1] = g;
                colorMap[idx * 3 + 2] = b;
            }
        }
    }

    @NotNull
    private MapColor.Brightness getBrightness(int blockX, int blockY, @Nullable MapChunk preChunk, Result result, MapColor mapColor) {
        MapColor.Brightness brightness;

        if (mapColor == MapColor.WATER) {
            double heightDiff = result.waterHeight * 0.1D + (blockX + blockY & 1) * 0.2D;

            if (heightDiff < 0.5D) {
                brightness = MapColor.Brightness.HIGH;
            } else if (heightDiff > 0.9D) {
                brightness = MapColor.Brightness.LOW;
            } else {
                brightness = MapColor.Brightness.NORMAL;
            }
        } else {
            int lastHeightIdx = blockX * CHUNK_BLOCK_COUNT + blockY - 1;
            short lastHeight;

            if (blockY - 1 >= 0) {
                lastHeight = heightMap[lastHeightIdx];
            } else if (preChunk != null) {
                lastHeight = preChunk.heightMap[blockX * CHUNK_BLOCK_COUNT + CHUNK_BLOCK_COUNT - 1];
            } else {
                lastHeight = 0;
            }

            double heightDiff = (result.currY - lastHeight) * 4.0D / 5D + ((blockX + blockY & 1) - 0.5D) * 0.4D;

            if (heightDiff > 0.6D) {
                brightness = MapColor.Brightness.HIGH;
            } else if (heightDiff < -0.6D) {
                brightness = MapColor.Brightness.LOW;
            } else {
                brightness = MapColor.Brightness.NORMAL;
            }
        }

        return brightness;
    }

    @NotNull
    private static Result getSinglePoint(LevelAccessor level, BlockPos.MutableBlockPos mutableBlockPos,
                                         BlockPos.MutableBlockPos mutableBlockPos1, ChunkAccess chunkAccess, int currY) {
        BlockState blockstate;

        int waterHeight = 0;

        if (currY <= level.getMinBuildHeight() + 1) {
            blockstate = Blocks.BEDROCK.defaultBlockState();
        } else {
            do {
                currY--;
                mutableBlockPos.setY(currY);
                blockstate = chunkAccess.getBlockState(mutableBlockPos);
            } while (blockstate.getMapColor(level, mutableBlockPos) == MapColor.NONE && currY > level.getMinBuildHeight());

            if (currY > level.getMinBuildHeight() && !blockstate.getFluidState().isEmpty()) {
                int reduceY = currY - 1;
                mutableBlockPos1.set(mutableBlockPos);

                BlockState blockstate1;
                do {
                    mutableBlockPos1.setY(reduceY--);
                    blockstate1 = chunkAccess.getBlockState(mutableBlockPos1);
                    waterHeight++;
                } while (reduceY > level.getMinBuildHeight() && !blockstate1.getFluidState().isEmpty());

                FluidState fluidstate = blockstate.getFluidState();
                blockstate = !fluidstate.isEmpty() && !blockstate.isFaceSturdy(level, mutableBlockPos, Direction.UP)
                        ? fluidstate.createLegacyBlock()
                        : blockstate;
            }
        }

        return new Result(currY, blockstate, waterHeight);
    }

    private record Result(int currY, BlockState blockstate, int waterHeight) {
    }
}
