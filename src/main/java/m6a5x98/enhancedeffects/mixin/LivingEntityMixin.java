package m6a5x98.enhancedeffects.mixin;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "applyFoodEffects", cancellable = true)
    private void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo info) {
        Item item = stack.getItem();
        if (item.isFood()) {
            // Vanilla behaviour
            for (Pair<StatusEffectInstance, Float> pair : item.getFoodComponent().getStatusEffects()) {
                if (!world.isClient && pair.getFirst() != null && world.random.nextFloat() < pair.getSecond()) {
                    targetEntity.addStatusEffect(new StatusEffectInstance(pair.getFirst()));
                }
            }
            // Nbt-based effects
            if (stack.getNbt() != null) {
                for (NbtElement effect : stack.getNbt().getList("GivenEffects", NbtElement.COMPOUND_TYPE)) {
                    int effectID = ((NbtCompound) effect).getInt("Effect");
                    int effectDuration = ((NbtCompound) effect).getInt("Duration");
                    int effectAmplifier = ((NbtCompound) effect).getByte("Amplifier");
                    if (!world.isClient) {
                        targetEntity.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.get(effectID), effectDuration, effectAmplifier));
                    }
                }
            } else info.cancel();
        }
    }
}