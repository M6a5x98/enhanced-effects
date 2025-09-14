package m6a5x98.enhancedeffects;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("Max. amount of effects in a Potion Fusion")
            .defineInRange("maxEffectsInPotionFusion", 4, 0, Integer.MAX_VALUE);
    static final ModConfigSpec SPEC = BUILDER.build();
}
