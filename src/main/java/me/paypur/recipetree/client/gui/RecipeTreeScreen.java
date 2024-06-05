package me.paypur.recipetree.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.recipetree.RecipeNode;
import me.paypur.recipetree.RecipeTreeWrapper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static me.paypur.recipetree.RecipeTreeMain.MOD_ID;


public class RecipeTreeScreen extends Screen {

    private static final Component TITLE = new TextComponent("Recipe Tree");
    private final int WIDTH = 128;
    private final int HEIGHT = 128;
    private final ItemStack BASE_ITEM;
    private final RecipeIngredientRole RECIPE_ROLE;
    private final ResourceLocation BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/background.png");
    private RecipeTreeWrapper recipeTree;
    private String recipeTreeString;

    private int lineOffset = 0;

    public RecipeTreeScreen(ItemStack item, RecipeIngredientRole recipeRole) {
        super(TITLE);
        this.BASE_ITEM = item;
        this.RECIPE_ROLE = recipeRole;
    }

    @Override
    protected void init() {
        super.init();

        recipeTree = new RecipeTreeWrapper(new RecipeNode(BASE_ITEM, null), RECIPE_ROLE, 1);
        recipeTreeString = recipeTree.toString();
        System.out.println(recipeTreeString);

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

        Minecraft minecraft = getMinecraft();

        int l = lineOffset * 10;

        for (String line : recipeTreeString.split("\n")) {
            drawString(pPoseStack, minecraft.font,  line, 0, l, 0x3f2398);
            l += 10;
        }

        // renders all the widgets
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        lineOffset += (int) pDelta;
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
