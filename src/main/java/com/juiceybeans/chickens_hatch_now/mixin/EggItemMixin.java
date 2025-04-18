package com.juiceybeans.chickens_hatch_now.mixin;

import com.juiceybeans.chickens_hatch_now.block.ChickenEggBlock;
import com.juiceybeans.chickens_hatch_now.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug
@Mixin(EggItem.class)
public class EggItemMixin extends Item {
    public EggItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isClientSide()Z"))
    private void cancelIfSneaking(Level pLevel, Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (pPlayer.isCrouching()) {
            cir.cancel();
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = level.getBlockState(pPos);
        Player pPlayer = pContext.getPlayer();
        ItemStack itemstack = pPlayer.getItemInHand(pContext.getHand());
        int initialCount = itemstack.getCount();

        if (pPlayer.isCrouching()) {
            if (pState.is(ModBlocks.CHICKEN_EGG.get()) && pState.getValue(ChickenEggBlock.EGGS) < 4) {
                int heldEggs = pState.getValue(ChickenEggBlock.EGGS);
                BlockState newState = pState.setValue(ChickenEggBlock.EGGS, heldEggs + 1);

                level.setBlock(pPos, newState, ChickenEggBlock.UPDATE_ALL);
            } else {
                level.setBlock(pPos.above(), ChickenEggBlock.getStateForMixin(new BlockPlaceContext(pContext)), 0);
            }
            itemstack.setCount(initialCount - 1);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }



}