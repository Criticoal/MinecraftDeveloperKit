package io.github.criticoal.minecraftdeveloperkit;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class JavaIDE extends Application {
    private File currentProject;    // 当前打开的项目文件夹
    private File workspaceFolder;   // workspace文件夹

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 初始化workspace文件夹
        String workspacePath = "./workspace";
        workspaceFolder = new File(workspacePath);
        if (!workspaceFolder.exists()) {
            workspaceFolder.mkdirs();
        }

        // 工具栏
        Button importButton = new Button("导入");
        Button exportButton = new Button("导出");
        Button compileButton = new Button("编译");
        Button settingButton = new Button("设置");
        HBox toolBar = new HBox(10, importButton, exportButton, compileButton, settingButton);
        toolBar.setAlignment(Pos.CENTER_LEFT);

        // 左侧项目框架
        VBox projectBox = new VBox();
        projectBox.getChildren().addAll(new Button("项目1"), new Button("项目2"), new Button("项目3"));
        projectBox.setAlignment(Pos.CENTER);

        // 右侧代码编辑窗口
        CodeEditor codeEditor = new CodeEditor();
        codeEditor.setPrefSize(600, 400);

        // 将左右两侧组件放入SplitPane中，并设置方向
        SplitPane splitPane = new SplitPane(projectBox, codeEditor);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.2);

        // 将工具栏和splitPane组合放入BorderPane中
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolBar);
        borderPane.setCenter(splitPane);

        // 点击导入按钮时，弹出文件选择对话框，选择一个项目文件夹
        importButton.setOnAction(event -> {
            File projectFolder = FileUtils.showOpenFolderDialog(primaryStage, "选择项目文件夹");
            if (projectFolder != null) {
                currentProject = projectFolder;
                // 切换项目时，更新左侧项目框架
                projectBox.getChildren().clear();
                for (File subFile : currentProject.listFiles()) {
                    projectBox.getChildren().add(new Button(subFile.getName()));
                }
            }
        });

        // 点击编译按钮时，调用JavaCompiler类编译项目
        compileButton.setOnAction(event -> {
            try {
                JavaCompiler.compile(currentProject.getPath(), workspaceFolder.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 点击导出按钮时，弹出文件保存对话框，将项目编译成jar文件并保存
        exportButton.setOnAction(event -> {
            try {
                String jarPath = FileUtils.showSaveFileDialog(primaryStage, "保存为jar文件", "项目.jar", "Jar文件 (*.jar)");
                if (jarPath != null) {
                    JavaCompiler.compile(currentProject.getPath(), workspaceFolder.getPath());
                    File jarFile = new File(jarPath);
                    String classPath = workspaceFolder.getPath() + File.separator + currentProject.getName() + File.separator + "bin";
                    JarUtils.createJar(jarFile, classPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        primaryStage.setScene(new Scene(borderPane, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}