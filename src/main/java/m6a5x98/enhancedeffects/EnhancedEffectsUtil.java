package m6a5x98.enhancedeffects;

import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.components.components.MergeFromComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.util.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnhancedEffectsUtil {
    public static boolean isFood(ItemStack stack) {
        return stack.getComponents().get(DataComponentTypes.FOOD) != null;
    }

    public static List<StatusEffectInstance> getPotionEffects(ItemStack stack) {
        List<StatusEffectInstance> effects = new ArrayList<>();
        ConsumableComponent consumableComponent = stack.getComponents().get(DataComponentTypes.CONSUMABLE);
        PotionContentsComponent potionContentsComponent = stack.getComponents().get(DataComponentTypes.POTION_CONTENTS);
        if (consumableComponent != null) {
            for (ConsumeEffect effect : consumableComponent.onConsumeEffects()) {
                if (effect instanceof ApplyEffectsConsumeEffect applyEffect) {
                    effects.addAll(applyEffect.effects());
                }
            }
        }
        if (potionContentsComponent != null && potionContentsComponent.hasEffects()) {
            potionContentsComponent.getEffects().forEach(effects::add);
        }
        effects.addAll(getSusStewEffects(stack));
        return effects;
    }

    public static List<StatusEffectInstance> getSusStewEffects(ItemStack stack) {
        SuspiciousStewEffectsComponent suspiciousStewEffectsComponent = stack.getComponents().get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
        if (suspiciousStewEffectsComponent == null) return List.of();
        if (suspiciousStewEffectsComponent.effects().isEmpty()) return List.of();
        List<StatusEffectInstance> result = new ArrayList<>();
        suspiciousStewEffectsComponent.effects().forEach(e -> result.add(e.createStatusEffectInstance()));
        return result;
    }

    public static boolean isMergeFromComponentEmpty(ItemStack stack) {
        List<MergeFromComponent.MergeFrom> mergeFromComponent = stack.copy().get(ModComponents.MERGE_FROM_COMPONENT);
        return mergeFromComponent == null || mergeFromComponent.isEmpty();
    }

    public static int getPotionColor(ItemStack stack) {
        Optional<?> Component = getComponent(stack, DataComponentTypes.POTION_CONTENTS);
        if (Component.isPresent()) {
            PotionContentsComponent component =  ((PotionContentsComponent) Component.get());
            return component.getColor(Colors.ALTERNATE_WHITE);
        }
        return Colors.ALTERNATE_WHITE;
    }

    public static Optional<?> getComponent(ItemStack stack, ComponentType<?> componentType) {
        if (stack.get(componentType) == null) return Optional.empty();
        else return Optional.of(stack.get(componentType));
    }

}
