package main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//Add java logging

public class Main {

    private static final String SOURCE_DIR = Paths.get(System.getProperty("user.home"), "Desktop").toString();
    private static final String VIDEOS_DIR = Paths.get(SOURCE_DIR, "Videos").toString();
    private static final String IMAGES_DIR = Paths.get(SOURCE_DIR, "Images").toString();
    private static final String DOCUMENTS_DIR = Paths.get(SOURCE_DIR, "Documents").toString();

    private static final String[] videoExtensions = { ".mp4", ".avi", ".mov", ".mkv", ".flv", ".wmv" };
    private static final String[] imageExtensions = { ".png", ".avif", ".gif", ".jpg", ".jpeg", ".svg", ".webp" };
    private static final String[] docExtensions = { ".txt", ".pdf", ".docx", ".xlsx", ".pptx" };

    public static void main(String[] args) throws IOException {

        File sourceFolder = new File(SOURCE_DIR);

        File[] files = sourceFolder.listFiles();
        if (files == null) {
            System.out.println("There are no files");
            return;
        } else {
            createFolder(VIDEOS_DIR);
            createFolder(IMAGES_DIR);
            createFolder(DOCUMENTS_DIR);
            organize(files);
        }

    }

    private static void move(File f, String destination) throws IOException {
        Path source = f.toPath();
        Path target = Paths.get(destination, f.getName());

        try {
            Files.move(source, target);
            System.out.println("Moved" + f.getName() + " to " + destination);
        } catch (IOException e) {
            System.err.println("Failed to move " + f.getName() + ": " + e.getMessage());
        }
    }

    private static void organize(File[] files) throws IOException {

        for (int i = 0; i < files.length; i++) {

            String fileName = files[i].getName().toLowerCase();
            if (!files[i].isFile() || !fileName.contains(".")) {
                continue;
            }
            String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
           

           Set<String> videoExtSet = new HashSet<>(Arrays.asList(videoExtensions));
           Set<String> imageExtSet = new HashSet<>(Arrays.asList(imageExtensions));
           Set<String> docExtSet = new HashSet<>(Arrays.asList(docExtensions));

            if (videoExtSet.contains(ext)) {
                move(files[i], VIDEOS_DIR);
            } else if (imageExtSet.contains(ext)) {
                move(files[i], IMAGES_DIR);
            } else if (docExtSet.contains(ext)) {
                move(files[i], DOCUMENTS_DIR);
            }
        }

    }

    private static String createFolder(String dir) {
        File folder = new File(
                dir);

        boolean Docscreated = folder.mkdir();

        if (Docscreated) {
            System.out.println("Folder " + folder.getName() + " created successfully.");
        } else {
            System.out.println("Folder" + folder.getName() + "already exists.");
        }

        return dir;
    }

}
    
    
        





    

