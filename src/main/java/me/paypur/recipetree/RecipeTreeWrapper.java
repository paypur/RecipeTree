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
    static IRecipeManager recipeManager = JeiHelper.jeiRuntime.getRecipeManager();

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

    public void setTreeDepth(int treeDepth) {
        this.TREE_DEPTH = treeDepth > 0 ? treeDepth : this.TREE_DEPTH;
        updateTree();
    }


    private void buildTree() {
        int depth = 0;
        while (depth <= TREE_DEPTH) {
            List<RecipeNode> newLeafNodes = new ArrayList<>();
            for (RecipeNode node : LEAF_NODES) {
                addChildren(node, depth + 1);
                newLeafNodes.addAll(node.getChildren());
            }
            LEAF_NODES = newLeafNodes;
            depth++;
        }
    }

    private void updateTree() {
        // TODO: need to update tree
    }

    private void addChildren(RecipeNode recipeNode, int depth) {
        if (recipeNode.getItems().isEmpty()) {
            return;
        }

        for (ItemStack item : recipeNode.getItems()) {
            IFocus<ItemStack> focus = JeiHelper.jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(ingredientRole, VanillaTypes.ITEM, item);
            Stream<RecipeType<?>> allRecipeTypes = recipeManager.createRecipeCategoryLookup().get().map(IRecipeCategory::getRecipeType);
            List<?> recipes = allRecipeTypes.flatMap(recipeType -> recipeManager.createRecipeLookup(recipeType).limitFocus(List.of(focus)).get()).distinct().toList();

            if (ingredientRole.equals(RecipeIngredientRole.INPUT)) {
                addOutputs(recipeNode, recipes, depth);
            } else if (ingredientRole.equals(RecipeIngredientRole.OUTPUT)) {
                addInputs(recipeNode, recipes, depth);
            }
        }
    }

    private void addOutputs(RecipeNode recipeNode, List<?> recipes, int depth) {
        for (Object object : recipes) {
            if (object instanceof Recipe<?> recipe) {
                // TODO: does not work for upgrade recipes
                recipeNode.addChild(new RecipeNode(recipe.getResultItem(), depth));
            } else if (object instanceof IJeiBrewingRecipe recipe) {
                recipeNode.addChild(new RecipeNode(recipe.getPotionOutput(), depth));
            // cases where there is no output or cycle
            } else if (object instanceof IJeiAnvilRecipe || object instanceof IJeiCompostingRecipe || object instanceof IJeiFuelingRecipe) {
            } else {
                System.err.println("Unhandled recipe " + object);
            }
        }
    }

    private void addInputs(RecipeNode recipeNode, List<?> recipes, int depth) {
        for (Object object : recipes) {
            if (object instanceof Recipe<?> recipe) {
                // TODO: does not work for upgrade recipes
                recipeNode.addChild(new RecipeNode(recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getItems().length != 0)
                        // TODO: only gets first tag entry I think
                        .map(ingredient -> ingredient.getItems()[0])
                        .distinct()
                        .toList(), depth));
            } else if (object instanceof IJeiBrewingRecipe recipe) {
                recipeNode.addChild(new RecipeNode(Stream.of(recipe.getPotionInputs(), recipe.getIngredients()).flatMap(Collection::stream).toList(), depth));
            // cases where there is no output
            } else if (object instanceof IJeiAnvilRecipe || object instanceof IJeiCompostingRecipe || object instanceof IJeiFuelingRecipe) {
            } else {
                System.err.println("Unhandled recipe " + object);
            }
        }
    }

    @Override
    public String toString() {
        return ROOT_NODE.toString();
    }

}