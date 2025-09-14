package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.Config;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.component.ModComponents;
import m6a5x98.enhancedeffects.component.components.MergeFromComponent;
import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class PotionMergeRecipe extends CustomRecipe {
    public PotionMergeRecipe() {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        boolean foundPotion1 = false;
        boolean foundPotion2 = false;
        boolean foundCatalyst = false;
        boolean foundBottle = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            else if (!foundPotion1 && stack.getItem() instanceof PotionItem && EnhancedEffectsUtil.isMergeFromComponentEmpty(stack)) {
                foundPotion1 = true;
                continue;
            } else if (!foundPotion2 && stack.getItem() instanceof PotionItem && EnhancedEffectsUtil.isMergeFromComponentEmpty(stack))
                foundPotion2 = true;
            else if (!foundCatalyst && stack.is(ModItems.CATALYST)) foundCatalyst = true;
            else if (!foundBottle && stack.is(Items.GLASS_BOTTLE)) foundBottle = true;
            else return false;
        }
        return foundPotion1 && foundPotion2 && foundCatalyst && foundBottle;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.@NotNull Provider registries) {
        ItemStack potion1 = ItemStack.EMPTY;
        ItemStack potion2 = ItemStack.EMPTY;
        boolean hasCatalyst = false;
        boolean hasGlassBottle = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof PotionItem) {
                if (potion1.isEmpty()) {
                    potion1 = stack;
                } else if (potion2.isEmpty()) {
                    potion2 = stack;
                }
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                hasGlassBottle = true;
            } else if (stack.is(ModItems.CATALYST)) {
                hasCatalyst = true;
            }
        }

        if (potion1.isEmpty() || potion2.isEmpty() || !hasCatalyst || !hasGlassBottle) {
            return ItemStack.EMPTY;
        }

        List<MobEffectInstance> potionEffects = new ArrayList<>();
        potionEffects.addAll(EnhancedEffectsUtil.getPotionEffects(potion1));
        potionEffects.addAll(EnhancedEffectsUtil.getPotionEffects(potion2));

        int maxEffects = Config.MAGIC_NUMBER.getAsInt();
        while (potionEffects.size() > maxEffects) {
            potionEffects.remove(RandomSource.create().nextInt(potionEffects.size()));
        }

        ItemStack result = new ItemStack(Items.POTION);
        result.set(DataComponents.POTION_CONTENTS, new PotionContents(
                Optional.empty(),
                Optional.of((EnhancedEffectsUtil.getPotionColor(potion1) + EnhancedEffectsUtil.getPotionColor(potion2)) / 2),
                potionEffects
        ));
        result.set(DataComponents.CUSTOM_NAME, Component.translatable("item.enhanced_effects.potion-fusion").setStyle(Style.EMPTY.withItalic(false)));

        // Nbt for tooltips
        List<MergeFromComponent.MergeFrom> potions = new ArrayList<>();
        AtomicReference<String> extractedFrom = new AtomicReference<>();
        AtomicReference<String> translationKey = new AtomicReference<>();
        //===========
        if (potion1.getComponents().has(ModComponents.EXTRACTED_FROM_COMPONENT.get())) {
            EnhancedEffectsUtil.getComponent(potion1, ModComponents.EXTRACTED_FROM_COMPONENT.get())
                    .ifPresent(extractedFrom::set);
            translationKey.set("item.enhanced_effects.extract");
        } else {
            translationKey.set("tooltip.potion-fusion.potionOf");
            EnhancedEffectsUtil.getComponent(potion1, DataComponents.POTION_CONTENTS).ifPresent(e -> extractedFrom.set(e.potion().orElse(Potions.WATER).value().getEffects().getFirst().getEffect().value().getDescriptionId()));
        }
        potions.add(new MergeFromComponent.MergeFrom(extractedFrom.get(), translationKey.get()));
        //===========
        if (potion2.getComponents().has(ModComponents.EXTRACTED_FROM_COMPONENT.get())) {
            EnhancedEffectsUtil.getComponent(potion2, ModComponents.EXTRACTED_FROM_COMPONENT.get())
                    .ifPresent(extractedFrom::set);
            translationKey.set("item.enhanced_effects.extract");
        } else {
            translationKey.set("tooltip.potion-fusion.potionOf");
            EnhancedEffectsUtil.getComponent(potion2, DataComponents.POTION_CONTENTS).ifPresent(e -> extractedFrom.set(e.potion().orElse(Potions.WATER).value().getEffects().getFirst().getEffect().value().getDescriptionId()));
        }
        potions.add(new MergeFromComponent.MergeFrom(extractedFrom.get(), translationKey.get()));
        result.set(ModComponents.MERGE_FROM_COMPONENT, potions);

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput inv) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.CATALYST)) {
                ItemStack copy = stack.copy();
                copy.setDamageValue(copy.getDamageValue() + 1);
                if (copy.getDamageValue() < copy.getMaxDamage()) {
                    remainders.set(i, copy);
                }
                continue;
            }
            if (stack.is(Items.POTION)) {
                remainders.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return remainders;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.POTION_MERGE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<PotionMergeRecipe> {
        @Override
        public @NotNull MapCodec<PotionMergeRecipe> codec() {
            return MapCodec.unit(PotionMergeRecipe::new);
        }


        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, PotionMergeRecipe> streamCodec() {
            return StreamCodec.of(
                    (buf, value) -> {
                    },
                    buf -> new PotionMergeRecipe()
            );
        }
    }
}
