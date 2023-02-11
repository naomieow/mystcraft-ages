package net.mystcraftages.items;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public class LinkingBookItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String TAG_LINK_POS = "LinkedPos";
    public static final String TAG_LINK_DIM = "LinkedDim";

    public LinkingBookItem(Properties properties) {
        super(properties);
    }

    public static boolean isLinkedBook(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null && (compoundTag.contains("LinkedPos") || compoundTag.contains("LinkedDim"));
    }

    private static Optional<ResourceKey<Level>> getLinkedDimension(CompoundTag compoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("LinkedDim")).result();
    }

    @Nullable
    public static GlobalPos getLinkedPosition(CompoundTag compoundTag) {
        boolean bl = compoundTag.contains("LinkedPos");
        boolean bl2 = compoundTag.contains("LinkedDim");
        if (bl && bl2) {
            Optional<ResourceKey<Level>> optional = getLinkedDimension(compoundTag);
            if (optional.isPresent()) {
                BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound("LinkedPos"));
                return GlobalPos.of(optional.get(), blockPos);
            }
        }
        return null;
    }

    private void addLinkedTags(ItemStack itemStack, Player player, Level level) {
        CompoundTag compoundTag = new CompoundTag();
        ResourceKey<Level> resourceKey = level.dimension();
        compoundTag.put("LinkedPos", NbtUtils.writeBlockPos(player.blockPosition()));
        compoundTag.putString("LinkedDim", resourceKey.location().toString());
        compoundTag.putInt("CustomModelData", 99976843);
        itemStack.setTag(compoundTag);

//        player.sendSystemMessage(Component.literal(player.blockPosition().toString()));
//        player.sendSystemMessage(Component.literal(resourceKey.location().toString()));
//        player.sendSystemMessage(Component.literal(
//                itemStack.getOrCreateTag().getAsString()
//        ));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (isLinkedBook(itemStack)) {
                player.sendSystemMessage(Component.literal("Teleport!"));
            } else {
//                player.sendSystemMessage(Component.literal("Linked Book!"));
                this.addLinkedTags(itemStack, player, level);
            }
            player.getCooldowns().addCooldown(this, 50);
        }
        return super.use(level, player, hand);
    }
}
