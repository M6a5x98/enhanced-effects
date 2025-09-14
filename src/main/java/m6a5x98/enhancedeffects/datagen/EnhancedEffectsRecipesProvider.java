package m6a5x98.enhancedeffects.datagen;

import m6a5x98.enhancedeffects.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EnhancedEffectsRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public EnhancedEffectsRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CATALYST.get())
                .requires(Items.PAPER)
                .requires(Items.GLOWSTONE_DUST)
                .requires(Items.REDSTONE)
                .requires(Items.SUGAR)
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .unlockedBy("has_glowstone", has(Items.GLOWSTONE_DUST))
                .save(recipeOutput);
    }
}
