package me.paypur.recipetree;

import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class RecipeNode {

    private final ItemStack item;
    private final int depth;

    private final List<RecipeNode> children = new ArrayList<>();
    public RecipeNode(ItemStack item) {
        this.item = item;
        this.depth = 0;
    }
    public RecipeNode(ItemStack item, int depth) {
        this.item = item;
        this.depth = depth;
    }

    public ItemStack getResult() {
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
        return String.format(item.toString() + children.stream().map(child -> "\n" + "│   ".repeat(depth) + "├─── " + child.toString()).collect(Collectors.joining("")));
    }
}
