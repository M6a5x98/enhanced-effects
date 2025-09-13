package m6a5x98.enhancedeffects.client;

import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.components.components.GivenEffectsComponent;
import m6a5x98.enhancedeffects.components.components.MergeFromComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class EnhancedEffectsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (stack.get(ModComponents.MERGE_FROM_COMPONENT) != null &&
                    !stack.get(ModComponents.MERGE_FROM_COMPONENT).isEmpty()) {
                EnhancedEffectsUtil.getComponent(stack, ModComponents.MERGE_FROM_COMPONENT)
                        .ifPresent(e -> {
                            if (e instanceof List<?> list) {
                                for (Object obj : list) {
                                    if (obj instanceof MergeFromComponent.MergeFrom(
                                            String extractedFrom, String translationKey
                                    )) {
                                        String translationParameter = extractedFrom.isEmpty()
                                                ? "" : extractedFrom;
                                        lines.add(1, Text.translatable(
                                                translationKey,
                                                Text.translatable(translationParameter)
                                        ));
                                    }
                                }
                            }
                        });
            }
            if (stack.get(ModComponents.GIVEN_EFFECTS_COMPONENT) != null &&
                    !stack.get(ModComponents.GIVEN_EFFECTS_COMPONENT).isEmpty() &&
                    EnhancedEffectsUtil.isFood(stack) &&
                    player != null) {
                for (GivenEffectsComponent.GivenEffects effect : stack.get(ModComponents.GIVEN_EFFECTS_COMPONENT)) {
                    int effectID = effect.effect();
                    int effectAmplifier = effect.amplifier();
                    String effectLevel = Text.translatableWithFallback(
                            "potion.potency." + effectAmplifier,
                            String.valueOf(effectAmplifier)
                    ).getString();
                    String effectName;
                    try {
                        effectName = Text.translatable(
                                Registries.STATUS_EFFECT.get(effectID).getTranslationKey()
                        ).getString();
                    } catch (NullPointerException e) {
                        try {
                            effectName = Registries.STATUS_EFFECT.get(effectID).getName().getString();
                        } catch (NullPointerException ex) {
                            effectName = Registries.STATUS_EFFECT.getId(
                                    Registries.STATUS_EFFECT.get(effectID)
                            ).getPath();
                        }
                    }
                    String effectString = effectName + " " + effectLevel;
                    int color = 0xFFFFFF; // default white
                    try {
                        color = Registries.STATUS_EFFECT.get(effectID).getColor();
                    } catch (NullPointerException ignored) {}

                    lines.add(Text.literal(effectString).setStyle(Style.EMPTY.withColor(color)));
                }
            }
        });

    }
}
