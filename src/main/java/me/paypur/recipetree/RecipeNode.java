package me.paypur.recipetree;

import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeNode {

    private List<ItemStack> items;
    private final RecipeNode parent;
    private final List<RecipeNode> children = new ArrayList<>();
    public RecipeNode(ItemStack items, RecipeNode parent) {
        this(List.of(items), parent);
    }
    public RecipeNode(List<ItemStack> items, RecipeNode parent) {
        List<ItemStack> deduplicatedItems = new ArrayList<>(items);
        RecipeNode ancestor = parent;
        while (ancestor != null) {
            deduplicatedItems = removeItems(deduplicatedItems, ancestor.getItems());
            ancestor = ancestor.getParent();
        }
        this.items = deduplicatedItems;
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

    public boolean isLast() {
        if (parent == null) {
            return false;
        }
        return this.equals(parent.getChildren().get(parent.getChildren().size() - 1));
    }

    public List<RecipeNode> getChildren() {
        return children;
    }

    public void addChild(RecipeNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return items.toString() + children.stream().map(child -> "\n" +
                Arrays.stream(child.toString().split("\n"))
                        .map(sub -> (child.isLast() ? (sub.contains("├") || sub.contains("└") ? "    " : "└───") : (sub.contains("├") || sub.contains("└") ? "│   " : "├───")) + sub)
                        .collect(Collectors.joining("\n")))
                .collect(Collectors.joining(""));
    }
}
