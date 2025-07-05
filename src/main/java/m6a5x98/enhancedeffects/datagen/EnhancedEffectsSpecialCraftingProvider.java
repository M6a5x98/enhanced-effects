package m6a5x98.enhancedeffects.datagen;

import com.google.gson.JsonObject;
import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EnhancedEffectsSpecialCraftingProvider {
    public static void generateCraftingSpecialRecipe(Consumer<RecipeJsonProvider> consumer, String recipeID, RecipeSerializer<?> serializer) {
        consumer.accept(new RecipeJsonProvider() {
            private final String recipeId = recipeID;
            @Override
            public void serialize(JsonObject json) {json.addProperty("type", EnhancedEffects.MOD_ID + ":"+ recipeId);}
            @Override
            public Identifier getRecipeId() {return new Identifier(EnhancedEffects.MOD_ID, recipeId);}
            @Override
            public RecipeSerializer<?> getSerializer() {return serializer;}
            @Override
            public JsonObject toAdvancementJson() {return null;}
            @Override
            @Nullable
            public Identifier getAdvancementId() {return null;}
        });
    }
}
