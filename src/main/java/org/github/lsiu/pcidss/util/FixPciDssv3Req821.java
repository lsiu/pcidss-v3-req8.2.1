package org.github.lsiu.pcidss.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * This utility is to address PCI 3.0 - Requirement 8.2.1. Specifically,
 * <ul>
 * <li>8.2.1.b - For a sample of system components, examine password files to
 * verify that passwords are unreadable during storage</li>
 * <li>8.2.1.d Additional testing procedure for service providers: Observe
 * password files to verify that customer passwords are unreadable during
 * storage.</li>
 * </ul>
 *
 * @See Payment Card Industry (PCI) Data Security Standard v 3.0 -
 * https://www.pcisecuritystandards.org/documents/PCI_DSS_v3.pdf
 */
public class FixPciDssv3Req821 {

    // Following keys maybe generated via openssl using the following command
    //   openssl enc -aes-256-cbc -k secret -P -md sha
    // The "salt" value is not used. It is used in combination with the passphrase, the value after the "-k" argument,
    // to generate the "key" value
    private static final byte[] key = decodeHex("DC84F118EF16A7B1D586FA15D4D3F659EFD19C3FBC010CE14A2D7B20E24BEDD0");
    private static final byte[] iv = decodeHex("0AC2F416A3FEEA8CEFB8492200B953C9");

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
        if (args == null || args.length != 1) {
            System.err.println("Expect one argument of base64 encoded and encrypted text for decryption");
            System.exit(-1);
        }
        System.out.println(decrypt(args[0]));
    }

    public static String decrypt(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        final Cipher dc = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dc.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv, 0, dc.getBlockSize()));
        byte[] original = dc.doFinal(encrypted);
        return new String(original);
    }

    public static String decrypt(String base64encodedAndEncrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        return decrypt(DatatypeConverter.parseBase64Binary(base64encodedAndEncrypted));
    }

    /*
     May also encrypt with openssl using
     echo -n "testa" | openssl enc -aes-256-cbc -e -K <key in hex tring format> -iv <salt in hex string format> -a
     */
    public static String encrypt2base64String(String secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv, 0, cipher.getBlockSize()));
        return DatatypeConverter.printBase64Binary((cipher.doFinal(secret.getBytes())));
    }
    
    // reference - http://stackoverflow.com/questions/15594518/using-java-to-decrypt-openssl-aes-256-cbc-using-provided-key-iv/15595200#15595200
    public static final byte[] decodeHex(final String encoded) {
        byte[] decoded = new BigInteger(encoded, 16).toByteArray();
        if (decoded[0] == 0) {
            final byte[] tmp = new byte[decoded.length - 1];
            System.arraycopy(decoded, 1, tmp, 0, tmp.length);
            decoded = tmp;
        }
        return decoded;
    }

}
