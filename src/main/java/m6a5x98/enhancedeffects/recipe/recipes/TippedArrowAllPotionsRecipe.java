package m6a5x98.enhancedeffects.recipe.recipes;

import com.mojang.serialization.MapCodec;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class TippedArrowAllPotionsRecipe extends SpecialCraftingRecipe {
    public TippedArrowAllPotionsRecipe() {
        super(CraftingRecipeCategory.MISC);
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        if (craftingRecipeInput.getWidth() == 3 && craftingRecipeInput.getHeight() == 3 && craftingRecipeInput.getStackCount() == 9) {
            for (int i = 0; i < craftingRecipeInput.getHeight(); i++) {
                for (int j = 0; j < craftingRecipeInput.getWidth(); j++) {
                    ItemStack itemStack = craftingRecipeInput.getStackInSlot(j, i);
                    if (itemStack.isEmpty()) {
                        return false;
                    }

                    if (j == 1 && i == 1) {
                        if (!(itemStack.isOf(Items.POTION)
                                || itemStack.isOf(Items.SPLASH_POTION)
                                || itemStack.isOf(Items.LINGERING_POTION))) {
                            return false;
                        }
                    } else if (!itemStack.isOf(Items.ARROW)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = craftingRecipeInput.getStackInSlot(1, 1);
        if (!(itemStack.isOf(Items.POTION)
                || itemStack.isOf(Items.SPLASH_POTION)
                || itemStack.isOf(Items.LINGERING_POTION))) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack2 = new ItemStack(Items.TIPPED_ARROW, 8);
            itemStack2.set(DataComponentTypes.POTION_CONTENTS, itemStack.get(DataComponentTypes.POTION_CONTENTS));
            itemStack2.set(DataComponentTypes.CUSTOM_NAME, Text.translatable("item.enhanced_effects.custom-tipped-arrow").setStyle(Style.EMPTY.withItalic(false)));
            return itemStack2;
        }
    }


    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return ModRecipes.TIPPED_ARROW_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<TippedArrowAllPotionsRecipe> {
        @Override
        public MapCodec<TippedArrowAllPotionsRecipe> codec() {
            return MapCodec.unit(TippedArrowAllPotionsRecipe::new);
        }


        @Override
        public PacketCodec<RegistryByteBuf, TippedArrowAllPotionsRecipe> packetCodec() {
            return PacketCodec.of(
                    (buf, value) -> {},
                    buf -> new TippedArrowAllPotionsRecipe()
            );
        }
    }
}