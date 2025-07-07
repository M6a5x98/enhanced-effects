package m6a5x98.enhancedeffects.recipe.recipes;

import m6a5x98.enhancedeffects.item.ModItems;
import m6a5x98.enhancedeffects.recipe.ModRecipes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PotionMergeRecipe extends SpecialCraftingRecipe {
    public PotionMergeRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        boolean foundPotion1 = false;
        boolean foundPotion2 = false;
        boolean foundCatalyst = false;
        boolean foundBottle = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;
            else if (!foundPotion1 && stack.getItem() instanceof PotionItem && stack.copy().getOrCreateNbt().getList("MergeFrom", NbtElement.COMPOUND_TYPE).isEmpty()) {
                foundPotion1 = true;
                continue;
            }
            else if (!foundPotion2 && stack.getItem() instanceof PotionItem && stack.copy().getOrCreateNbt().getList("MergeFrom", NbtElement.COMPOUND_TYPE).isEmpty()) foundPotion2 = true;
            else if (!foundCatalyst && stack.isOf(ModItems.catalyst)) foundCatalyst = true;
            else if (!foundBottle && stack.isOf(Items.GLASS_BOTTLE)) foundBottle = true;
            else return false;
        }
        return foundPotion1 && foundPotion2 && foundCatalyst && foundBottle;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
        ItemStack potion1 = ItemStack.EMPTY;
        ItemStack potion2 = ItemStack.EMPTY;
        boolean hasCatalyst = false;
        boolean hasGlassBottle = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof PotionItem) {
                if (potion1.isEmpty()) {
                    potion1 = stack;
                } else if (potion2.isEmpty()) {
                    potion2 = stack;
                }
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                hasGlassBottle = true;
            } else if (stack.getItem() == ModItems.catalyst) {
                hasCatalyst = true;
            }
        }

        if (potion1.isEmpty() || potion2.isEmpty() || !hasCatalyst || !hasGlassBottle) {
            return ItemStack.EMPTY;
        }

        List<StatusEffectInstance> potionEffects = new ArrayList<>();
        potionEffects.addAll(PotionUtil.getPotionEffects(potion1));
        potionEffects.addAll(PotionUtil.getPotionEffects(potion2));

        int maxEffects = 4;
        while (potionEffects.size() > maxEffects) {
            potionEffects.remove(Random.create().nextInt(potionEffects.size()));
        }

        ItemStack result = new ItemStack(Items.POTION);
        PotionUtil.setCustomPotionEffects(result, potionEffects);
        result.setCustomName(Text.translatable("item.enhanced-effects.potion-fusion").setStyle(Style.EMPTY.withItalic(false)));
        // Nbt for tooltips
        NbtList potions = new NbtList();
        NbtCompound potion1Compound = new NbtCompound();
        NbtCompound potion2Compound = new NbtCompound();
        potion1Compound.put("TranslationKey", NbtString.of(
                "item.minecraft.potion.effect." + PotionUtil.getPotion(potion1).finishTranslationKey("")
                )
        );
        if (!potion1.getOrCreateNbt().getString("ExtractedFrom").isEmpty()) {
            potion1Compound.put("ExtractedFrom", NbtString.of(potion1.getOrCreateNbt().getString("ExtractedFrom")));
            potion1Compound.put("TranslationKey", NbtString.of("item.enhanced-effects.extract"));
        }
        potion2Compound.put("TranslationKey", NbtString.of(
                "item.minecraft.potion.effect." + PotionUtil.getPotion(potion2).finishTranslationKey("")
                )
        );
        if (!potion2.getOrCreateNbt().getString("ExtractedFrom").isEmpty()) {
            potion2Compound.put("ExtractedFrom", NbtString.of(potion2.getOrCreateNbt().getString("ExtractedFrom")));
            potion2Compound.put("TranslationKey", NbtString.of("item.enhanced-effects.extract"));
        }
        potions.add(potion1Compound);
        potions.add(potion2Compound);
        result.getOrCreateNbt().put("MergeFrom", potions);
        result.getNbt().putInt("CustomPotionColor", (PotionUtil.getColor(potion1) + PotionUtil.getColor(potion2)) / 2);

        return result;
    }


    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inv) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isOf(ModItems.catalyst)) {
                ItemStack copy = stack.copy();
                copy.setDamage(copy.getDamage() + 1);
                if (copy.getDamage() < copy.getMaxDamage()) {
                    remainders.set(i, copy);
                }
                continue;
            }
            if (stack.isOf(Items.POTION)) {
                remainders.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return remainders;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.POTION_MERGE;
    }

    public static class RecipeType implements net.minecraft.recipe.RecipeType<PotionMergeRecipe> {
        public static final PotionMergeRecipe.RecipeType INSTANCE = new PotionMergeRecipe.RecipeType();
        public static final String ID = "crafting_special_potion_merge";
    }
}
