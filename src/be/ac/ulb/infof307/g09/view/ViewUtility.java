package be.ac.ulb.infof307.g09.view;

import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.view.editor.PasswordDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Collection of utility functions used in the view and related to javaFX
 */
public class ViewUtility {
    public static final String ALLOWED_CHARACTERS_PATTERN = "^[_,A-Z|a-z|0-9]+";
    public static final String UNALLOWED_CHARACTERS_PATTERN = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";
    public static final String WHITE_SPACES_PATTERN = "^[\\s]+|[\\s]+$";
    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

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
        InputStream is = null;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            is = classloader.getResourceAsStream("eula.txt");
            if (is == null) {
                throw new IOException("file not found");
            }
            String eula = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
            ta.setText(eula);
        } catch (IOException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            ta.setText("An error occurred. Unable to find eula.txt file.");
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    alert.getDialogPane().setContent(ta);
                    alert.showAndWait();
                }
            }
        }

        alert.getDialogPane().setContent(ta);
        alert.showAndWait();
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
        ControllerUtility.checkString(phone, "phone");
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
        ControllerUtility.checkString(email, "email");
        if (!email.matches(EMAIL_PATTERN))
            throw new BizzException("The email is wrong");
    }

    /**
     * Check if the firstname has no special characters or numbers.
     *
     * @param firstname
     * @throws BizzException
     */
    public static void checkFirstName(String firstname) throws BizzException {
        ControllerUtility.checkString(firstname, "firstname");
        if (firstname.isEmpty() || firstname.matches(UNALLOWED_CHARACTERS_PATTERN))
            throw new BizzException("The firstname has unallowed characters");
    }

    /**
     * Check if the lastname has no special characters or numbers.
     *
     * @param lastname
     * @throws BizzException
     */
    public static void checkLastName(String lastname) throws BizzException {
        ControllerUtility.checkString(lastname, "lastname");
        if (lastname.isEmpty() || lastname.matches(UNALLOWED_CHARACTERS_PATTERN))
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
        ControllerUtility.checkString(password1, "password");
        ControllerUtility.checkString(password2, "password");
    }

    /**
     * Used to format textFields in the view
     *
     * @return a unary operator that applies a transformation to text
     */
    public static UnaryOperator<TextFormatter.Change> textFormatterUnary() {
        return change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
    }

    /**
     * Used to ask the user the project password
     *
     * @return the entered password or null if the input wasn't valid
     */
    public static String askProjectPassword() {
        PasswordDialog pd = new PasswordDialog();
        pd.setTitle("Project password");
        pd.setHeaderText("Please enter your password to unlock your project");
        pd.setContentText("Password:");
        Optional<String> password = pd.showAndWait();
        if(!password.isPresent()){
            //If the user cancel the action
            return null;
        }
        else if (password.get().matches(ViewUtility.ALLOWED_CHARACTERS_PATTERN)) {
            return password.get();
        } else {
            showAlert(Alert.AlertType.WARNING, "Project", "Please enter a valid password", "Please enter a valid password");
            return null;
        }
    }
}
