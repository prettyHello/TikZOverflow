package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Collection of utility functions used in the view
 */
public class Utility {

    public static String ALLOWED_CHARACTERS_PATTERN = "^[_,A-Z|a-z|0-9]+";

    public static final String UNALLOWED_CHARACTERS_PATTERN = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";


    public static final String WHITE_SPACES_PATTERN = "^[\\s]+|[\\s]+$";
    private static String nameArchive1;


    //TODO Change capital letters
    public static final String EMAIL_PATTERN = "(?:[a-zA-Z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    private Utility() {
    }

    public static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);

    }

    /**
     * Show an alert to the user.
     *
     * @param type        Type of alert (warning, etc).
     * @param title       Title of the alert.
     * @param headerText  Header of the alert box.
     * @param contentText Content of the alert box.
     */
    public static void showAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Show the eula in a pop-up box.
     */
    public static void showEula() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End-User license agreement");
        alert.setHeaderText("EULA");

        TextArea ta = new TextArea();
        ta.setWrapText(true);
        ta.setEditable(false);
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("eula.txt");
            if (is == null) {
                throw new IOException("file not found");
            }
            String eula = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            ta.setText(eula);
        } catch (IOException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            ta.setText("An error occurred. Unable to find eula.txt file.");
        }

        alert.getDialogPane().setContent(ta);
        alert.showAndWait();
    }

    /**
     * Checks if any Object in a series is null
     *
     * @param obj the series of Objects
     */
    public static void checkObjects(Object... obj) {
        for (Object o : obj) {
            if (o == null) {
                throw new FatalException("Object is null");
            }
        }
    }

    /**
     * Check if a String is empty.
     *
     * @param chaine  String to check.
     * @param varName Name of the variable, to be used in case a BizzException is thrown.
     * @throws BizzException In case the String is empty.
     */
    public static void checkString(String chaine, String varName) throws BizzException {
        if (chaine == null || chaine.equals("")) {
            throw new BizzException(varName + " is empty");
        }
    }

    /**
     * Decompress file ".tar.gz"
     *
     * @param tarFile  path to source file ".tar.gz"
     * @param destFile destination directory of decompressed file
     */
    public static String unTarFile(File tarFile, Path destFile) {
        TarArchiveInputStream tis = null;
        try {
            FileOutputStream fos = null;
            String untaredNameFolder = null;
            TarArchiveEntry tarEntry = null;
            tis = new TarArchiveInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(tarFile))));
            tis.getNextTarEntry();

            while ((tarEntry = tis.getNextTarEntry()) != null) {

                int it = 0;
                String name = tarEntry.getName();
                untaredNameFolder = name.substring(0, name.indexOf("/"));

                if (tarEntry.isDirectory()) {
                    continue;
                } else {
                    File outputFile = new File(destFile.toFile(), tarEntry.getName());
                    outputFile.getParentFile().mkdirs();
                    fos = new FileOutputStream(outputFile);

                    byte[] fileRead = new byte[1024];
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));

                    int len = 0;
                    while ((len = tis.read(fileRead)) != -1) {
                        bos.write(fileRead, 0, len);
                    }
                    bos.close();
                    fileRead = null;
                    fos.close();
                }
            }
            return untaredNameFolder;
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "File decompression error").showAndWait();
            return null;
        } finally {
            if (tis != null) {
                try {
                    tis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void copy(File dirSrc, File dirDest) throws IOException {

        if (dirSrc.isDirectory()) {
            if (!dirDest.exists()) {
                dirDest.mkdir();
                System.out.println("Dossier " + dirSrc + "  > " + dirDest);
            }
            String files[] = dirSrc.list();

            for (String f : files) {
                File srcF = new File(dirSrc, f);
                File destF = new File(dirDest, f);
                copy(srcF, destF);
            }
        } else {
            InputStream in = new FileInputStream(dirSrc);
            OutputStream out = new FileOutputStream(dirDest);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }


    /**
     * Check the data the users enter while registering of modifying theirs information.
     *
     * @param firstname
     * @param lastname
     * @param email
     * @param firstPassword
     * @param secondPassword
     * @param phone
     * @throws BizzException
     */
    public static void checkUserData(String firstname, String lastname, String email, String firstPassword, String secondPassword, String phone) throws BizzException {
        checkFirstName(firstname);
        checkLastName(lastname);
        checkEmail(email);
        comparePasswords(firstPassword, secondPassword);
        checkPhone(phone);
    }

    /**
     * Check if the phone number length is correct.
     *
     * @param phone
     * @throws BizzException
     */
    public static void checkPhone(String phone) throws BizzException {
        checkString(phone, "phone");
        if (phone.length() < 9)
            throw new BizzException("The PhoneNumber is too short");
        if (phone.length() > 11)
            throw new BizzException("The PhoneNumber is too long");
    }

    /**
     * Check if the email's structure is correct.
     *
     * @param email
     * @throws BizzException
     */
    public static void checkEmail(String email) throws BizzException {
        checkString(email, "email");
        if (!email.matches(Utility.EMAIL_PATTERN))
            throw new BizzException("The email is wrong");
    }

    /**
     * Check if the firstname has no special characters or numbers.
     *
     * @param firstname
     * @throws BizzException
     */
    public static void checkFirstName(String firstname) throws BizzException {
        checkString(firstname, "firstname");
        if (firstname.isEmpty() || firstname.matches(Utility.UNALLOWED_CHARACTERS_PATTERN))
            throw new BizzException("The firstname has unallowed characters");
    }

    /**
     * Check if the lastname has no special characters or numbers.
     *
     * @param lastname
     * @throws BizzException
     */
    public static void checkLastName(String lastname) throws BizzException {
        checkString(lastname, "lastname");
        if (lastname.isEmpty() || lastname.matches(Utility.UNALLOWED_CHARACTERS_PATTERN))
            throw new BizzException("The lastname has unallowed characters");
    }

    /**
     * Check if the passwords introduced are the same.
     *
     * @param password1
     * @param password2
     * @throws BizzException
     */
    public static void comparePasswords(String password1, String password2) throws BizzException {
        if (!password1.equals(password2))
            throw new BizzException("The passwords are not the sames");
        checkString(password1, "password");
        checkString(password2, "password");
    }


    public static void deleteFile(File dir) {

        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            if (listFiles != null) {
                for (File entry : listFiles) {
                    deleteFile(entry);
                }
                dir.delete();
            }
        } else {
            try {
                Files.delete(dir.toPath());
            } catch (NoSuchFileException e) {
                new Alert(Alert.AlertType.ERROR, dir + " no such file or directory").showAndWait();
            } catch (DirectoryNotEmptyException e) {
                new Alert(Alert.AlertType.ERROR, dir + " not empty").showAndWait();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, " File permission problems for delete " + dir).showAndWait();
            }
        }
    }
}
