package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TippedArrowAllPotionsRecipe extends CustomRecipe {
    public TippedArrowAllPotionsRecipe() {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(@NotNull CraftingInput craftingRecipeInput, @NotNull Level world) {
        if (craftingRecipeInput.width() == 3 && craftingRecipeInput.height() == 3 && craftingRecipeInput.ingredientCount() == 9) {
            for (int i = 0; i < craftingRecipeInput.height(); i++) {
                for (int j = 0; j < craftingRecipeInput.width(); j++) {
                    ItemStack itemStack = craftingRecipeInput.getItem(j, i);
                    if (itemStack.isEmpty()) {
                        return false;
                    }

                    if (j == 1 && i == 1) {
                        if (!(itemStack.is(Items.POTION)
                                || itemStack.is(Items.SPLASH_POTION)
                                || itemStack.is(Items.LINGERING_POTION))) {
                            return false;
                        }
                    } else if (!itemStack.is(Items.ARROW)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput craftingRecipeInput, HolderLookup.@NotNull Provider registries) {
        ItemStack itemStack = craftingRecipeInput.getItem(1, 1);
        if (!(itemStack.is(Items.POTION)
                || itemStack.is(Items.SPLASH_POTION)
                || itemStack.is(Items.LINGERING_POTION))) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack2 = new ItemStack(Items.TIPPED_ARROW, 8);
            itemStack2.set(DataComponents.POTION_CONTENTS, itemStack.get(DataComponents.POTION_CONTENTS));
            itemStack2.set(DataComponents.CUSTOM_NAME, Component.translatable("item.enhanced_effects.custom-tipped-arrow").setStyle(Style.EMPTY.withItalic(false)));
            return itemStack2;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.TIPPED_ARROW_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<TippedArrowAllPotionsRecipe> {
        @Override
        public @NotNull MapCodec<TippedArrowAllPotionsRecipe> codec() {
            return MapCodec.unit(TippedArrowAllPotionsRecipe::new);
        }


        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, TippedArrowAllPotionsRecipe> streamCodec() {
            return StreamCodec.of(
                    (buf, value) -> {
                    },
                    buf -> new TippedArrowAllPotionsRecipe()
            );
        }
    }
}
