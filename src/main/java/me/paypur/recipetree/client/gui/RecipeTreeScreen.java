package me.paypur.recipetree.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.recipetree.client.JeiHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IJeiCompostingRecipe;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static me.paypur.recipetree.RecipeTree.MOD_ID;


public class RecipeTreeScreen extends Screen {

    private static final Component TITLE = new TextComponent("Recipe Tree");
    private final int WIDTH = 128;
    private final int HEIGHT = 128;
    private final ItemStack BASE_ITEM;
    private final ResourceLocation BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/background.png");

    private String text = "";

    private List<CraftingRecipe> recipeStream = Collections.EMPTY_LIST;

    public RecipeTreeScreen(ItemStack item) {
        super(TITLE);
        this.BASE_ITEM = item;
    }

    @Override
    protected void init() {
        super.init();

        IRecipeManager recipeManager = JeiHelper.jeiRuntime.getRecipeManager();

        List<IFocus<ItemStack>> focus = List.of(JeiHelper.jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.INPUT, VanillaTypes.ITEM, BASE_ITEM));
        Stream<RecipeType<?>> allRecipeTypes = recipeManager.createRecipeCategoryLookup().get().map(IRecipeCategory::getRecipeType);


        List<?> uses = allRecipeTypes.flatMap(recipeType -> recipeManager.createRecipeLookup(recipeType).limitFocus(focus).get()).toList();


        for (Object use : uses) {
            if (use instanceof Recipe<?> recipe) {
                System.out.println(recipe.getIngredients().stream().map((ingredient -> Arrays.toString(ingredient.getItems()))).toList() + " -> " + recipe.getResultItem());
            } else if (use instanceof IJeiAnvilRecipe recipe) {
                System.out.println(recipe.getLeftInputs() + " + " +  recipe.getRightInputs() + " -> " + recipe.getOutputs());
            } else if (use instanceof IJeiBrewingRecipe recipe) {
                System.out.println(recipe.getPotionInputs() + " + " +  recipe.getIngredients() + " -> " + recipe.getPotionOutput());
            } else if (use instanceof IJeiCompostingRecipe recipe) {
                System.out.println(recipe.getInputs() + " -> " + recipe.getChance() );
            } else if (use instanceof IJeiFuelingRecipe recipe) {
                System.out.println(recipe.getInputs() + " -> " + recipe.getBurnTime());
            } else {
                System.err.println("Unhandled recipe " + use);
            }
        }


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

        int l = 0;
        drawString(pPoseStack, minecraft.font,  text, 0, 0, 0x3f2398);

        // renders all the widgets
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }



    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
