package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.IvParameterSpec;

public final class Crypto {
    private static final SecureRandom srandom = new SecureRandom();

    public static void encryptDirectory(String password, String path) {
        File directory = new File(path);
        File[] filesOfDirectory = directory.listFiles();

        for (File file : filesOfDirectory) {
            if (file.isFile()) {
                if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".bin"))
                    encrypt(password, file.getPath());
            }
        }
    }

    public static void decryptDirectory(String password, String path) throws BizzException, FatalException {
        File directory = new File(path);
        File[] filesOfDirectory = directory.listFiles();

        for (File file : filesOfDirectory) {
            if (file.isFile()) {
                System.out.println(file.getName().substring(file.getName().lastIndexOf(".")));
                if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".enc")) {
                        decrypt(password, file);
                }
            }
        }

    }

    public static String hashingFile(String fileToHash) throws FatalException{
        try {
            int i;
            String byteToHash = "";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream file = new FileInputStream(fileToHash);
            while((i = file.read())!=-1){
                byteToHash += (char)i;
            }
            byte[] encodedhash = md.digest(
                    byteToHash.getBytes(StandardCharsets.UTF_8));
            System.out.println("Hash dans la fonction hashingFile: " +bytesToHex(encodedhash));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new FatalException("Hashing error " + e.getMessage());
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private static void encrypt(String password, String fileToEncypt) {

        try {
            byte[] salt = genSalt();
            byte[] iv = genIv();
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec skey = genAES(salt, password);
            FileOutputStream out = genOutputFile(fileToEncypt, salt, iv);
            Cipher ci = cipherContent(skey, ivspec);
            writeToOutput(fileToEncypt, ci, out);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static private byte[] genSalt() {
        byte[] salt = new byte[8];
        srandom.nextBytes(salt);
        return salt;
    }

    static private byte[] genIv() {
        byte[] iv = new byte[128 / 8];
        srandom.nextBytes(iv);
        return iv;
    }

    static private FileOutputStream genOutputFile(String fileName, byte[] salt, byte[] iv) {
        try {
            FileOutputStream out = new FileOutputStream(fileName + ".enc");
            out.write(salt);
            out.write(iv);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    static private SecretKeySpec genAES(byte[] salt, String password) {
        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec skey = new SecretKeySpec(tmp.getEncoded(), "AES");
            return skey;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    private static Cipher cipherContent(SecretKeySpec skey, IvParameterSpec ivspec) {
        try {
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
            return ci;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeToOutput(String fileName, Cipher ci, FileOutputStream out) {
        try (FileInputStream in = new FileInputStream(fileName)) {
            processFile(ci, in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void decrypt(String password, File fileToDecrypt) throws BizzException, FatalException {
        try {
            FileInputStream in = new FileInputStream(fileToDecrypt);
            byte[] salt = new byte[8], iv = new byte[128 / 8];
            in.read(salt);
            in.read(iv);
            SecretKeySpec skey = genAES(salt, password);
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));
            int indexlastDot = fileToDecrypt.getName().lastIndexOf(".");
            String newFilename = fileToDecrypt.getName().substring(0, indexlastDot);
            String path = fileToDecrypt.getParent();
            FileOutputStream out = new FileOutputStream(path + File.separator + newFilename);
            processFile(ci, in, out);

        } catch (BadPaddingException e) {
            throw new BizzException("Invalid password!");
        } catch (IOException e) {
            throw new FatalException("Unexpected error : " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new FatalException("Unexpected error : " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new FatalException("Unexpected error : " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new FatalException("Unexpected error : " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new FatalException("Unexpected error : " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new FatalException("Unexpected error : " + e.getMessage());
        }

    }


    static private void processFile(Cipher ci, InputStream in, OutputStream out)
            throws
            javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException {
        byte[] ibuf = new byte[1024];
        int len;
        while ((len = in.read(ibuf)) != -1) {
            byte[] obuf = ci.update(ibuf, 0, len);
            if (obuf != null) out.write(obuf);
        }
        byte[] obuf = ci.doFinal();
        if (obuf != null) out.write(obuf);

    }


}

