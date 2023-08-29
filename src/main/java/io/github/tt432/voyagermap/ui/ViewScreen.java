package io.github.tt432.voyagermap.ui;

import io.github.tt432.voyagermap.ui.layout.View;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * @author TT432
 */
public class ViewScreen extends Screen {
    View toplevelView;

    final MouseHandler mouseHandler = new MouseHandler();

    protected ViewScreen(View toplevelView) {
        super(Component.empty());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int p_281550_, int p_282878_, float p_282465_) {
        mouseHandler.update();
        toplevelView.updateAll();
    }
}
