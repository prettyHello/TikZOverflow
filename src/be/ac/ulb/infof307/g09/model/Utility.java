package be.ac.ulb.infof307.g09.model;

import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Collection of utility functions used by the model
 * Functions easily imported from project to project and related to file handling
 */
public class Utility {
    /**
     * Decompress file ".tar.gz"
     *
     * @param tarFile  path to source file ".tar.gz"
     * @param destFile destination directory of decompressed file
     */
    public static String untarfile(File tarFile, Path destFile) throws FatalException {
        TarArchiveInputStream tis;
        try {
            FileOutputStream fos;
            String untaredNameFolder = null;
            TarArchiveEntry tarEntry;
            tis = new TarArchiveInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(tarFile))));
            tis.getNextTarEntry();
            while ((tarEntry = tis.getNextTarEntry()) != null) {
                String name = tarEntry.getName();
                untaredNameFolder = name.substring(0, name.indexOf("/"));
                if (!tarEntry.isDirectory()) {
                    File outputFile = new File(destFile.toFile(), tarEntry.getName());
                    outputFile.getParentFile().mkdirs();
                    fos = new FileOutputStream(outputFile);

                    byte[] fileRead = new byte[1024];
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile));

                    int len;
                    while ((len = tis.read(fileRead)) != -1) {
                        bos.write(fileRead, 0, len);
                    }
                    bos.close();
                    fos.close();
                }
            }
            tis.close();
            return untaredNameFolder;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FatalException("File decompression error");
        }
    }

    /**
     * Copy a folder and it's content recursively
     *
     * @param dirSrc  The folder to copy
     * @param dirDest It's new location
     */
    private static void copyDirectory(File dirSrc, File dirDest) {
        if (!dirDest.exists()) {
            dirDest.mkdir();
        }
        String[] files = dirSrc.list();
        for (String f : files) {
            File srcF = new File(dirSrc, f);
            File destF = new File(dirDest, f);
            copy(srcF, destF);
        }
    }

    /**
     * Copy a simple file
     *
     * @param dirSrc  The file to copy
     * @param dirDest The new location
     * @throws FatalException if file was deleted or if I/O Exception
     */
    private static void copyFile(File dirSrc, File dirDest) throws FatalException {
        try {
            InputStream in = new FileInputStream(dirSrc);
            OutputStream out = new FileOutputStream(dirDest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            throw new FatalException("Couldn't copy file");
        } catch (IOException e) {
            throw new FatalException("IOException in copyFile");
        }
    }

    /**
     * Copy a folder with all it's files recursively
     * Can copy simple file as well
     *
     * @param dirSrc  The file or folder to copy
     * @param dirDest The location in which to copy said file/folder
     * @throws FatalException if file was deleted or if I/O Exception
     */
    public static void copy(File dirSrc, File dirDest) throws FatalException {
        if (dirSrc.isDirectory()) {
            copyDirectory(dirSrc, dirDest);
        } else {
            copyFile(dirSrc, dirDest);
        }
    }

    /**
     * Delete the give file/directory
     *
     * @param dir
     * @throws FatalException File permission problem
     * @throws BizzException  No file or directory not empty
     */
    public static void deleteFile(File dir) throws FatalException, BizzException {
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
                throw new BizzException(dir + " no such file or directory");
            } catch (DirectoryNotEmptyException e) {
                throw new FatalException("What we thought was a file was a directory"); //should never happen as we check if dir is a directory in the if
            } catch (IOException e) {
                throw new FatalException(" File permission problems for delete " + dir + " | "+ e.getMessage());
            }
        }
    }

    /**
     * Try to Delete the give file/directory, and do nothing if it doesnt exist
     *
     * @param dir
     * @throws FatalException File permission problem
     * @throws BizzException  No file or directory not empty
     */
    public static void deleteFileSilent(File dir) {
        try {
            deleteFile(dir);
        } catch (BizzException e) {
            // delete file only launch a BizzException if the file doesn't exist
        }
    }

    /**
     * create an empty ".tar.gz" folder in which compressed files will be added
     *
     * @param folderProject      path to the folder we need to compress
     * @param fileTarDestination path to destination of the compressed file
     * @return
     * @throws IOException
     * @throws BizzException
     */
    public static void createTarGz(String folderProject, String fileTarDestination) throws FatalException {
        TarArchiveOutputStream archiveTarGz = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileTarDestination));
            GZIPOutputStream outputGZip = new GZIPOutputStream(new BufferedOutputStream(fileOutputStream));
            archiveTarGz = new TarArchiveOutputStream(outputGZip);
            addFileToArchiveTarGz(folderProject, "", archiveTarGz);
            archiveTarGz.close();
        } catch (IOException e) {
            throw new FatalException("Impossible to export the project");
        }
    }

    /**
     * Add a file into an existing ".tar.gz" file
     *
     * @param folderProject file to be added
     * @param parent        parent folder of the file to be added
     * @param archiveTarGz  destination of the file
     */
    private static void addFileToArchiveTarGz(String folderProject, String parent, TarArchiveOutputStream archiveTarGz) throws FatalException {
        File file = new File(folderProject);
        String entryName = parent + file.getName();
        try {
            archiveTarGz.putArchiveEntry(new TarArchiveEntry(file, entryName));
            if (file.isFile()) {
                BufferedInputStream fileSelected = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(fileSelected, archiveTarGz);  // copy file in archive
                archiveTarGz.closeArchiveEntry();
                fileSelected.close();
            } else if (file.isDirectory()) {
                archiveTarGz.closeArchiveEntry();
                for (File fileInSubFolder : file.listFiles()) {
                    addFileToArchiveTarGz(fileInSubFolder.getAbsolutePath(), entryName + "/", archiveTarGz);
                }
            }
        } catch (IOException e) {
            throw new FatalException("Impossible to export the project");
        }
    }
}
