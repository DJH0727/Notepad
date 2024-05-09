package org.example.notepad;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {

        File file=null;

        primaryStage.setTitle("Notepad");




        NotepadTextArea notepadTextArea;
        NotepadMenu notepadMenu;
        NotepadStatusBar statusBar;
        BorderPane borderPane;

        // 创建文本编辑区域
        notepadTextArea = new NotepadTextArea();
        TextArea textArea = notepadTextArea.getTextArea();


        // 创建菜单
        notepadMenu = new NotepadMenu(notepadTextArea,file);
        MenuBar menuBar = notepadMenu.getMenuBar();
        // 创建状态栏
        statusBar = new NotepadStatusBar(notepadTextArea);
        HBox statusBarHBox = statusBar.getStatusBar();


        // 设置布局
        //VBox vBox = new VBox(menuBar, textArea, statusBarHBox);
        borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);
        borderPane.setBottom(statusBarHBox);
        //布局和状态栏传入Menu类，以便Menu类可以修改布局和状态栏
        notepadMenu.setBorderPane(borderPane,statusBar);


        //borderPane.setBottom(null);
        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(scene);
        //传入stage
        notepadMenu.initFileMenu(primaryStage,file);
        notepadMenu.filemenu.CheckIfSave();//是否保存


        notepadTextArea.setFile(file);

        notepadTextArea.CheckSave(primaryStage);
        primaryStage.show();
    }



//新建窗口
    public void newStage(File file,Stage  primaryStage)
    {
        if(file==null)
            primaryStage.setTitle("Notepad");
        else
            primaryStage.setTitle(file.getName());



        NotepadTextArea notepadTextArea;
        NotepadMenu notepadMenu;
        NotepadStatusBar statusBar;
        BorderPane borderPane;

        // 创建文本编辑区域
        notepadTextArea = new NotepadTextArea();
        TextArea textArea = notepadTextArea.getTextArea();
        if(file!=null) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line);
                    textArea.appendText("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        // 创建菜单
        notepadMenu = new NotepadMenu(notepadTextArea,file);
        MenuBar menuBar = notepadMenu.getMenuBar();
        // 创建状态栏
        statusBar = new NotepadStatusBar(notepadTextArea);
        HBox statusBarHBox = statusBar.getStatusBar();


        // 设置布局
        //VBox vBox = new VBox(menuBar, textArea, statusBarHBox);
        borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);
        borderPane.setBottom(statusBarHBox);
        //布局和状态栏传入Menu类，以便Menu类可以修改布局和状态栏
        //System.out.println(file);
        notepadMenu.setBorderPane(borderPane,statusBar);


        //borderPane.setBottom(null);
        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(scene);
        //传入stage
        notepadMenu.initFileMenu(primaryStage,file);
        notepadMenu.filemenu.CheckIfSave();//是否保存

        notepadTextArea.setFile(file);

        notepadTextArea.CheckSave(primaryStage);

        primaryStage.show();


    }
}
