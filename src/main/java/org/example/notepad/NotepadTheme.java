package org.example.notepad;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NotepadTheme {

    static int savedSize=15;

    static String savedFont="System";

    static Color savedColor = Color.BLACK;
    static FontWeight savedFontWeight = FontWeight.NORMAL;

    static FontPosture savedFontPosture = FontPosture.REGULAR;

    public static void initTheme(TextArea textArea) {
        textArea.setStyle("-fx-text-fill: " + toHexString(savedColor) + ";");
        textArea.setFont(Font.font(savedFont, savedFontWeight, savedFontPosture, savedSize));
    }


    boolean isChange = false;
    TextArea textArea;




    //演示文本
    Text demoText = new Text("你好，世界！\nHello, World!\n1234567890");
    public NotepadTheme(TextArea textArea) {

        this.textArea = textArea;
        currentSize = savedSize;
        currentColor = savedColor;
        currentFont = savedFont;
        currentFontWeight = savedFontWeight;
        currentFontPosture = savedFontPosture;
        demoText.setFont(Font.font(savedFont, savedFontWeight, savedFontPosture, savedSize));
        demoText.setFill(savedColor);
        boldCheck.setSelected(savedFontWeight == FontWeight.BOLD);
        italicCheck.setSelected(savedFontPosture == FontPosture.ITALIC);

    }


    Integer currentSize;
    ColorPicker colorPicker;
    ComboBox<String> fontComboBox = new ComboBox<>();
    ComboBox<String> sizeComboBox = new ComboBox<>();
    String[] sizes= {"8","9","10","11","12","13","14","15","16","18","20","22","24","26","28","36","48","72"};
    Color currentColor;
    String currentFont;
    CheckBox boldCheck = new CheckBox("粗体");
    CheckBox italicCheck = new CheckBox("斜体");

    FontWeight currentFontWeight;
    FontPosture currentFontPosture;

    public void showChangeStage(Stage stage) {

        if (isChange) return;



        Stage primaryStage = new Stage();



        sizeComboBox.getItems().addAll(sizes);
        sizeComboBox.setValue(currentSize.toString());
        sizeComboBox.setEditable(true);

        Label fontLabel = new Label("更改字体:");
        fontComboBox.getItems().addAll(Font.getFamilies());
        fontComboBox.setValue(currentFont);
        fontComboBox.setVisibleRowCount(10);//设置可见行数

        Label sizelabel = new Label("字体大小:");
        Label colorLabel = new Label("更改颜色:");
         colorPicker = new ColorPicker();
        colorPicker.setValue(currentColor);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.add(sizelabel, 0, 0);
        gridPane.add(sizeComboBox, 1, 0);
        gridPane.add(fontLabel, 0, 1);
        gridPane.add(fontComboBox, 1, 1);
        gridPane.add(colorLabel, 0, 2);
        gridPane.add(colorPicker, 1, 2);
        gridPane.add(boldCheck, 0, 3);
        gridPane.add(italicCheck, 1, 3);
        gridPane.add(new Label("演示文本："), 0, 4);

        VBox demoBox = new VBox(demoText);
        demoBox.setAlignment(Pos.CENTER);
        VBox setBox = new VBox(gridPane, demoBox);

        //gridPane.add(demoText, 0, 3);
        //gridPane.setAlignment(Pos.CENTER);
        boldCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                currentFontWeight = FontWeight.BOLD;
                savedFontWeight = currentFontWeight;
                demoText.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
                textArea.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
            } else {
                currentFontWeight = FontWeight.NORMAL;
                savedFontWeight = currentFontWeight;
                demoText.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
                textArea.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
            }
        });
        italicCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                currentFontPosture = FontPosture.ITALIC;
                savedFontPosture = currentFontPosture;
                demoText.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
                textArea.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
            } else {
                currentFontPosture = FontPosture.REGULAR;
                savedFontPosture = currentFontPosture;
                demoText.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
                textArea.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
            }
        });


        sizeComboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //System.out.println("输入已改变: " + newValue);
                //判断输入是否为数字,且长度不超过2位
                if (!newValue.matches("\\d*") || newValue.length() > 2|| newValue.isEmpty()) {
                    sizeComboBox.getEditor().setText(oldValue);
                }
                else {
                    currentSize = Integer.parseInt(newValue);
                    savedSize = currentSize;
                    demoText.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
                    textArea.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
                }
            }
        });

        fontComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            currentFont = newValue;
            savedFont = currentFont;
            demoText.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
            textArea.setFont(Font.font(currentFont, currentFontWeight, currentFontPosture, currentSize));
        });

        colorPicker.setOnAction(e -> changeColor());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(setBox);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("字体");
        primaryStage.setResizable(false);
        primaryStage.show();
        //将焦点移到demoText按钮上
        demoText.requestFocus();

        primaryStage.setOnCloseRequest(event ->{
        isChange=false;
        primaryStage.close();

        });

        isChange = true;
    }


    private void changeColor() {

         currentColor = colorPicker.getValue();
         savedColor = currentColor;
         demoText.setFill(currentColor);
         textArea.setStyle("-fx-text-fill: " + toHexString(currentColor) + ";");
        // 更改字体颜色
        //textArea.setStyle("-fx-text-fill: red;");
    }
    static String  toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }



}
