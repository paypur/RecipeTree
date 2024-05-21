package me.paypur.recipetree;

import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeNode {

    private final List<ItemStack> item;
    private final int depth;

    private final List<RecipeNode> children = new ArrayList<>();
    public RecipeNode(ItemStack item) {
        this(item, 0);
    }
    public RecipeNode(ItemStack item, int depth) {
        this(List.of(item), depth);
    }
    public RecipeNode(List<ItemStack> item, int depth) {
        this.item = item;
        this.depth = depth;
    }

    public List<ItemStack> getItems() {
        return item;
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
        return String.format(item.toString() + children.stream().map(child -> "\n" + "│   ".repeat(depth) + "├─── " + child.toString()).collect(Collectors.joining("")));
    }
}
