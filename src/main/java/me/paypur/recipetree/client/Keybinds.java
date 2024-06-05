package me.paypur.recipetree.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keybinds {
    public static final KeyMapping RecipeTreeUses = new KeyMapping(
                "key.recipetree.uses",
                KeyConflictContext.GUI,
                InputConstants.getKey(InputConstants.KEY_I, -1),
                "key.categories.recipetree"
    );

    public static final KeyMapping RecipeTreeRecipes = new KeyMapping(
                "key.recipetree.recipes",
                KeyConflictContext.GUI,
                InputConstants.getKey(InputConstants.KEY_T, -1),
                "key.categories.recipetree"
    );

    public static void init() {
        ClientRegistry.registerKeyBinding(RecipeTreeUses);
        ClientRegistry.registerKeyBinding(RecipeTreeRecipes);
    }

}
