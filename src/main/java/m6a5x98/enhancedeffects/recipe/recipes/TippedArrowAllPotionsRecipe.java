package m6a5x98.enhancedeffects.recipe.recipes;

import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TippedArrowAllPotionsRecipe extends SpecialCraftingRecipe {
    public TippedArrowAllPotionsRecipe(Identifier identifier, CraftingRecipeCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        if (recipeInputInventory.getWidth() == 3 && recipeInputInventory.getHeight() == 3) {
            for (int i = 0; i < recipeInputInventory.getWidth(); i++) {
                for (int j = 0; j < recipeInputInventory.getHeight(); j++) {
                    ItemStack itemStack = recipeInputInventory.getStack(i + j * recipeInputInventory.getWidth());
                    if (itemStack.isEmpty()) {
                        return false;
                    }

                    if (i == 1 && j == 1) {
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

    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = recipeInputInventory.getStack(1 + recipeInputInventory.getWidth());
        if (!(itemStack.isOf(Items.POTION)
                || itemStack.isOf(Items.SPLASH_POTION)
                || itemStack.isOf(Items.LINGERING_POTION))) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemStack2 = new ItemStack(Items.TIPPED_ARROW, 8);
            PotionUtil.setPotion(itemStack2, PotionUtil.getPotion(itemStack));
            PotionUtil.setCustomPotionEffects(itemStack2, PotionUtil.getCustomPotionEffects(itemStack));
            itemStack2.getNbt().putInt("CustomPotionColor", PotionUtil.getColor(itemStack));
            return itemStack2;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TIPPED_ARROW;
    }

    public static class RecipeType implements net.minecraft.recipe.RecipeType<TippedArrowAllPotionsRecipe> {
        public static final TippedArrowAllPotionsRecipe.RecipeType INSTANCE = new TippedArrowAllPotionsRecipe.RecipeType();
        public static final String ID = "crafting_special_tipped_arrow";
    }
}
