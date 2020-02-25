package view.registration;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utility {

    private Utility(){}

    public static final String getTimeStamp(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        /*System.out.println(formatter.format(date));*/
        return formatter.format(date);

    }
}
