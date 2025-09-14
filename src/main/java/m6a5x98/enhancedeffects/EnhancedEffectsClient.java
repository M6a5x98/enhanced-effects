package m6a5x98.enhancedeffects;

import m6a5x98.enhancedeffects.component.ModComponents;
import m6a5x98.enhancedeffects.component.components.GivenEffectsComponent;
import m6a5x98.enhancedeffects.component.components.MergeFromComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;


@Mod(value = EnhancedEffects.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = EnhancedEffects.MOD_ID, value = Dist.CLIENT)
public class EnhancedEffectsClient {
    public EnhancedEffectsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        Player player = event.getEntity();
        if (stack.has(ModComponents.MERGE_FROM_COMPONENT)) {
            EnhancedEffectsUtil.getComponent(stack, ModComponents.MERGE_FROM_COMPONENT.get())
                    .ifPresent(e -> {
                        if (e instanceof List<?> list) {
                            for (Object obj : list) {
                                if (obj instanceof MergeFromComponent.MergeFrom(
                                        String extractedFrom, String translationKey
                                )) {
                                    String translationParameter = extractedFrom.isEmpty()
                                            ? "" : extractedFrom;
                                    tooltip.add(1, Component.translatable(
                                            translationKey,
                                            Component.translatable(translationParameter)
                                    ));
                                }
                            }
                        }
                    });
        }

        if (stack.has(ModComponents.GIVEN_EFFECTS_COMPONENT) && EnhancedEffectsUtil.isFood(stack) && player != null && player.getAbilities().instabuild) {
            for (GivenEffectsComponent.GivenEffects effectComponent : stack.get(ModComponents.GIVEN_EFFECTS_COMPONENT)) {
                String effectID = effectComponent.effect();
                int effectAmplifier = effectComponent.amplifier();
                String effectLevel = Component.translatableWithFallback(
                        "potion.potency." + effectAmplifier,
                        String.valueOf(effectAmplifier)
                ).getString();
                String effectName;
                MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(effectID));
                if (effect != null) {
                    effectName = effect.getDisplayName().getString();
                    int color = effect.getColor();
                    tooltip.add(Component.literal(effectName + " " + effectLevel).setStyle(Style.EMPTY.withColor(color)));
                }
            }
        }
    }
}
