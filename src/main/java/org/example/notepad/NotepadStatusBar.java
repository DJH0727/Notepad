package org.example.notepad;

import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class NotepadStatusBar
{
     HBox statusBar;//状态栏
     Label statusLabel;
    Label charnumLabel;
     Label encodingLabel;
     String encoding;
     public void setEncoding(String encoding) {
        this.encoding = encoding;
        encodingLabel.setText("编码: "+encoding+"  ");
    }
    public String getEncoding() {
        return encoding;
    }
    public NotepadStatusBar(NotepadTextArea notepadTextArea  ) {
         TextArea textArea = notepadTextArea.getTextArea();
        encoding="UTF-8";
        //初始化状态标签
        int initCharNum=textArea.getText().length();
        statusLabel = new Label("  行: 1 列: 1");
        charnumLabel = new Label("  字符数: "+initCharNum);
        encodingLabel = new Label("编码: "+encoding+"  ");

        //监听器,更新光标位置
        textArea.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            int caretPosition = newVal.intValue();
            int line = textArea.getText(0, caretPosition).split("\n",-1).length;
            int column = caretPosition - textArea.getText().lastIndexOf('\n', caretPosition - 1);

            statusLabel.setText(String.format("  行: %d 列: %d", line, column));
        });
        //监听器,更新字符数
        textArea.textProperty().addListener((obs, oldVal, newVal) -> {
            int charCnt = textArea.getText().length();
            charnumLabel.setText(String.format("  字符数: %d", charCnt));
        });


       //spacer可以使状态栏宽度自适应
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        //设置状态栏
        statusBar = new HBox(statusLabel, charnumLabel,spacer, encodingLabel);
        statusBar.setPrefHeight(16);//状态栏高度15像素
        statusLabel.setFont(new Font(12)); // 字体大小
        encodingLabel.setFont(new Font(12));
        statusBar.setStyle("-fx-background-color: #F5F5F5;");
    }

    public HBox getStatusBar() {
        return statusBar;
    }

}
