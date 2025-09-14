package m6a5x98.enhancedeffects.components;

import m6a5x98.enhancedeffects.components.components.GivenEffectsComponent;
import m6a5x98.enhancedeffects.components.components.MergeFromComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

import static m6a5x98.enhancedeffects.EnhancedEffects.MOD_ID;
import static m6a5x98.enhancedeffects.components.components.GivenEffectsComponent.GIVEN_EFFECTS_LIST_CODEC;
import static m6a5x98.enhancedeffects.components.components.MergeFromComponent.MERGE_FROM_LIST_CODEC;
import static m6a5x98.enhancedeffects.components.components.ExtractedFromComponent.EXTRACTED_FROM_CODEC;

public class ModComponents {
    public static void init() {}
    public static final ComponentType<List<GivenEffectsComponent.GivenEffects>> GIVEN_EFFECTS_COMPONENT =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(MOD_ID, "given_effects"),
                    ComponentType.<List<GivenEffectsComponent.GivenEffects>>builder()
                            .codec(GIVEN_EFFECTS_LIST_CODEC)
                            .build()
    );
    public static final ComponentType<List<MergeFromComponent.MergeFrom>> MERGE_FROM_COMPONENT =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(MOD_ID, "merge_from"),
                    ComponentType.<List<MergeFromComponent.MergeFrom>>builder()
                            .codec(MERGE_FROM_LIST_CODEC)
                            .build()
    );
    public static final ComponentType<String> EXTRACTED_FROM_COMPONENT =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(MOD_ID, "extracted_from"),
                    ComponentType.<String>builder()
                            .codec(EXTRACTED_FROM_CODEC)
                            .build()
    );
}
