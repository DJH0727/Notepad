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
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Notepad");




        // 创建文本编辑区域
        NotepadTextArea notepadTextArea = new NotepadTextArea();
        TextArea textArea = notepadTextArea.getTextArea();

        // 创建菜单
        NotepadMenu notepadMenu = new NotepadMenu(textArea);
        MenuBar menuBar = notepadMenu.getMenuBar();
        // 创建状态栏
        NotepadStatusBar statusBar = new NotepadStatusBar(textArea);
        HBox statusBarHBox = statusBar.getStatusBar();


        // 设置布局
        //VBox vBox = new VBox(menuBar, textArea, statusBarHBox);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);
        borderPane.setBottom(statusBarHBox);

        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
