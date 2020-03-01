package utilities;

import javafx.scene.Scene;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utility {

    public static final String  UNALLOWED_CHARACTERS_PATTERN = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";

    public static final String WHITE_SPACES_PATTERN = "^[\\s]+|[\\s]+$";

    //TODO Change capital letters
    public static final String EMAIL_PATTERN = "(?:[a-zA-Z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    private Utility(){}

    public static final String getTimeStamp(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        /*System.out.println(formatter.format(date));*/
        return formatter.format(date);

    }

    public static boolean checkPhone(String phone) {
        System.out.println("Checking phone");
        System.out.println(phone.length());
        if ((phone.length() > 9) && (phone.length() < 11)) {
            System.out.println(phone);
            return true;
        } else return false;
    }

    public static boolean checkEmail(String email) {
        System.out.println("Checking email");
        if (email.matches(Utility.EMAIL_PATTERN)) {
            return true;
        }
        return false;
    }

    public static boolean checkFirstName(String firstname) {
        System.out.println("Checking firstname");
        if (firstname.isEmpty() || firstname.matches(Utility.UNALLOWED_CHARACTERS_PATTERN))
            return false;
        return true;
    }


    public static boolean checkLastName(String lastname) {
        System.out.println("Checking lastname");
        if (lastname.isEmpty() || lastname.matches(Utility.UNALLOWED_CHARACTERS_PATTERN))
            return false;
        return true;
    }

    public static boolean comparePasswords(String password1, String password2) {
        System.out.println("Checking password");
        if (password1.equals(password2)) {
            return true;
        } else
            return false;
    }

}
