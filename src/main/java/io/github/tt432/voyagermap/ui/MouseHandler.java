package io.github.tt432.voyagermap.ui;

import net.minecraft.client.Minecraft;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author TT432
 */
public class MouseHandler {
    public boolean isDragging = false;

    public void update() {
        var window = Minecraft.getInstance().getWindow().getWindow();

        glfwSetMouseButtonCallback(window, (windowId, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                if (action == GLFW_PRESS) {
                    isDragging = true;
                    // 记录鼠标按下的初始位置
                } else if (action == GLFW_RELEASE) {
                    isDragging = false;
                    // 处理鼠标释放逻辑
                }
            }
        });

        glfwSetCursorPosCallback(window, (windowId, xpos, ypos) -> {
            if (isDragging) {
                // 执行拖拽逻辑
            }
        });

        glfwSetScrollCallback(window, (windowId, xoffset, yoffset) -> {
            // xoffset 和 yoffset 表示滚轮的滚动量
            // 在大多数情况下，yoffset 是你关心的（垂直滚动）
        });
    }
}
