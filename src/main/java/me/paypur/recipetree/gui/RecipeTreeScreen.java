package me.paypur.recipetree.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.paypur.recipetree.RecipeTree.MOD_ID;


public class RecipeTreeScreen extends Screen {

    private static final Component TITLE = new TextComponent("Recipe Tree");
    private final int WIDTH = 128;
    private final int HEIGHT = 128;
    private final ItemStack BASE_ITEM;
    private final ResourceLocation BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/background.png");

    public RecipeTreeScreen(ItemStack item) {
        super(TITLE);

        this.BASE_ITEM = item;
    }

    @Override
    protected void init() {
        super.init();

        // calculate recipes

        // add widgets
        // net.minecraft.client.gui.components

//        addRenderableOnly();
//        addWidget();
//        addRenderableWidget(Button.builder());

    }


    @Override
    public void tick() {
        super.tick();


    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // render order matters!

        // makes background darker when screen is open
        this.renderBackground(pPoseStack);
        // TODO: where texture???
        this.blit(pPoseStack, 0, 0, 0, 0, 128, 128);

        // renders all the widgets
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
