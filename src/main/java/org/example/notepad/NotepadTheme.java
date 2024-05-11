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
import javafx.stage.Stage;

public class NotepadTheme {

    static int savedSize=15;

    static String savedFont="System";

    static Color savedColor = Color.BLACK;

    public static void initTheme(TextArea textArea) {
        textArea.setStyle("-fx-text-fill: " + toHexString(savedColor) + ";");
        textArea.setFont(Font.font(savedFont, FontWeight.NORMAL, FontPosture.REGULAR, savedSize));
    }


    boolean isChange = false;
    TextArea textArea;


    public NotepadTheme(TextArea textArea) {

        this.textArea = textArea;
        currentSize = savedSize;
        currentColor = savedColor;
        currentFont = savedFont;

    }

    TextField sizeField = new TextField();
    Integer currentSize;
    ColorPicker colorPicker;
    ComboBox<String> fontComboBox = new ComboBox<>();
    Color currentColor;
    String currentFont;

    public void showChangeStage(Stage stage) {

        if (isChange) return;



        Stage primaryStage = new Stage();
        stage.setOnCloseRequest(event -> {
            primaryStage.close();
        });

        //sizeField = new TextField();
        sizeField.setText(currentSize.toString());
        Label fontLabel = new Label("更改字体:");
        fontComboBox.getItems().addAll(Font.getFamilies());
        fontComboBox.setValue(currentFont);
        fontComboBox.setVisibleRowCount(10);//设置可见行数

        Label sizelabel = new Label("字体大小:");
        Button sizeBiggerButton = new Button("+");
        Button sizeSmallerButton = new Button("-");
        Label colorLabel = new Label("更改颜色:");
         colorPicker = new ColorPicker();
        colorPicker.setValue(currentColor);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.add(sizelabel, 0, 0);
        gridPane.add(sizeField, 1, 0);
        gridPane.add(sizeBiggerButton, 2, 0);
        gridPane.add(sizeSmallerButton, 3, 0);
        gridPane.add(fontLabel, 0, 1);
        gridPane.add(fontComboBox, 1, 1);
        gridPane.add(colorLabel, 0, 2);
        gridPane.add(colorPicker, 1, 2);
        gridPane.setAlignment(Pos.CENTER);




        sizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                sizeField.setText(newValue.replaceAll("[\\D]", ""));
            }
            if(!newValue.isEmpty()) {
                currentSize = Integer.parseInt(newValue);
                savedSize = currentSize;
                textArea.setFont(new Font(currentSize));
            }
        });

        fontComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            //System.out.println("旧值：" + oldValue);
           // System.out.println("新值：" + newValue);
            currentFont = newValue;
            savedFont = currentFont;
            textArea.setFont(Font.font(newValue, FontWeight.BOLD, FontPosture.REGULAR, currentSize));
        });

        //fontButton.setOnAction(e -> changeFont());
        sizeBiggerButton.setOnAction(e -> changeSize(true));
        sizeSmallerButton.setOnAction(e -> changeSize(false));
        colorPicker.setOnAction(e -> changeColor());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(gridPane);

        Scene scene = new Scene(layout, 400, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("字体");
        primaryStage.setResizable(false);
        primaryStage.show();
        //将焦点移到size+按钮上
        sizeBiggerButton.requestFocus();

        primaryStage.setOnCloseRequest(event ->{

        isChange=false;
        });




        isChange = true;
    }

    private void changeSize(boolean isBigger) {

        if(currentSize<1)return;
        if(isBigger)
            currentSize+=1;
        else
            currentSize-=1;

        // 更改字体大小
        savedSize = currentSize;
        sizeField.setText(currentSize.toString());
        textArea.setFont(Font.font(currentFont, FontWeight.NORMAL, FontPosture.REGULAR, currentSize));
    }

    private void changeColor() {

         currentColor = colorPicker.getValue();
         savedColor = currentColor;
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

    //允许用户更改文本的字体样式、大小和颜色



}
