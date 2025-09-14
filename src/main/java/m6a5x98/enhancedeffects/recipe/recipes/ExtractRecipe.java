package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.EnhancedEffectsUtil;
import m6a5x98.enhancedeffects.component.ModComponents;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ExtractRecipe extends CustomRecipe {


    public ExtractRecipe() {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
        boolean foundBottle = false;
        boolean foundFood = false;
        boolean foodHasEffects = false;
        boolean potionTypeMutator = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (!foundFood && EnhancedEffectsUtil.isFood(stack)) {
                foundFood = true;
                foodHasEffects = !EnhancedEffectsUtil.getPotionEffects(stack).isEmpty()
                        || !EnhancedEffectsUtil.getSusStewEffects(stack).isEmpty();
                continue;
            }
            if (!foundBottle && stack.is(Items.GLASS_BOTTLE)) {
                foundBottle = true;
                continue;
            }
            if (!potionTypeMutator && (stack.is(Items.GUNPOWDER) || stack.is(Items.DRAGON_BREATH))) {
                potionTypeMutator = true;
                continue;
            }
            return false;
        }

        return foundBottle && foundFood && foodHasEffects;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv, HolderLookup.@NotNull Provider registries) {
        ItemStack foodStack = ItemStack.EMPTY;
        ItemStack result = new ItemStack(Items.POTION);

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (EnhancedEffectsUtil.isFood(stack)) {
                foodStack = stack.copy();
                continue;
            }
            if (stack.is(Items.GUNPOWDER)) {
                result = new ItemStack(Items.SPLASH_POTION);
            }
            if (stack.is(Items.DRAGON_BREATH)) {
                result = new ItemStack(Items.LINGERING_POTION);
            }
        }
        List<MobEffectInstance> foodEffects = EnhancedEffectsUtil.getPotionEffects(foodStack);
        if (foodStack.is(Items.SUSPICIOUS_STEW)) {
            foodEffects.addAll(EnhancedEffectsUtil.getSusStewEffects(foodStack));
        }
        Component foodName = Component.translatable(foodStack.getItem().getDescriptionId());
        result.set(DataComponents.CUSTOM_NAME, Component.translatable(
                "item.enhanced_effects." + (
                        result.is(Items.SPLASH_POTION)
                                ? "splash_"
                                : result.is(Items.LINGERING_POTION)
                                ? "lingering_"
                                : ""
                ) + "extract",
                foodName
        ).setStyle(Style.EMPTY.withItalic(false)));
        result.set(ModComponents.EXTRACTED_FROM_COMPONENT, foodStack.getItem().getDescriptionId());
        result.set(DataComponents.POTION_CONTENTS, new PotionContents(
                Optional.of(Potions.WATER),
                Optional.of(RandomSource.create().nextInt()),
                foodEffects
        ));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.EXTRACT_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<ExtractRecipe> {
        @Override
        public @NotNull MapCodec<ExtractRecipe> codec() {
            return MapCodec.unit(ExtractRecipe::new);
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ExtractRecipe> streamCodec() {
            return StreamCodec.of(
                    (buf, value) -> {
                    },
                    buf -> new ExtractRecipe()
            );
        }
    }
}
