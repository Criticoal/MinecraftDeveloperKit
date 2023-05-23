package io.github.criticoal.minecraftdeveloperkit;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileUtils {
    public static File showOpenFolderDialog(Stage stage, String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(stage);
    }

    public static String showSaveFileDialog(Stage stage, String title, String defaultName, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(defaultName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", extensions));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            return file.getPath();
        } else {
            return null;
        }
    }
}