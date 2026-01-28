package com.juiceybeans.chickens_hatch_now.mixin;

import com.juiceybeans.chickens_hatch_now.block.ChickenEggBlock;
import com.juiceybeans.chickens_hatch_now.tag.ModTags;
import com.juiceybeans.chickens_hatch_now.util.Reference;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;

import static com.juiceybeans.chickens_hatch_now.block.ChickenEggBlock.EGGS;

@Mixin(EggItem.class)
public abstract class EggItemMixin extends Item {

    public EggItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        Level level = context.getLevel();
        RandomSource random = RandomSource.create();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        BlockPos offsetPos = pos.relative(context.getClickedFace());
        BlockState offsetState = level.getBlockState(offsetPos);

        Block variantBlock = Reference.EGG_ITEM_TO_BLOCK.get(this.asItem()).get();

        // Check if clicked block is already an egg block
        if (state.is(ModTags.CHICKEN_EGG_BLOCKS)) {
            // Check if egg block has less than 4 eggs
            if (getEggCount(state) < 4) {
                level.setBlock(pos, state.setValue(EGGS, getEggCount(state) + 1), ChickenEggBlock.UPDATE_ALL);
            } else if (offsetState.is(ModTags.CHICKEN_EGG_BLOCKS) && getEggCount(offsetState) < 4) {
                // offset block is egg
                level.setBlock(offsetPos, offsetState.setValue(EGGS, getEggCount(offsetState) + 1),
                        ChickenEggBlock.UPDATE_ALL);
            } else if (!offsetState.is(ModTags.CHICKEN_EGG_BLOCKS) && offsetState.canBeReplaced()) {
                // offset block can be replaced
                level.setBlock(offsetPos, variantBlock.getStateForPlacement(placeContext), ChickenEggBlock.UPDATE_ALL);
            } else return InteractionResult.PASS; // fail
        } else { // If clicked block is not an egg
            level.setBlock(offsetPos, variantBlock.getStateForPlacement(placeContext),
                    ChickenEggBlock.UPDATE_ALL);
        }

        level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.7F,
                0.9F + random.nextFloat() * 0.2F);
        player.awardStat(Stats.ITEM_USED.get(Items.EGG));

        if (!player.isCreative()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    private static @NonNull Integer getEggCount(BlockState state) {
        return state.getValue(EGGS);
    }
}
