package net.mystcraftages.fabric;

import net.mystcraftages.MystcraftAges;
import net.fabricmc.api.ModInitializer;

public class MystcraftAgesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MystcraftAges.init();
    }
}
