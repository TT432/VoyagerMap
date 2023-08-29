package io.github.tt432.voyagermap.ui.layout;

import io.github.tt432.voyagermap.ui.MouseHandler;
import io.github.tt432.voyagermap.ui.layout.shape.ViewShape;

import java.util.List;

/**
 * @author TT432
 */
public class View {
    protected List<View> children;
    public ViewPos defaultPos;
    public ViewPos actualPos;
    public ViewShape shape;

    public final MouseHandler mouseHandler = new MouseHandler();

    public boolean toplevel;

    public void layoutChildren() {
        // need child impl this
    }

    /**
     * all for entry point!!
     */
    public void updateAll() {
        updateState();

        render();
    }

    public void updateState() {
        layoutChildren();

        if (toplevel) {
            mouseHandler.update();
        }
    }

    public void onMouse(float mouseX, float mouseY, int mouseEvent) {

    }

    public void render() {
        children.forEach(View::render);
    }
}
