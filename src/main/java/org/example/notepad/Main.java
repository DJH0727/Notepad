package org.example.notepad;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import java.io.*;


import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main extends Application {


    public static void main(String[] args)
    {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        File file=null;

       newStage(file,primaryStage);
    }



//新建窗口
    public void newStage(File file,Stage  primaryStage)
    {
        //System.out.println(file.getName());

        if(file==null)
            primaryStage.setTitle("Notepad");
        else
            primaryStage.setTitle(file.getName());

       // primaryStage.setTitle("Notepad");

        Image icon = new Image("/icon.png");
        if (icon.isError()) {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, "Error loading icon");
        } else {
            primaryStage.getIcons().add(icon);
        }


        NotepadTextArea notepadTextArea;
        NotepadMenu notepadMenu;
        NotepadStatusBar statusBar;
        BorderPane borderPane;

        // 创建文本编辑区域
        notepadTextArea = new NotepadTextArea();
        TextArea textArea = notepadTextArea.getTextArea();
        NotepadTheme.initTheme(textArea);
        if(file!=null) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), StandardCharsets.UTF_8))) {

                StringBuilder sb = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1) {
                    sb.append((char) c);
                }
                textArea.setText(sb.toString());


            } catch (Exception e) {
                Logger  logger = Logger.getLogger(Main.class.getName());
                logger.log(Level.SEVERE, "Error reading file", e);
            }
        }



        // 创建菜单
        notepadMenu = new NotepadMenu(notepadTextArea,file);
        MenuBar menuBar = notepadMenu.getMenuBar();
        // 创建状态栏
        statusBar = new NotepadStatusBar(notepadTextArea);
        HBox statusBarHBox = statusBar.getStatusBar();


        // 设置布局
        VBox MenuBox = new VBox(menuBar);
        borderPane = new BorderPane();
        borderPane.setTop(MenuBox);
        borderPane.setCenter(textArea);
        borderPane.setBottom(statusBarHBox);
        //布局和状态栏传入Menu类，以便Menu类可以修改布局和状态栏
        //System.out.println(file);



        //borderPane.setBottom(null);
        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(scene);
        scene.getStylesheets().add("text-area.css");//
        //传入stage
        notepadMenu.setBorderPane(borderPane,statusBar,primaryStage);
        notepadMenu.initFileMenu(primaryStage,file);
        notepadMenu.initEditMenu(MenuBox);
        notepadMenu.filemenu.CheckIfSave();//是否保存

        notepadTextArea.setFile(file);

        notepadTextArea.CheckSave(primaryStage);

        primaryStage.show();





    }



}
