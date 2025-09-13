package m6a5x98.enhancedeffects.mixin;

import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.components.components.GivenEffectsComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FoodComponent.class)
public class ItemConsumptionMixin {
    @Inject(at = @At("TAIL"), method = "onConsume")
    private void applyFoodEffects(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo ci) {
        if (user.getWorld().isClient) return;
        GivenEffectsComponent.consume(user.getWorld(), user, stack);
    }
}