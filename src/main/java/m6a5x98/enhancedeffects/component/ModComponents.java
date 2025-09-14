package m6a5x98.enhancedeffects.component;

import m6a5x98.enhancedeffects.EnhancedEffects;
import m6a5x98.enhancedeffects.component.components.ExtractedFromComponent;
import m6a5x98.enhancedeffects.component.components.GivenEffectsComponent;
import m6a5x98.enhancedeffects.component.components.MergeFromComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, EnhancedEffects.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<GivenEffectsComponent.GivenEffects>>> GIVEN_EFFECTS_COMPONENT =
            DATA_COMPONENTS.register("given_effects", () ->
                    DataComponentType.<List<GivenEffectsComponent.GivenEffects>>builder()
                            .persistent(GivenEffectsComponent.GIVEN_EFFECTS_CODEC)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<MergeFromComponent.MergeFrom>>> MERGE_FROM_COMPONENT =
            DATA_COMPONENTS.register("merge_from", () ->
                    DataComponentType.<List<MergeFromComponent.MergeFrom>>builder()
                            .persistent(MergeFromComponent.MERGE_FROM_CODEC)
                            .build()
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> EXTRACTED_FROM_COMPONENT =
            DATA_COMPONENTS.register("extracted_from", () ->
                    DataComponentType.<String>builder()
                            .persistent(ExtractedFromComponent.EXTRACTED_FROM_CODEC)
                            .build()
            );

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
