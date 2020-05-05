package io.isotope.enigma.engine.services.db;

import io.isotope.enigma.engine.domain.Encrypted;
import io.isotope.enigma.engine.domain.Key;
import io.isotope.enigma.engine.services.aes.AESFactory;
import io.isotope.enigma.engine.services.crypto.Decryptor;
import io.isotope.enigma.engine.services.crypto.Encryptor;
import io.isotope.enigma.engine.services.aes.KeySpecification;
import io.isotope.enigma.engine.services.crypto.StringDecryptor;
import io.isotope.enigma.engine.services.crypto.StringEncryptor;
import io.isotope.enigma.engine.services.exceptions.EnigmaException;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class DatabaseCryptoService implements DatabaseCrypto {

    private final KeySpecification dbKeySpecification;
    private final AESFactory aesFactory;

    public DatabaseCryptoService(KeySpecification dbKeySpecification, AESFactory aesFactory) {
        this.dbKeySpecification = dbKeySpecification;
        this.aesFactory = aesFactory;
    }

    @Override
    public Key decrypt(Key key) {
        StringDecryptor dec = AESFactory.key(dbKeySpecification).stringDecryptor(StandardCharsets.UTF_8);
        for (Field field : Key.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Encrypted.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(key);
                        String decrypted = dec.decrypt(value);
                        field.set(key, decrypted);
                    } catch (Exception e) {
                        throw new EnigmaException(e);
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
        return key;
    }

    @Override
    public Key encrypt(Key key) {
        StringEncryptor enc = AESFactory.key(dbKeySpecification).stringEncryptor(StandardCharsets.UTF_8);

        for (Field field : Key.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Encrypted.class)) {
                if (field.getType().isAssignableFrom(String.class)) {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(key);
                        String encrypted = enc.encrypt(value);
                        field.set(key, encrypted);
                    } catch (Exception e) {
                        throw new EnigmaException(e);
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
        return key;
    }
}
