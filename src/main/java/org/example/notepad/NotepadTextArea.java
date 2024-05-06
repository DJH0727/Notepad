package org.example.notepad;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class NotepadTextArea {
    private TextArea textArea;

    public NotepadTextArea() {

        textArea = new TextArea();
        //自动换行
        textArea.setWrapText(true);
        //设置字体大小
        textArea.setFont(new Font(15));
        //设置字体颜色
        textArea.setStyle("-fx-text-fill: red;");
    }

    public TextArea getTextArea() {
        return textArea;
    }
}
