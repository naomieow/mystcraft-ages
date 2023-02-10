package net.mystcraftages;

import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class MystcraftAges {
    public static final String MOD_ID = "mystcraftages";

    // Registries
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
    
    // Creative Tabs
    public static final CreativeModeTab MYSTCRAFT_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "mystcraft_tab"), () ->
            new ItemStack(MystcraftAges.DESCRIPTIVE_BOOK.get()));

    // Items
    public static final RegistrySupplier<Item> LINKING_BOOK = ITEMS.register("linking_book", () ->
            new Item(new Item.Properties().tab(MystcraftAges.MYSTCRAFT_TAB)));
    public static final RegistrySupplier<Item> DESCRIPTIVE_BOOK = ITEMS.register("descriptive_book", () ->
            new Item(new Item.Properties().tab(MystcraftAges.MYSTCRAFT_TAB)));
    
    public static void init() {
        ITEMS.register();
        
        System.out.println(MystcraftAgesExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
