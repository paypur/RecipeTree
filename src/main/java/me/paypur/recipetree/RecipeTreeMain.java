package me.paypur.recipetree;

import me.paypur.recipetree.client.JeiHelper;
import me.paypur.recipetree.client.Keybinds;
import me.paypur.recipetree.client.gui.RecipeTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static me.paypur.recipetree.RecipeTreeMain.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class RecipeTreeMain {
    public static final String MOD_ID = "recipetree";

    @Mod.EventBusSubscriber(modid = RecipeTreeMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public final class ClientModHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            Keybinds.init();
        }
    }

    @Mod.EventBusSubscriber(modid = RecipeTreeMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public final class ClientForgeHandler {
        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (Keybinds.RecipeTreeUses.consumeClick()) {
                if (JeiHelper.jeiRuntime == null) {
                    return;
                }

                Minecraft minecraft = Minecraft.getInstance();

//                ItemStack itemStack = Help.jeiRuntime.getRecipesGui().getIngredientUnderMouse(VanillaTypes.ITEM_STACK);

                ItemStack item = minecraft.player.getMainHandItem();
                minecraft.setScreen(new RecipeTreeScreen(item));
            }
        }
    }

}
