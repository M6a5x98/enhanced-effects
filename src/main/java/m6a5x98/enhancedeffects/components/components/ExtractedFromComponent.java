package m6a5x98.enhancedeffects.components.components;

import com.mojang.serialization.Codec;

public class ExtractedFromComponent {
    public static Codec<String> EXTRACTED_FROM_CODEC = Codec.string(5, Integer.MAX_VALUE);
}
