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

/**
 * Source: https://www.novixys.com/blog/aes-encryption-decryption-password-java/
 */
public final class Crypto {
    private static final SecureRandom srandom = new SecureRandom();

    /**
     * This method will encrypt the given .bin file with the given password
     * @param password
     * @param path
     */
    public static void encryptDirectory(String password, String path) {
        File directory = new File(path);
        File[] filesOfDirectory = directory.listFiles();

        for (File file : filesOfDirectory) {
            if (file.isFile()) {
                if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".bin"))
                    encrypt(password, file.getPath());
                    Utility.deleteFileSilent(file);
            }
        }
    }

    /**
     * This method will decrypt the given .enc file with the given password
     * @param password
     * @param path
     * @throws BizzException
     * @throws FatalException
     */
    public static void decryptDirectory(String password, String path) throws BizzException, FatalException {
        File directory = new File(path);
        File[] filesOfDirectory = directory.listFiles();

        for (File file : filesOfDirectory) {
            if (file.isFile()) {
                if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".enc")) {
                        decrypt(password, file);
                        Utility.deleteFileSilent(file);
                }
            }
        }

    }

    /**
     * This method hash the given file with SHA-256
     * @param fileToHash
     * @return
     * @throws FatalException in case of issue directly hashed related
     * @throws IOException we are forced to throw this exception as it is a parent of FileNotFoundException, which we want to handle in a specific way
     */
    public static String hashingFile(File fileToHash) throws FatalException {
        try {
            int i;
            String byteToHash = "";
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream file = new FileInputStream(fileToHash);
            while((i = file.read())!=-1){
                byteToHash += (char)i;
            }
            file.close();
            byte[] encodedhash = md.digest(byteToHash.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new FatalException("Hashing error " + e.getMessage());
        }
    }

    /**
     * This method read bytes and convert them to hexadecimal
     * @param hash the bytes to convert
     * @return the hexadecimal in string format
     */
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * This method use AES to encrypt and a salt on the given file.
     * @param password
     * @param fileToEncypt
     * @throws FatalException
     */
    private static void encrypt(String password, String fileToEncypt) throws  FatalException {

        try {
            byte[] salt = genSalt();
            byte[] iv = genIv();
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec skey = genAES(salt, password);
            FileOutputStream out = genOutputFile(fileToEncypt, salt, iv);
            Cipher ci = cipherContent(skey, ivspec);
            writeToOutput(fileToEncypt, ci, out);
            out.close();
        } catch (IOException e) {
            throw new FatalException("Ressource couldn't be closed " + e.getMessage());
        }

    }

    /**
     * This method generate a salt
     * @return the generated salt
     */
    static private byte[] genSalt() {
        byte[] salt = new byte[8];
        srandom.nextBytes(salt);
        return salt;
    }

    /**
     * This method generate an initialization vector
     * @return the IV
     */
    static private byte[] genIv() {
        byte[] iv = new byte[128 / 8];
        srandom.nextBytes(iv);
        return iv;
    }

    /**
     * This method create the new encrypted file
     * @param fileName
     * @param salt
     * @param iv
     * @return
     */
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

    /**
     * This method generate secret key in AES
     * @param salt
     * @param password
     * @return
     */
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

    /**
     * This method encrypt the text with the secret key and the initialization vector
     * @param skey
     * @param ivspec
     * @return
     */
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

    /**
     * This method transform the input in the output
     * @param fileName
     * @param ci
     * @param out
     */
    private static void writeToOutput(String fileName, Cipher ci, FileOutputStream out) {
        try (FileInputStream in = new FileInputStream(fileName)) {
            processFile(ci, in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method decrypt the given file with the given password using AES
     * @param password
     * @param fileToDecrypt
     * @throws BizzException
     * @throws FatalException
     */
    private static void decrypt(String password, File fileToDecrypt) throws BizzException, FatalException {
        FileInputStream in = null;
        FileOutputStream out = null;
        String path = null;
        String newFilename = null;
        try{
            try {
                in = new FileInputStream(fileToDecrypt);
                byte[] salt = new byte[8], iv = new byte[128 / 8];
                in.read(salt);
                in.read(iv);
                SecretKeySpec skey = genAES(salt, password);
                Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
                ci.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));
                int indexlastDot = fileToDecrypt.getName().lastIndexOf(".");
                newFilename = fileToDecrypt.getName().substring(0, indexlastDot);
                path = fileToDecrypt.getParent();
                out = new FileOutputStream(path + File.separator + newFilename);
                processFile(ci, in, out);
            } catch (BadPaddingException e) {
                //if the password is incorrect we need to remove the output file
                if(out != null) {
                    out.close();
                    Utility.deleteFileSilent(new File(path + File.separator + newFilename));
                }
                throw new BizzException("Invalid password!");
            } catch (IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
                throw new FatalException("Unexpected error : " + e.getMessage());
            }finally {
                if(in!=null){
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            }
        }catch (IOException e){
            throw new FatalException("Unexpected error : " + e.getMessage());
        }


    }

    /**
     * This method process the given input with the ciphertext
     * @param ci
     * @param in
     * @param out
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @throws java.io.IOException
     */
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

