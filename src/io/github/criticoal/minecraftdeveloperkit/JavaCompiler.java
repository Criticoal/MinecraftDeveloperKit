package io.github.criticoal.minecraftdeveloperkit;


import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JavaCompiler {
    public static void compile(String sourceFolderPath, String outputFolderPath) throws IOException {
        // 获取编译器
        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new RuntimeException("无法获取编译器");
        }
        // 获取编译任务
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(getJavaFiles(new File(sourceFolderPath)));
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        List<String> optionList = Arrays.asList("-d", outputFolderPath);
        Iterable<String> classes = null;
        Iterable<String> sourcePath = Arrays.asList(sourceFolderPath);
        javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, optionList,
                classes, compilationUnit);
        task.setProcessors(null);
        task.setLocale(null);
        // 执行编译任务
        if (!task.call()) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
                System.out.println(diagnostic.getMessage(null));
            }
        }
    }

    private static List<File> getJavaFiles(File file) {
        List<File> result = new java.util.ArrayList<>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                result.addAll(getJavaFiles(f));
            }
        } else if (file.getName().endsWith(".java")) {
            result.add(file);
        }
        return result;
    }
}
