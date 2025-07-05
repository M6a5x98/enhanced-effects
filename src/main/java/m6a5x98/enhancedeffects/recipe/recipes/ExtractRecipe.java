package m6a5x98.enhancedeffects.recipe.recipes;

import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ExtractRecipe extends SpecialCraftingRecipe  {
    public ExtractRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        boolean foundBottle = false;
        boolean foundFood = false;
        boolean foodHasEffects = false;
        boolean potionTypeMutator = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            if (!foundFood && stack.isFood()) {
                foundFood = true;
                foodHasEffects = !stack.getItem()
                        .getFoodComponent()
                        .getStatusEffects()
                        .isEmpty() || !stack.getOrCreateNbt().getList("Effects", NbtElement.COMPOUND_TYPE).isEmpty();
                continue;
            }
            if (!foundBottle && stack.isOf(Items.GLASS_BOTTLE)) {
                foundBottle = true;
                continue;
            }
            if (!potionTypeMutator && (stack.isOf(Items.GUNPOWDER) || stack.isOf(Items.DRAGON_BREATH))) {
                potionTypeMutator = true;
                continue;
            }
            return false;
        }

        return foundBottle && foundFood && foodHasEffects;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
        ItemStack foodStack = ItemStack.EMPTY;
        ItemStack result = new ItemStack(Items.POTION);

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.isFood()) {
                foodStack = stack.copy();
                continue;
            }
            if (stack.isOf(Items.GUNPOWDER)) {
                result = new ItemStack(Items.SPLASH_POTION);
            }
            if (stack.isOf(Items.DRAGON_BREATH)) {
                result = new ItemStack(Items.LINGERING_POTION);
            }
        }
        List<net.minecraft.entity.effect.StatusEffectInstance> foodEffects = new ArrayList<>();
        foodStack.getItem()
                .getFoodComponent()
                .getStatusEffects()
                .forEach(pair -> foodEffects.add(pair.getFirst()));
        if (foodStack.isOf(Items.SUSPICIOUS_STEW)) {
            foodStack.getOrCreateNbt().getList("Effects", NbtElement.COMPOUND_TYPE).forEach(stewEffect ->
                    foodEffects.add(
                            new StatusEffectInstance(
                                Registries.STATUS_EFFECT.get(
                                        ((NbtCompound) stewEffect).getInt("EffectId")
                                ), ((NbtCompound) stewEffect).getInt("EffectDuration")
                        )
                    )
            );
        }
        PotionUtil.setCustomPotionEffects(result, foodEffects);
        Text foodName = Text.translatable(foodStack.getItem().getTranslationKey());
        result.setCustomName(Text.translatable(
                "item.enhanced-effects.extract",
                foodName
        ).setStyle(Style.EMPTY.withItalic(false)));
        result.getOrCreateNbt().put("ExtractedFrom", NbtString.of(foodStack.getItem().getTranslationKey()));
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EXTRACT;
    }

    public static class RecipeType implements net.minecraft.recipe.RecipeType<ExtractRecipe> {
        public static final ExtractRecipe.RecipeType INSTANCE = new ExtractRecipe.RecipeType();
        public static final String ID = "crafting_special_extract";
    }
}
