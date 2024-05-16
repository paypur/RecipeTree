package me.paypur.recipetree.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keybinds {
//    public static final Keybinds INSTANCE = new Keybinds();
    public static final KeyMapping RecipeTreeUses = new KeyMapping(
                "key.recipetree.uses",
                KeyConflictContext.GUI,
                InputConstants.getKey(InputConstants.KEY_U, -1),
                "key.categories.recipetree"
    );

//    private Keybinds() {}

    public static void init() {
        ClientRegistry.registerKeyBinding(RecipeTreeUses);
    }

}
