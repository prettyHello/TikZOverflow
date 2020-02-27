package utilities;

import javafx.scene.Scene;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

public final class Utility {

    private Utility(){}

    public static final String getTimeStamp(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        /*System.out.println(formatter.format(date));*/
        return formatter.format(date);

    }

    public static void unTarFile(File tarFile, Path destFile)
    {
        TarArchiveInputStream tis = null;
        try {
            FileInputStream fis = new FileInputStream(tarFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(fis));
            tis = new TarArchiveInputStream(gzipInputStream);
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tis.getNextTarEntry()) != null)
            {
                if(tarEntry.isDirectory())
                {
                    continue;
                }else {
                    File outputFile = new File(destFile.toString() + File.separator + tarEntry.getName());
                    outputFile.getParentFile().mkdirs();
                    IOUtils.copy(tis, new FileOutputStream(outputFile));
                }
            }
        }catch(IOException ex) {
            System.out.println("Error while untarring a file- " + ex.getMessage());
        }finally { if(tis != null) { try { tis.close(); } catch (IOException e) { e.printStackTrace(); } } }
    }


    public static String HachFonction (String name) {

        return name ;
    }


}
