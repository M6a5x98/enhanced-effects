package m6a5x98.enhancedeffects;

import m6a5x98.enhancedeffects.component.ModComponents;
import m6a5x98.enhancedeffects.component.components.MergeFromComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnhancedEffectsUtil {
    public static boolean isFood(ItemStack stack) {
        return stack.getComponents().has(DataComponents.FOOD);
    }

    public static <T> Optional<T> getComponent(ItemStack stack, DataComponentType<T> componentType) {
        T component = stack.get(componentType);
        return Optional.ofNullable(component);
    }

    public static List<MobEffectInstance> getPotionEffects(ItemStack stack) {
        List<MobEffectInstance> effects = new ArrayList<>();
        FoodProperties foodComponent = stack.getComponents().get(DataComponents.FOOD);
        PotionContents potionContentsComponent = stack.getComponents().get(DataComponents.POTION_CONTENTS);
        if (foodComponent != null) {
            for (FoodProperties.PossibleEffect effect : foodComponent.effects()) {
                effects.add(effect.effect());
            }
        }
        if (potionContentsComponent != null && potionContentsComponent.hasEffects()) {
            potionContentsComponent.getAllEffects().forEach(effects::add);
        }
        effects.addAll(getSusStewEffects(stack));
        return effects;
    }

    public static List<MobEffectInstance> getSusStewEffects(ItemStack stack) {
        SuspiciousStewEffects suspiciousStewEffectsComponent = stack.getComponents().get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (suspiciousStewEffectsComponent == null) return List.of();
        if (suspiciousStewEffectsComponent.effects().isEmpty()) return List.of();
        List<MobEffectInstance> result = new ArrayList<>();
        suspiciousStewEffectsComponent.effects().forEach(e -> result.add(e.createEffectInstance()));
        return result;
    }

    public static boolean isMergeFromComponentEmpty(ItemStack stack) {
        List<MergeFromComponent.MergeFrom> mergeFromComponent = stack.get(ModComponents.MERGE_FROM_COMPONENT);
        return mergeFromComponent == null || mergeFromComponent.isEmpty();
    }

    public static int getPotionColor(ItemStack stack) {
        Optional<?> Component = getComponent(stack, DataComponents.POTION_CONTENTS);
        if (Component.isPresent()) {
            PotionContents component = ((PotionContents) Component.get());
            return component.getColor();
        }
        return CommonColors.LIGHTER_GRAY;
    }
}
