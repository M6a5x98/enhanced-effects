package m6a5x98.enhancedeffects.components.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class MergeFromComponent {
    public record MergeFrom(String extractedFrom, String translationKey) {
        public boolean isEmpty() {
            return (extractedFrom == null || extractedFrom.isEmpty()) &&
                    (translationKey == null || translationKey.isEmpty());
        }
    }
    public static final String EMPTY = "";
    public static final Codec<MergeFrom> MERGE_FROM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("extracted_from").forGetter(m -> m.extractedFrom != null ? m.extractedFrom : ""),
            Codec.STRING.fieldOf("translation_key").forGetter(m -> m.translationKey != null ? m.translationKey : "")
    ).apply(instance, MergeFrom::new));
    public static final Codec<List<MergeFrom>> MERGE_FROM_LIST_CODEC = MERGE_FROM_CODEC.listOf();
}
