package m6a5x98.enhancedeffects.item;

import m6a5x98.enhancedeffects.EnhancedEffects;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item catalyst = registerItem("catalyst", new Item(new FabricItemSettings().maxCount(64).maxDamage(5)));

    public static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(catalyst);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(EnhancedEffects.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemsToIngredientItemGroup);
    }
}
