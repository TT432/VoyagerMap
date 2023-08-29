package io.github.tt432.voyagermap.ui.layout;

/**
 * @author TT432
 */
public class ViewPos {
    float x;
    float y;

    public void setValue(ViewPos defaultPos, ViewPos actualPos) {
        this.x = defaultPos.x + actualPos.x;
        this.y = defaultPos.y + actualPos.y;
    }
}
