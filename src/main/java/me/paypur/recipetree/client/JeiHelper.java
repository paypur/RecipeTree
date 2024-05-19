package me.paypur.recipetree.client;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

import static me.paypur.recipetree.RecipeTree.MOD_ID;

@JeiPlugin
public class JeiHelper implements IModPlugin  {

    public static IJeiRuntime jeiRuntime;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "helpme");
    }


    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JeiHelper.jeiRuntime = jeiRuntime;
    }
}
