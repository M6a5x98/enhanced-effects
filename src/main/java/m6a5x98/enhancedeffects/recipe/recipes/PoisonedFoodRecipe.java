package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.components.ModComponents;
import m6a5x98.enhancedeffects.components.components.GivenEffectsComponent;
import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class PoisonedFoodRecipe extends SpecialCraftingRecipe {

    public PoisonedFoodRecipe() {
        super(CraftingRecipeCategory.MISC);
    }

    @Override
    public boolean matches(CraftingRecipeInput inv, World world) {
        boolean foundFood = false;
        boolean foundPotion = false;
        boolean foundCatalyst = false;
        boolean foodHasEffects = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (!foundFood && EnhancedEffectsUtil.isFood(stack)) {
                foundFood = true;

                var consumable = stack.getItem().getComponents().get(DataComponentTypes.CONSUMABLE);
                var stewEffects = stack.getComponents().get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);

                foodHasEffects = (consumable != null && !consumable.onConsumeEffects().isEmpty())
                        || (stewEffects != null && !stewEffects.effects().isEmpty());
                continue;
            }

            if (!foundPotion && stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION)) {
                foundPotion = true;
                continue;
            }

            if (!foundCatalyst && stack.isOf(ModItems.CATALYST)) {
                foundCatalyst = true;
                continue;
            }

            return false;
        }

        return foundFood && (foodHasEffects ? foundCatalyst : foundPotion);
    }



    @Override
    public ItemStack craft(CraftingRecipeInput inv, RegistryWrapper.WrapperLookup registries) {
        ItemStack result = ItemStack.EMPTY;
        ItemStack potionStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof PotionItem) {
                potionStack = stack;
            } else if (EnhancedEffectsUtil.isFood(stack)) {
                result = new ItemStack(stack.getItem(), 1);
            }
        }
        if (result.isEmpty() || potionStack.isEmpty()) {
            return new ItemStack(Items.AIR);
        }
        List<StatusEffectInstance> effects = EnhancedEffectsUtil.getPotionEffects(potionStack);
        List<GivenEffectsComponent.GivenEffects> given_effects = effects.stream()
                .map(sei -> new GivenEffectsComponent.GivenEffects(
                        Registries.STATUS_EFFECT.getRawId(sei.getEffectType().value()),
                        (byte) sei.getAmplifier(),
                        sei.getDuration()
                ))
                .toList();
        result.set(ModComponents.GIVEN_EFFECTS_COMPONENT, given_effects);
        return result;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.POISONED_FOOD_SERIALIZER;
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
            }
        }
        return remainders;
    }

    public static class Serializer implements RecipeSerializer<PoisonedFoodRecipe> {
        @Override
        public MapCodec<PoisonedFoodRecipe> codec() {
            return MapCodec.unit(PoisonedFoodRecipe::new);
        }


        @Override
        public PacketCodec<RegistryByteBuf, PoisonedFoodRecipe> packetCodec() {
            return PacketCodec.of(
                    (buf, value) -> {},
                    buf -> new PoisonedFoodRecipe()
            );
        }
    }
}
