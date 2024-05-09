package org.example.notepad;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

public class NotepadTextArea {
    private TextArea textArea;

    public String getSavedText() {
        return SavedText;
    }

    public void setSavedText(String savedText) {
        SavedText = savedText;
    }

    String SavedText;

    File file;
    public void setFile(File file) {
        this.file = file;
    }
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
//检测是否有修改,如果有修改而且没有保存,则显示星号
    public void CheckSave(Stage stage) {

        textArea.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               if(file!=null)
                    if(textArea.getText().equals(SavedText))
                    {
                        stage.setTitle(file.getName());
                    }
                    else
                    {
                        stage.setTitle("*"+file.getName());

                    }
                //System.out.println("changed");
            }
        });


    }


}
