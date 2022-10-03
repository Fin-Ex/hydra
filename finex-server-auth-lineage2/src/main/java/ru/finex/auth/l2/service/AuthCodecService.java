package ru.finex.auth.l2.service;

import com.mycila.jmx.annotation.JmxBean;
import com.mycila.jmx.annotation.JmxMethod;
import lombok.Getter;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@JmxBean
@Singleton
public class AuthCodecService {

    private KeyPair pair;
    @Getter
    private SecretKey blowfishKey;

    public AuthCodecService() throws GeneralSecurityException {
        pair = generatePair();
        blowfishKey = generateBlowfish();
    }

    private KeyPair generatePair() throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec params = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
        generator.initialize(params);
        return generator.generateKeyPair();
    }

    private SecretKey generateBlowfish() throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance("Blowfish");
        return generator.generateKey();
    }

    public PrivateKey getPrivateKey() {
        return pair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return pair.getPublic();
    }

    @JmxMethod(name = "Update RSA Pair", description = "Update RSA Pair what used in transport with client.")
    public void updatePair() throws GeneralSecurityException {
        pair = generatePair();
    }

    @JmxMethod(name = "Update Blowfish key", description = "Update Blowfish key what used in transport with client.")
    public void updateBlowfish() throws GeneralSecurityException {
        blowfishKey = generateBlowfish();
    }

}
