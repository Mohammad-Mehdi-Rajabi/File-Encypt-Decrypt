package Core.Encrypt;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Encrypt {
    public static String sourcePath;
    public static String distPath;

    static {
        sourcePath = null;
        distPath = null;
    }

    public static void encryptDecrypt(String key, int cipherMode, File in, File out)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());

        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = skf.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        if (cipherMode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            write(cis, fos);
        } else if (cipherMode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, SecureRandom.getInstance("SHA1PRNG"));
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            write(fis, cos);
        }
    }

    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[64];
        int numOfBytesRead;
        while ((numOfBytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numOfBytesRead);
        }
        out.close();
        in.close();
    }

    public static void encrypt(String source, String dist, String password) throws InvalidKeySpecException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String[] split = source.split("\\\\");
        String name = split[split.length - 1];
        String[] split1 = name.split("\\.");
        name = split1[0];
        String postfix = split1[1];
        String distFullPath = dist + "\\" + name + "_C." + postfix;
        distPath = distFullPath;
        sourcePath = source;
        File plaintext = new File(source);
        File encrypted = new File(distFullPath);


        encryptDecrypt(password, Cipher.ENCRYPT_MODE, plaintext, encrypted);


    }
}
