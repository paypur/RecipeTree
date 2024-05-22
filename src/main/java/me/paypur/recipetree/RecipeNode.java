package me.paypur.recipetree;

import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeNode {

    private List<ItemStack> items;
    private final int depth;
    private final RecipeNode parent;

    private final List<RecipeNode> children = new ArrayList<>();
    public RecipeNode(ItemStack items, RecipeNode parent) {
        this(items, 0, parent);
    }
    public RecipeNode(ItemStack items, int depth, RecipeNode parent) {
        this(List.of(items), depth, parent);
    }
    public RecipeNode(List<ItemStack> items, int depth, RecipeNode parent) {
        List<ItemStack> deduplicatedItems = new ArrayList<>(items);
        RecipeNode ancestor = parent;
        while (ancestor != null) {
            deduplicatedItems = removeItems(deduplicatedItems, ancestor.getItems());
            ancestor = ancestor.getParent();
        }
        this.items = deduplicatedItems;
        this.depth = depth;
        this.parent = parent;
    }

    private List<ItemStack> removeItems(List<ItemStack> itemStacks, List<ItemStack> removeStacks) {
        List<ItemStack> deduplicatedItems = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            for (ItemStack removeStack : removeStacks) {
                if (!itemStack.getItem().equals(removeStack.getItem())) {
                    deduplicatedItems.add(itemStack);
                }
            }
        }
        return deduplicatedItems;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public RecipeNode getParent() {
        return parent;
    }

    public List<RecipeNode> getChildren() {
        return children;
    }

    public void addChild(RecipeNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        // ├─ │ ─ └─
        // TODO: has a few issues still
        return String.format(items.toString() + children.stream().map(child -> "\n" + "│   ".repeat(depth) + "├─── " + child.toString()).collect(Collectors.joining("")));
    }
}
