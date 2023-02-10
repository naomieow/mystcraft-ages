package net.mystcraftages.forge;

import net.mystcraftages.MystcraftAgesExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class MystcraftAgesExpectPlatformImpl {
    /**
     * This is our actual method to {@link MystcraftAgesExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
