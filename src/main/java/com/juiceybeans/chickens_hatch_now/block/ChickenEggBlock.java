package com.juiceybeans.chickens_hatch_now.block;

import com.juiceybeans.chickens_hatch_now.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

public class ChickenEggBlock extends Block {
    private static final VoxelShape ONE_EGG_AABB = Block.box(3.0D, 0.0D, 3.0D,
            12.0D, 7.0D, 12.0D);
    private static final VoxelShape MULTIPLE_EGGS_AABB = Block.box(1.0D, 0.0D, 1.0D,
            15.0D, 7.0D, 15.0D);
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    public ChickenEggBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HATCH, Integer.valueOf(0))
                .setValue(EGGS, Integer.valueOf(1)));
    }

    @Override
    public Item asItem() {
        return Items.EGG;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.isSteppingCarefully()) {
            this.destroyEgg(pLevel, pState, pPos, pEntity, 100);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (!(pEntity instanceof Zombie)) {
            this.destroyEgg(pLevel, pState, pPos, pEntity, 3);
        }

        super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
    }

    private void destroyEgg(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, int pChance) {
        if (this.canDestroyEgg(pLevel, pEntity)) {
            if (!pLevel.isClientSide && pLevel.random.nextInt(pChance) == 0 && pState.is(ModBlocks.CHICKEN_EGG.get())) {
                this.decreaseEggs(pLevel, pPos, pState);
            }

        }
    }

    private void decreaseEggs(Level pLevel, BlockPos pPos, BlockState pState) {
        pLevel.playSound((Player)null, pPos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS,
                0.7F, 0.9F + pLevel.random.nextFloat() * 0.2F);
        int i = pState.getValue(EGGS);
        if (i <= 1) {
            pLevel.destroyBlock(pPos, false);
        } else {
            pLevel.setBlock(pPos, pState.setValue(EGGS, Integer.valueOf(i - 1)), 2);
            pLevel.gameEvent(GameEvent.BLOCK_DESTROY, pPos, GameEvent.Context.of(pState));
            pLevel.levelEvent(2001, pPos, Block.getId(pState));
        }

    }

    public static boolean onHay(BlockGetter pLevel, BlockPos pPos) {
        return isHay(pLevel, pPos.below());
    }

    public static boolean isHay(BlockGetter pReader, BlockPos pPos) {
        return pReader.getBlockState(pPos).is(Blocks.HAY_BLOCK);
    }


    public int getHatchLevel(BlockState pState) {
        return pState.getValue(HATCH);
    }

    private boolean isReadyToHatch(BlockState pState) {
        return this.getHatchLevel(pState) == 2;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!this.isReadyToHatch(pState)) {
            pLevel.playSound(null, pPos, SoundEvents.SNIFFER_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
            pLevel.setBlock(pPos, pState.setValue(HATCH, Integer.valueOf(this.getHatchLevel(pState) + 1)), 2);
        } else {
            pLevel.playSound(null, pPos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
            pLevel.destroyBlock(pPos, false);

            for(int j = 0; j < pState.getValue(EGGS); ++j) {
                Chicken chicken = EntityType.CHICKEN.create(pLevel);
                if (chicken != null) {
                    chicken.setAge(-24000);
                    chicken.moveTo(pPos.getX(), pPos.getY(), pPos.getZ());
                    pLevel.addFreshEntity(chicken);
                }
            }
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (onHay(pLevel, pPos) && !pLevel.isClientSide) {
            pLevel.levelEvent(2005, pPos, 0);
        }

        pLevel.scheduleTick(pPos, this, (Config.hatchProgressUpdate * 20));
    }

    /**
     * Called after a player has successfully harvested this block. This method will only be called if the player has
     * used the correct tool and drops should be spawned.
     */
    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe,
                              ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
        this.decreaseEggs(pLevel, pPos, pState);
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().is(this.asItem()) &&
                pState.getValue(EGGS) < 4 || super.canBeReplaced(pState, pUseContext);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(EGGS,
                Integer.valueOf(Math.min(4, blockstate.getValue(EGGS) + 1))) : super.getStateForPlacement(pContext);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(EGGS) > 1 ? MULTIPLE_EGGS_AABB : ONE_EGG_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HATCH, EGGS);
    }

    private boolean canDestroyEgg(Level pLevel, Entity pEntity) {
        if (!(pEntity instanceof Chicken) && !(pEntity instanceof Bat)) {
            if (!(pEntity instanceof LivingEntity)) {
                return false;
            } else {
                return pEntity instanceof Player || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(pLevel, pEntity);
            }
        } else {
            return false;
        }
    }

    public static BlockState getStateForEvent(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        return state.is(ModBlocks.CHICKEN_EGG.get()) ?
                state.setValue(EGGS, Math.min(4, state.getValue(EGGS) + 1)) :
                ModBlocks.CHICKEN_EGG.get().getStateForPlacement(
                        new BlockPlaceContext(event.getLevel(), event.getEntity(), event.getHand(),
                                event.getItemStack(), event.getHitVec()));
    }
}