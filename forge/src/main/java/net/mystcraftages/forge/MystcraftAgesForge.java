package net.mystcraftages.forge;

import dev.architectury.platform.forge.EventBuses;
import net.mystcraftages.MystcraftAges;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MystcraftAges.MOD_ID)
public class MystcraftAgesForge {
    public MystcraftAgesForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MystcraftAges.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MystcraftAges.init();
    }
}
