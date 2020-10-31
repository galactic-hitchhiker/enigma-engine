package io.isotope.enigma.engine.services.rsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.security.*;
import java.util.Optional;

public class RSA {

    private static final Logger logger = LoggerFactory.getLogger(RSA.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    public static final String NAME = "RSA";
    public static final String BLOCK_MODE = "ECB";
    public static final String PADDING = "OAEPWithSHA-512AndMGF1Padding";
    public static final Integer RSA_KEY_LENGTH = 4096;

    private RSA() { }

    public static Optional<RSAKeySpecification> generateKey() {

        try {
            StopWatch sw = new StopWatch();
            sw.start();

            KeyPairGenerator kpg = KeyPairGenerator.getInstance(NAME);
            kpg.initialize(RSA_KEY_LENGTH, secureRandom);
            KeyPair keyPair = kpg.generateKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            sw.stop();

            logger.info("RSA key generation finished in {} seconds ", sw.getTotalTimeSeconds());

            return Optional.of(RSAKeySpecification.builder()
                    .privateKey(privateKey.getEncoded())
                    .publicKey(publicKey.getEncoded())
                    .size(RSA_KEY_LENGTH)
                    .build());

        } catch (Exception e) {
            logger.error("Error generating RSA key", e);
            return Optional.empty();
        }
    }

    public static RSAFactory key(RSAKeySpecification rsaKeySpecification) {
        return new RSAFactory(rsaKeySpecification);
    }
}
