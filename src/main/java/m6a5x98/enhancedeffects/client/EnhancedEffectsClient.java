package m6a5x98.enhancedeffects.client;

import m6a5x98.enhancedeffects.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class EnhancedEffectsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(
                ModItems.catalyst, new Identifier("damage"),
                (itemStack, clientWorld, livingEntity, seed) ->
                        itemStack.getDamage() / 5.0f
        );
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!context.isAdvanced()) return;
            if (stack.getNbt() == null) return;
            stack.getNbt().getList("MergeFrom", NbtElement.COMPOUND_TYPE).forEach(potion -> {
                NbtCompound potionCompound = (NbtCompound) potion;
                String translationParameter = "";
                if (!potionCompound.getString("ExtractedFrom").isEmpty()) translationParameter = potionCompound.getString("ExtractedFrom");
                lines.add(1, Text.translatable(potionCompound.getString("TranslationKey"), Text.translatable(translationParameter)));
            });
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (
                    !stack.isFood() ||
                            stack.getNbt().getList("GivenEffects", NbtElement.COMPOUND_TYPE).isEmpty() ||
                            player == null || !player.getAbilities().creativeMode
            ) {
                return;
            }

            for (NbtElement effect : stack.getNbt().getList("GivenEffects", NbtElement.COMPOUND_TYPE)) {
                int effectID = ((NbtCompound) effect).getInt("Effect");
                int effectAmplifier = ((NbtCompound) effect).getByte("Amplifier");
                String effectLevel = Text.translatableWithFallback("potion.potency."+effectAmplifier, String.valueOf(effectAmplifier)).getString();
                String effectName;
                try {
                    effectName = Text.translatable(Registries.STATUS_EFFECT.get(effectID).getTranslationKey()).getString();
                } catch (NullPointerException e) {
                    try {
                        effectName = Registries.STATUS_EFFECT.get(effectID).getName().getString();
                    } catch (NullPointerException ex) {
                        effectName = Registries.STATUS_EFFECT.getId(Registries.STATUS_EFFECT.get(effectID)).getPath();
                    }
                }
                String effectString = effectName + " " + effectLevel;
                try {
                    lines.add(1, Text.literal(effectString).setStyle(Style.EMPTY.withColor(Registries.STATUS_EFFECT.get(effectID).getColor())));
                } catch (NullPointerException e) {
                    lines.add(1, Text.literal(effectString).setStyle(Style.EMPTY.withColor(0xFFF)));
                }
            }
        });
    }
}
