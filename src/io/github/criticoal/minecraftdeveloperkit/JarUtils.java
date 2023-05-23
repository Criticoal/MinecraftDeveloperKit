package io.github.criticoal.minecraftdeveloperkit;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarUtils {
    public static void createJar(File jarFile, String sourceFolderPath) throws IOException {
        JarOutputStream jarOut = null;
        try {
            jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarFile)));
            addDirectoryToJar(sourceFolderPath, jarOut, "");
        } finally {
            if (jarOut != null) {
                jarOut.close();
            }
        }
    }

    private static void addDirectoryToJar(String path, JarOutputStream jarOut, String prefix) throws IOException {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {   // 处理文件
                    jarOut.putNextEntry(new JarEntry(prefix + file.getName()));
                    FileInputStream in = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int n = in.read(buffer);
                    while (n != -1) {
                        jarOut.write(buffer, 0, n);
                        n = in.read(buffer);
                    }
                    in.close();
                    jarOut.closeEntry();
                } else {    // 处理目录
                    String newPrefix = prefix + file.getName() + "/";
                    jarOut.putNextEntry(new JarEntry(newPrefix));
                    jarOut.closeEntry();
                    addDirectoryToJar(file.getAbsolutePath(), jarOut, newPrefix);
                }
            }
        }
    }
}
