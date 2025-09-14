package m6a5x98.enhancedeffects.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.component.ModComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class GivenEffectsComponent {
    private static final Codec<GivenEffects> _GIVEN_EFFECTS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("effect").forGetter(GivenEffects::effect),
            Codec.BYTE.fieldOf("amplifier").forGetter(GivenEffects::amplifier),
            Codec.INT.fieldOf("duration").forGetter(GivenEffects::duration)
    ).apply(instance, GivenEffects::new));
    public static final Codec<List<GivenEffects>> GIVEN_EFFECTS_CODEC = _GIVEN_EFFECTS_CODEC.listOf();

    public static void consume(Level world, LivingEntity user, ItemStack stack) {
        List<GivenEffectsComponent.GivenEffects> effects = stack.get(ModComponents.GIVEN_EFFECTS_COMPONENT);
        if (effects != null && EnhancedEffectsUtil.isFood(stack)) {
            for (GivenEffects effect : effects) {
                String effectID = effect.effect;
                int effectDuration = effect.duration;
                int effectAmplifier = effect.amplifier;
                if (!world.isClientSide) {
                    Optional<Holder.Reference<MobEffect>> statusEffect = BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(effectID));
                    if (statusEffect.isEmpty()) continue;
                    user.addEffect(new MobEffectInstance(statusEffect.get(), effectDuration, effectAmplifier));
                }
            }
        }
    }

    public record GivenEffects(String effect, byte amplifier, int duration) {
    }
}
