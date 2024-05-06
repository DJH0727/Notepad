package org.example.notepad;

import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class NotepadStatusBar
{
    private HBox statusBar;//状态栏
    private Label statusLabel;
    private Label encodingLabel;
    public NotepadStatusBar(TextArea textArea ) {
        //初始化状态标签
        statusLabel = new Label("  行: 0 列: 0 | 字符数: 0");
        encodingLabel = new Label("编码: UTF-8  ");

        //监听器
        textArea.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            int caretPosition = newVal.intValue();
            int line = textArea.getText(0, caretPosition).split("\n").length;
            int column = caretPosition - textArea.getText().lastIndexOf('\n', caretPosition - 1);
            int charnum = textArea.getLength();
            statusLabel.setText(String.format("  行: %d 列: %d | 字符数: %d", line, column, charnum));
        });

       //spacer可以使状态栏宽度自适应
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        //设置状态栏
        statusBar = new HBox(statusLabel, spacer, encodingLabel);
        statusBar.setPrefHeight(15);//状态栏高度15像素
        statusLabel.setFont(new Font(12)); // 字体大小
        encodingLabel.setFont(new Font(12));
        statusBar.setStyle("-fx-background-color: #F5F5F5;");
    }

    public HBox getStatusBar() {
        return statusBar;
    }

}
