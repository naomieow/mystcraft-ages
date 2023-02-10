package net.mystcraftages.items;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.mystcraftages.MystcraftAges;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
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

    private void addLinkedTags(ResourceKey<Level> resourceKey, BlockPos blockPos, CompoundTag compoundTag) {
        compoundTag.put("LinkedPos", NbtUtils.writeBlockPos(blockPos));
//        DataResult dataResult = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, resourceKey);
//        Logger logger = LOGGER;
//        Objects.requireNonNull(logger);
//        dataResult.resultOrPartial(logger::error).ifPresent((tag) ->
//                compoundTag.put("LinkedDim", tag));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            ItemStack itemStack = player.getUseItem();
            player.sendSystemMessage(Component.literal("Used Linking Book!"));
            player.getCooldowns().addCooldown(this, 50);
            boolean bl = !player.getAbilities().instabuild && itemStack.getCount() == 1;
            if (bl) {
                this.addLinkedTags(level.dimension(), player.blockPosition(), itemStack.getOrCreateTag());
            } else {
                ItemStack itemStack2 = new ItemStack(itemStack.getItem(), 1);
                CompoundTag compoundTag = itemStack.hasTag() ? itemStack.getTag().copy() : new CompoundTag();
                itemStack2.setTag(compoundTag);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                this.addLinkedTags(level.dimension(), player.blockPosition(), compoundTag);
                if (!player.getInventory().add(itemStack2)) {
                    player.drop(itemStack2, false);
                }
            }
        }
        return super.use(level, player, hand);
    }
}
