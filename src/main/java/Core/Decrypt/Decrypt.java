package Core.Decrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Decrypt {
    public static String distPath;
    public static String sourcePath;

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

    public static void decrypt(String source, String dist, String password) throws InvalidKeySpecException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String[] split = source.split("\\\\");
        String name = split[split.length - 1];
        String[] split1 = name.split("\\.");
        name = split1[0];
        if ((name.charAt(name.length() - 2)) == '_' && (name.charAt(name.length() - 1)) == 'C') {
            StringBuilder stringBuilder = new StringBuilder(name);
            stringBuilder.replace(name.length() - 1, name.length(), "D");
            name = stringBuilder.toString();
        } else {
            StringBuilder s = new StringBuilder(name);
            s.append("_D");
            name = s.toString();
        }
        String postfix = split1[1];
        String distFullPath = dist + "\\" + name + "." + postfix;
        distPath = distFullPath;
        sourcePath = source;
        File encrypted = new File(source);
        File decrypted = new File(distFullPath);

        encryptDecrypt(password, Cipher.DECRYPT_MODE, encrypted, decrypted);

    }
}
