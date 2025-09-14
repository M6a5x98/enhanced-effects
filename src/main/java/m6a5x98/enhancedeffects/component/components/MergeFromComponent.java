package m6a5x98.enhancedeffects.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class MergeFromComponent {
    public record MergeFrom(String extractedFrom, String translationKey) {}

    private static final Codec<MergeFrom> _MERGE_FROM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("extracted_from").forGetter(m -> m.extractedFrom != null ? m.extractedFrom : ""),
            Codec.STRING.fieldOf("translation_key").forGetter(m -> m.translationKey != null ? m.translationKey : "")
    ).apply(instance, MergeFrom::new));
    public static final Codec<List<MergeFrom>> MERGE_FROM_CODEC = _MERGE_FROM_CODEC.listOf();
}
