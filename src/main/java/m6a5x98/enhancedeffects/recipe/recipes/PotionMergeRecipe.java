package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.components.components.MergeFromComponent;
import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class PotionMergeRecipe extends SpecialCraftingRecipe {
    public PotionMergeRecipe() {
        super(CraftingRecipeCategory.MISC);
    }

    @Override
    public boolean matches(CraftingRecipeInput inv, World world) {
        boolean foundPotion1 = false;
        boolean foundPotion2 = false;
        boolean foundCatalyst = false;
        boolean foundBottle = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            else if (!foundPotion1 && stack.getItem() instanceof PotionItem && EnhancedEffectsUtil.isMergeFromComponentEmpty(stack)) {
                foundPotion1 = true;
                continue;
            }
            else if (!foundPotion2 && stack.getItem() instanceof PotionItem && EnhancedEffectsUtil.isMergeFromComponentEmpty(stack)) foundPotion2 = true;
            else if (!foundCatalyst && stack.isOf(ModItems.CATALYST)) foundCatalyst = true;
            else if (!foundBottle && stack.isOf(Items.GLASS_BOTTLE)) foundBottle = true;
            else return false;
        }
        return foundPotion1 && foundPotion2 && foundCatalyst && foundBottle;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inv, RegistryWrapper.WrapperLookup registryManager) {
        ItemStack potion1 = ItemStack.EMPTY;
        ItemStack potion2 = ItemStack.EMPTY;
        boolean hasCatalyst = false;
        boolean hasGlassBottle = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof PotionItem) {
                if (potion1.isEmpty()) {
                    potion1 = stack;
                } else if (potion2.isEmpty()) {
                    potion2 = stack;
                }
            } else if (stack.isOf(Items.GLASS_BOTTLE)) {
                hasGlassBottle = true;
            } else if (stack.isOf(ModItems.CATALYST)) {
                hasCatalyst = true;
            }
        }

        if (potion1.isEmpty() || potion2.isEmpty() || !hasCatalyst || !hasGlassBottle) {
            return ItemStack.EMPTY;
        }

        List<StatusEffectInstance> potionEffects = new ArrayList<>();
        potionEffects.addAll(EnhancedEffectsUtil.getPotionEffects(potion1));
        potionEffects.addAll(EnhancedEffectsUtil.getPotionEffects(potion2));

        int maxEffects = 4;
        while (potionEffects.size() > maxEffects) {
            potionEffects.remove(Random.create().nextInt(potionEffects.size()));
        }

        ItemStack result = new ItemStack(RegistryEntry.of(Items.POTION));
        result.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(
                Optional.empty(),
                Optional.of((EnhancedEffectsUtil.getPotionColor(potion1) + EnhancedEffectsUtil.getPotionColor(potion2)) / 2), potionEffects,
                Optional.empty()
        ));
        result.set(DataComponentTypes.CUSTOM_NAME, Text.translatable("item.enhanced_effects.potion-fusion").setStyle(Style.EMPTY.withItalic(false)));

        // Nbt for tooltips
        List<MergeFromComponent.MergeFrom> potions = new ArrayList<>();
        AtomicReference<String> extractedFrom = new AtomicReference<>();
        AtomicReference<String> translationKey = new AtomicReference<>();
        //===========
        if (potion1.getComponents().contains(ModComponents.EXTRACTED_FROM_COMPONENT)) {
            EnhancedEffectsUtil.getComponent(potion1, ModComponents.EXTRACTED_FROM_COMPONENT)
                    .ifPresent(e -> extractedFrom.set(e.toString()));
            translationKey.set("item.enhanced_effects.extract");
        } else {
            translationKey.set("tooltip.potion-fusion.potionOf");
            EnhancedEffectsUtil.getComponent(potion1, DataComponentTypes.POTION_CONTENTS).ifPresent(e -> extractedFrom.set(((PotionContentsComponent) e).potion().orElse(Potions.WATER).value().getEffects().getFirst().getEffectType().value().getTranslationKey()));
        }
        potions.add(new MergeFromComponent.MergeFrom(extractedFrom.get(), translationKey.get()));
        //===========
        if (potion2.getComponents().contains(ModComponents.EXTRACTED_FROM_COMPONENT)) {
            EnhancedEffectsUtil.getComponent(potion2, ModComponents.EXTRACTED_FROM_COMPONENT)
                    .ifPresent(e -> extractedFrom.set(e.toString()));
            translationKey.set("item.enhanced_effects.extract");
        } else {
            translationKey.set("tooltip.potion-fusion.potionOf");
            EnhancedEffectsUtil.getComponent(potion2, DataComponentTypes.POTION_CONTENTS).ifPresent(e -> extractedFrom.set(((PotionContentsComponent) e).potion().orElse(Potions.WATER).value().getEffects().getFirst().getEffectType().value().getTranslationKey()));
        }
        potions.add(new MergeFromComponent.MergeFrom(extractedFrom.get(), translationKey.get()));
        result.set(ModComponents.MERGE_FROM_COMPONENT, potions);

        return result;
    }


    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput inv) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isOf(ModItems.CATALYST)) {
                ItemStack copy = stack.copy();
                copy.setDamage(copy.getDamage() + 1);
                if (copy.getDamage() < copy.getMaxDamage()) {
                    remainders.set(i, copy);
                }
                continue;
            }
            if (stack.isOf(Items.POTION)) {
                remainders.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return remainders;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.POTION_MERGE_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<PotionMergeRecipe> {
        @Override
        public MapCodec<PotionMergeRecipe> codec() {
            return MapCodec.unit(PotionMergeRecipe::new);
        }


        @Override
        public PacketCodec<RegistryByteBuf, PotionMergeRecipe> packetCodec() {
            return PacketCodec.of(
                    (buf, value) -> {},
                    buf -> new PotionMergeRecipe()
            );
        }
    }
}
