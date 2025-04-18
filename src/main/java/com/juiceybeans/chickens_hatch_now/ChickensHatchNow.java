package com.juiceybeans.chickens_hatch_now;

import com.juiceybeans.chickens_hatch_now.block.ChickenEggBlock;
import com.juiceybeans.chickens_hatch_now.block.ModBlocks;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ChickensHatchNow.MOD_ID)
public class ChickensHatchNow {

    public static final String MOD_ID = "chickens_hatch_now";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final IEventBus bus = MinecraftForge.EVENT_BUS;

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public ChickensHatchNow() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(eventBus);

        bus.register(this);

        bus.addListener(ChickensHatchNow::onEggThrown);
        bus.addListener(ChickensHatchNow::onEggPlaced);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void onEggThrown(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof ThrownEgg) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEggPlaced(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        RandomSource pRandom = RandomSource.create();

        if (player.isCrouching() && itemStack.is(Items.EGG)) {
            BlockPos placePos = pos.offset(event.getFace().getNormal());
            if (state.is(ModBlocks.CHICKEN_EGG.get()) && state.getValue(ChickenEggBlock.EGGS) < 4) {
                int heldEggs = state.getValue(ChickenEggBlock.EGGS);
                BlockState newState = state.setValue(ChickenEggBlock.EGGS, heldEggs + 1);

                level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
                level.setBlock(pos, newState, ChickenEggBlock.UPDATE_ALL);
            } else {
                level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
                level.setBlock(placePos, ChickenEggBlock.getStateForEvent(event), ChickenEggBlock.UPDATE_ALL);
            }

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            player.awardStat(Stats.ITEM_USED.get(Items.EGG));
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

    }
}
