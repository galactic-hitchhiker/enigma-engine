package io.isotope.enigma.engine.services.crypto;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapStringEncryptor implements Encryptor<Map<String, String>, Map<String, String>> {

    private final StringEncryptor stringEncryptor;

    public MapStringEncryptor(StringEncryptor stringEncryptor) {
        this.stringEncryptor = stringEncryptor;
    }

    @Override
    public Map<String, String> encrypt(Map<String, String> value) {
        return value.entrySet().stream()
                .filter(v -> Objects.nonNull(v.getKey()))
                .filter(v -> Objects.nonNull(v.getValue()))
                .map(v -> Map.entry(v.getKey(), stringEncryptor.encrypt(v.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
