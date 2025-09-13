package m6a5x98.enhancedeffects.components.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.components.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GivenEffectsComponent {
    public record GivenEffects(int effect, byte amplifier, int duration) {}
    public static final Codec<GivenEffects> GIVEN_EFFECTS_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("effect").forGetter(GivenEffects::effect),
            Codec.BYTE.fieldOf("amplifier").forGetter(GivenEffects::amplifier),
            Codec.INT.fieldOf("duration").forGetter(GivenEffects::duration)
    ).apply(instance, GivenEffects::new));
    public static final Codec<List<GivenEffects>> GIVEN_EFFECTS_LIST_CODEC = GIVEN_EFFECTS_CODEC.listOf();
    public static void consume(World world, LivingEntity user, ItemStack stack) {
        List<GivenEffectsComponent.GivenEffects> effects = stack.get(ModComponents.GIVEN_EFFECTS_COMPONENT);
        if (effects != null && EnhancedEffectsUtil.isFood(stack)) {
            for (GivenEffects effect : effects) {
                int effectID = effect.effect;
                int effectDuration = effect.duration;
                int effectAmplifier = effect.amplifier;
                if (!world.isClient) {
                    Optional<RegistryEntry.Reference<StatusEffect>> statusEffect = Registries.STATUS_EFFECT.getEntry(effectID);
                    statusEffect.ifPresent(statusEffectReference -> user.addStatusEffect(
                            new StatusEffectInstance(
                                    statusEffectReference,
                                    effectDuration,
                                    effectAmplifier
                            )
                    ));
                }
            }
        }
    }
}
