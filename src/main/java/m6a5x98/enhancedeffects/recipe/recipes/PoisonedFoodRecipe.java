package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.component.ModComponents;
import m6a5x98.enhancedeffects.component.components.GivenEffectsComponent;
import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PoisonedFoodRecipe extends CustomRecipe {
    public PoisonedFoodRecipe() {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
        boolean foundFood = false;
        boolean foundPotion = false;
        boolean foundCatalyst = false;
        boolean foodHasEffects = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (!foundFood && EnhancedEffectsUtil.isFood(stack)) {
                foundFood = true;

                var stewEffects = stack.getComponents().get(DataComponents.SUSPICIOUS_STEW_EFFECTS);

                foodHasEffects = (!EnhancedEffectsUtil.getPotionEffects(stack).isEmpty())
                        || (stewEffects != null && !stewEffects.effects().isEmpty());
                continue;
            }

            if (!foundPotion && stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
                foundPotion = true;
                continue;
            }

            if (!foundCatalyst && stack.is(ModItems.CATALYST)) {
                foundCatalyst = true;
                continue;
            }

            return false;
        }

        return foundFood && (foodHasEffects ? foundCatalyst : foundPotion);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.@NotNull Provider registries) {
        ItemStack result = ItemStack.EMPTY;
        ItemStack potionStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
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
        List<MobEffectInstance> effects = EnhancedEffectsUtil.getPotionEffects(potionStack);
        List<GivenEffectsComponent.GivenEffects> given_effects = effects.stream()
                .map(sei -> new GivenEffectsComponent.GivenEffects(
                        BuiltInRegistries.MOB_EFFECT.getKey(sei.getEffect().value()).toString(),
                        (byte) sei.getAmplifier(),
                        sei.getDuration()
                ))
                .toList();
        result.set(ModComponents.GIVEN_EFFECTS_COMPONENT, given_effects);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.CATALYST)) {
                ItemStack copy = stack.copy();
                copy.setDamageValue(copy.getDamageValue() + 1);
                if (copy.getDamageValue() < copy.getMaxDamage()) {
                    remainders.set(i, copy);
                }
            }
        }
        return remainders;
    }

    @Override
    public @NotNull RecipeSerializer<PoisonedFoodRecipe> getSerializer() {
        return ModRecipes.POISONED_FOOD_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<PoisonedFoodRecipe> {
        @Override
        public @NotNull MapCodec<PoisonedFoodRecipe> codec() {
            return MapCodec.unit(PoisonedFoodRecipe::new);
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, PoisonedFoodRecipe> streamCodec() {
            return StreamCodec.of(
                    (buf, value) -> {
                    },
                    buf -> new PoisonedFoodRecipe()
            );
        }
    }
}
