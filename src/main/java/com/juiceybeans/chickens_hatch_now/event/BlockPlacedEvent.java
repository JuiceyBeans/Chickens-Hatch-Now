package com.juiceybeans.chickens_hatch_now.event;

import com.juiceybeans.chickens_hatch_now.ChickensHatchNow;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.juiceybeans.chickens_hatch_now.block.ChickenEggBlock.EGGS;

@EventBusSubscriber(modid = ChickensHatchNow.MOD_ID)
public class BlockPlacedEvent {

    @SubscribeEvent
    public static void onEggPlaced(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        RandomSource pRandom = RandomSource.create();
        BlockPos placePos = pos.offset(event.getFace().getUnitVec3i());
        BlockState placeState = level.getBlockState(placePos);

        if (player.isCrouching() && itemStack.is(ModTags.CHICKEN_EGG_ITEMS)) {
            Block variant = Reference.EGG_ITEM_TO_BLOCK.get(itemStack.getItem()).get();

            if (state.is(ModTags.CHICKEN_EGG_BLOCKS)) {
                if (state.getValue(EGGS) < 4) {
                    level.setBlock(pos, getEggBlockState(event, pos, variant), ChickenEggBlock.UPDATE_ALL);
                } else if (!placeState.is(ModTags.CHICKEN_EGG_BLOCKS) || placeState.getValue(EGGS) != 4) {
                    level.setBlock(placePos, getEggBlockState(event, placePos, variant), ChickenEggBlock.UPDATE_ALL);
                } else {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.FAIL);
                    return;
                }
            } else {
                level.setBlock(placePos, getEggBlockState(event, pos, variant), ChickenEggBlock.UPDATE_ALL);
            }

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.7F,
                    0.9F + pRandom.nextFloat() * 0.2F);
            player.awardStat(Stats.ITEM_USED.get(Items.EGG));

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    public static BlockState getEggBlockState(PlayerInteractEvent.RightClickBlock event, BlockPos pos, Block block) {
        BlockState state = event.getLevel().getBlockState(pos);

        // Increase egg count
        if (state.is(ModTags.CHICKEN_EGG_BLOCKS)) {
            if (state.getValue(EGGS) < 4) {
                return state.setValue(EGGS, Math.min(4, state.getValue(EGGS) + 1));
            } else {
                return state.setValue(EGGS, 1);
            }
        } else { // Place new egg
            return block.getStateForPlacement(
                    new BlockPlaceContext(event.getLevel(), event.getEntity(), event.getHand(),
                            event.getItemStack(), event.getHitVec()));
        }
    }
}
