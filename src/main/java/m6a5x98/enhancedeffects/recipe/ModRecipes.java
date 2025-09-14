package m6a5x98.enhancedeffects.recipe;

import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.recipe.recipes.ExtractRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PoisonedFoodRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.PotionMergeRecipe;
import m6a5x98.enhancedeffects.recipe.recipes.TippedArrowAllPotionsRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<PoisonedFoodRecipe> POISONED_FOOD_SERIALIZER =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(EnhancedEffects.MOD_ID, "crafting_poisoned_food"),
                    new PoisonedFoodRecipe.Serializer()
            );


    public static final RecipeSerializer<ExtractRecipe> EXTRACT_SERIALIZER =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(EnhancedEffects.MOD_ID, "crafting_extract"),
                    new ExtractRecipe.Serializer()
            );


    public static final RecipeSerializer<PotionMergeRecipe> POTION_MERGE_SERIALIZER =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(EnhancedEffects.MOD_ID, "crafting_potion_merge"),
                    new PotionMergeRecipe.Serializer()
            );


    public static final RecipeSerializer<TippedArrowAllPotionsRecipe> TIPPED_ARROW_SERIALIZER =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(EnhancedEffects.MOD_ID, "crafting_tipped_arrow"),
                    new TippedArrowAllPotionsRecipe.Serializer()
            );



    public static void init() {}
}
