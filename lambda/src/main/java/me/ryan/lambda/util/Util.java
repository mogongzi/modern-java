package me.ryan.lambda.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Util {
    private Util() {
    }

    public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;

    public static final List<String> BASE_62_CHARS = Collections.unmodifiableList(Arrays
            .asList(IntStream.rangeClosed('0', '9'), IntStream.rangeClosed('A', 'Z'), IntStream.rangeClosed('a', 'z'))
            .stream()
            .flatMap(range -> range.mapToObj(i -> String.valueOf((char) i)))
            .collect(Collectors.toList()));

    public static final boolean areAllNonNull(final Object... objects) {
        return Arrays.stream(objects).allMatch(Objects::nonNull);
    }

    public static <K, U, V> Collector<Map.Entry<K, U>, ?, Map<K, V>> mapValueTransformer(final Function<U, V> valueMapper) {
        return Collectors.toMap(Map.Entry::getKey, entry -> valueMapper.apply(entry.getValue()));
    }
}
