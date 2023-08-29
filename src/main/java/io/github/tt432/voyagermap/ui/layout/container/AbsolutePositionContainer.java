package io.github.tt432.voyagermap.ui.layout.container;

/**
 * Adjust the positions of the subviews based on offsetting the actual value of the container by the default value of each subview.
 *
 * @author TT432
 */
public class AbsolutePositionContainer extends Container {
    @Override
    public void layoutChildren() {
        children.forEach(view -> view.actualPos.setValue(view.defaultPos, actualPos));
    }
}
