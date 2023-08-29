package io.github.tt432.voyagermap.map;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author TT432
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class MapTest {
    @SubscribeEvent
    public static void onEvent(TickEvent.ClientTickEvent event) {
        if (true) return;
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_G)) {
                try {
                    BufferedImage image = new BufferedImage(16 * 16, 16 * 16, BufferedImage.TYPE_INT_RGB);

                    MapUpdateHandler.map.chunkMap.forEach((chunkPos, chunk) -> {
                        int chunkX = (int) (chunkPos >> 32);
                        int chunkY = (int) chunkPos.longValue();

                        if (chunkX >= 0 && chunkX < 16 && chunkY >= 0 && chunkY < 16) {
                            var data = chunk.colorMap;

                            for (int y = 0; y < 16; y++) {
                                for (int x = 0; x < 16; x++) {
                                    int indexIn = (x * 16 + y) * 3;
                                    var r = data[indexIn] & 255;
                                    var g = data[indexIn + 1] & 255;
                                    var b = data[indexIn + 2] & 255;
                                    int rgb = (r << 16) | (g << 8) | b;
                                    image.setRGB(chunkX * 16 + x, chunkY * 16 + y, rgb);
                                }
                            }
                        }
                    });

                    // 导出为PNG图片
                    ImageIO.write(image, "png", new File("output.png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
