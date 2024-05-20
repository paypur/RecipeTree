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
        // TODO: need to update tree
    }


    // TODO: should probably be called build tree
    private void buildTree() {
        int depth = 0;
        while (depth <= TREE_DEPTH) {
            List<RecipeNode> newLeafNodes = new ArrayList<>();
            for (RecipeNode node : LEAF_NODES) {
                addUses(node, depth + 1);
                newLeafNodes.addAll(node.getChildren());
            }
            LEAF_NODES = newLeafNodes;
            depth++;
        }
    }

    private void updateTree() {

    }

    private void addUses(RecipeNode recipeNode, int depth) {
        if (recipeNode.getResult() == null) {
            return;
        }

        IFocus<ItemStack> focus = JeiHelper.jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(ingredientRole, VanillaTypes.ITEM, recipeNode.getResult());
        Stream<RecipeType<?>> allRecipeTypes = recipeManager.createRecipeCategoryLookup().get().map(IRecipeCategory::getRecipeType);
        List<?> uses = allRecipeTypes.flatMap(recipeType -> recipeManager.createRecipeLookup(recipeType).limitFocus(List.of(focus)).get()).distinct().toList();

        for (Object use : uses) {
            if (use instanceof Recipe<?> recipe) {
                recipeNode.addChild(new RecipeNode(recipe.getResultItem(), depth));
            } else if (use instanceof IJeiAnvilRecipe recipe) {
                // avoid cycles
                if (!recipe.getLeftInputs().get(0).getItem().equals(recipe.getOutputs().get(0).getItem())
                    && !recipe.getRightInputs().get(0).getItem().equals(recipe.getOutputs().get(0).getItem())) {
                    recipeNode.addChild(new RecipeNode(recipe.getOutputs().get(0), depth));
                }
            } else if (use instanceof IJeiBrewingRecipe recipe) {
                recipeNode.addChild(new RecipeNode(recipe.getPotionOutput(), depth));
            // cases where there is no output
            } else if (use instanceof IJeiCompostingRecipe recipe) {
            } else if (use instanceof IJeiFuelingRecipe recipe) {
            } else {
                System.err.println("Unhandled recipe " + use);
            }
        }

    }

    @Override
    public String toString() {
        return ROOT_NODE.toString();
//        ├─ │ ─ └─
    }

}
