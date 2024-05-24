package me.paypur.recipetree;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class RecipeTreeWrapper {

    private final RecipeNode ROOT_NODE;
    private List<RecipeNode> LEAF_NODES = new ArrayList<>();
    private final RecipeIngredientRole ingredientRole;
    private int TREE_DEPTH;

    public RecipeTreeWrapper(RecipeNode recipeNode, RecipeIngredientRole recipeIngredientRole) {
        this.ROOT_NODE = recipeNode;
        LEAF_NODES.add(recipeNode);
        this.ingredientRole = recipeIngredientRole;
        this.TREE_DEPTH = 0;
        buildTree();
    }

    public RecipeTreeWrapper(RecipeNode recipeNode, RecipeIngredientRole recipeIngredientRole, int treeDepth) {
        this.ROOT_NODE = recipeNode;
        LEAF_NODES.add(recipeNode);
        this.ingredientRole = recipeIngredientRole;
        this.TREE_DEPTH = treeDepth;
        buildTree();
    }

    public int getTreeDepth() {
        return TREE_DEPTH;
    }

    public void increaseTreeDepth() {
        this.TREE_DEPTH += 1;
        updateTree();
    }


    private void buildTree() {
        int depth = 0;
        while (depth < TREE_DEPTH) {
            List<RecipeNode> newLeafNodes = new ArrayList<>();
            for (RecipeNode leaf : LEAF_NODES) {
                addChildren(leaf);
                newLeafNodes.addAll(leaf.getChildren());
            }
            LEAF_NODES = newLeafNodes;
            depth++;
        }
    }

    private void updateTree() {
        // TODO: need to update tree
        for (RecipeNode leaf : LEAF_NODES) {

        }
    }

    private void addChildren(RecipeNode parentNode) {
        if (parentNode.getItems().isEmpty()) {
            return;
        }

        for (ItemStack item : parentNode.getItems()) {
            IFocus<ItemStack> focus = JeiHelper.jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(ingredientRole, VanillaTypes.ITEM, item);
            IRecipeManager recipeManager = JeiHelper.jeiRuntime.getRecipeManager();
            Stream<RecipeType<?>> allRecipeTypes = recipeManager.createRecipeCategoryLookup().get().map(IRecipeCategory::getRecipeType);
            List<?> recipes = allRecipeTypes.flatMap(recipeType -> recipeManager.createRecipeLookup(recipeType).limitFocus(List.of(focus)).get()).distinct().toList();
            switch (ingredientRole) {
                case INPUT -> addOutputs(recipes, parentNode);
                case OUTPUT -> addInputs(recipes, parentNode);
            }
        }
    }

    private void addOutputs(List<?> recipes, RecipeNode parentNode) {
        for (Object object : recipes) {
            RecipeNode childNode;
            if (object instanceof Recipe<?> recipe) {
                // TODO: does not work for upgrade recipes
                childNode = new RecipeNode(recipe.getResultItem(), parentNode);
            } else if (object instanceof IJeiBrewingRecipe recipe) {
                childNode = new RecipeNode(recipe.getPotionOutput(), parentNode);
            // cases where there is no output or cycle
            } else if (object instanceof IJeiAnvilRecipe || object instanceof IJeiCompostingRecipe || object instanceof IJeiFuelingRecipe) {
                return;
            } else {
                System.err.println("Unhandled recipe " + object);
                return;
            }
            if (!childNode.getItems().isEmpty()) {
                parentNode.addChild(childNode);
            }
        }
    }

    private void addInputs(List<?> recipes, RecipeNode parentNode) {
        for (Object object : recipes) {
            RecipeNode childNode;
            if (object instanceof Recipe<?> recipe) {
                // TODO: does not work for upgrade recipes
                childNode = new RecipeNode(recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getItems().length != 0)
                        // TODO: only gets first tag entry I think
                        .map(ingredient -> ingredient.getItems()[0])
                        .distinct()
                        .toList(), parentNode);
            } else if (object instanceof IJeiBrewingRecipe recipe) {
                childNode = new RecipeNode(Stream.of(recipe.getPotionInputs(), recipe.getIngredients()).flatMap(Collection::stream).toList(), parentNode);
            // cases where there is no output
            } else if (object instanceof IJeiAnvilRecipe || object instanceof IJeiCompostingRecipe || object instanceof IJeiFuelingRecipe) {
                return;
            } else {
                System.err.println("Unhandled recipe " + object);
                return;
            }
            if (!childNode.getItems().isEmpty()) {
                parentNode.addChild(childNode);
            }
        }
    }

    @Override
    public String toString() {
        return ROOT_NODE.toString();
    }

}
