package me.paypur.recipetree;

import com.mojang.logging.LogUtils;
import me.paypur.recipetree.client.Keybinds;
import me.paypur.recipetree.gui.RecipeTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


import java.util.logging.Logger;

import static me.paypur.recipetree.RecipeTree.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class RecipeTree {
    public static final String MOD_ID = "recipetree";

    @Mod.EventBusSubscriber(modid = RecipeTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public final class ClientModHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            Keybinds.init();
        }
    }

    @Mod.EventBusSubscriber(modid = RecipeTree.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public final class ClientForgeHandler {
        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (Keybinds.RecipeTreeUses.consumeClick()) {
                Minecraft minecraft = Minecraft.getInstance();
                ItemStack item = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);

                minecraft.player.displayClientMessage(new TextComponent(item.toString()), false);

                minecraft.setScreen(new RecipeTreeScreen(item));
            }
        }
    }

}
