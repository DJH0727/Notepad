package org.example.notepad;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
文件  fileMenu
    新建  newMenuItem
    打开  openMenuItem
    保存  saveMenuItem
    另存为 saveAsMenuItem
    退出  exitMenuItem
编辑  editMenu
    撤销  undoMenuItem
    剪切  cutMenuItem
    复制  copyMenuItem
    粘贴  pasteMenuItem
    删除  deleteMenuItem
    查找  findMenuItem
    替换  replaceMenuItem
    转到  gotoMenuItem
    全选  selectAllMenuItem
    时间  timeDateMenuItem

帮助  helpMenu
    关于  aboutMenuItem
    字体  fontMenuItem
    缩放  zoomMenuItem
    自动换行  WrapTextMenuItem
*/
public class NotepadMenu {

    private MenuBar menuBar;//菜单栏

    TextArea textArea;//文本框
    FileMenu filemenu;//文件菜单
    EditMenu editmenu;//编辑菜单
    ViewMenu viewmenu;//视图菜单
    HelpMenu helpmenu;//帮助菜单
    public void setBorderPane(BorderPane borderPane,NotepadStatusBar bar,Stage stage) {

        this.viewmenu.initViewMenu(borderPane,bar,stage);

    }
    public void initFileMenu(Stage stage,File file)
    {
        helpmenu.setCurrentStage(stage);
        //顺便也初始化编辑菜单的stage
        editmenu.setCurrentStage(stage);
        filemenu.initFileMenu(stage,file);
    }

    public void initEditMenu(VBox vbox) {
        editmenu.initEditMenu(vbox);
    }


    public NotepadMenu() {}
    public NotepadMenu(NotepadTextArea notepadTextArea,File file) {
        menuBar = new MenuBar();
        filemenu = new FileMenu(notepadTextArea);
        Menu fileMenu = filemenu.getFileMenu();

        editmenu= new EditMenu(notepadTextArea);
        Menu editMenu = editmenu.getEditMenu();

        viewmenu = new ViewMenu(notepadTextArea,file);
        Menu viewMenu =viewmenu.getViewMenu();

        helpmenu = new HelpMenu(notepadTextArea);
        Menu helpMenu = helpmenu.getHelpMenu();

        menuBar.getMenus().addAll(fileMenu, editMenu,viewMenu, helpMenu);

        this.textArea = notepadTextArea.getTextArea();
        //if(this.textArea == null)System.out.println("TextArea is null");
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

}



class FileMenu
{
    Menu fileMenu ;

    TextArea textArea;
    File file;//打开的文件
    Stage currentStage;
    String SavedString;//文件打开时的文本，或者保存后的文本
    NotepadTextArea notepadTextArea;
    public void initFileMenu(Stage stage,File file)
    {
    this.currentStage=stage;this.file=file;

    }
    public FileMenu(NotepadTextArea notepadTextArea)
    {
        this.notepadTextArea = notepadTextArea;
        this.textArea =  notepadTextArea.getTextArea();
        SavedString=textArea.getText();
        notepadTextArea.setSavedText(SavedString);

        fileMenu = new Menu("文件");
        // 创建二级菜单
        NewFile();
        OpenFile();
        SaveFile();
        SaveAsFile();
        ExitFile();
        //CheckIfSave();
    }

    public Menu getFileMenu()
    {
        return fileMenu;
    }
    public void NewFile()
    {
        MenuItem newMenuItem = new MenuItem("新建");
        newMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));

        newMenuItem.setOnAction(event -> {
            //为了方便就直接创建一个新窗口，而不是清空当前窗口内容
            currentStage.fireEvent(
                    new WindowEvent(
                            currentStage,
                            WindowEvent.WINDOW_CLOSE_REQUEST
                    )
            );
            if(!currentStage.isShowing())
            {
                Stage stage = new Stage();
                Main main= new Main();
                main.newStage(null,stage);
            }
        });
        fileMenu.getItems().add(newMenuItem);
    }

    public void OpenFile() {
        MenuItem openMenuItem = new MenuItem("打开");
        openMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));


        openMenuItem.setOnAction(e -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
           //打开文件选择器
            File openedfile= fileChooser.showOpenDialog(stage);


            if (openedfile != null) {
                try {
                    //将文件名设置为标题
                    Stage Newstage=new Stage();
                    Main main =new Main();
                    main.newStage(openedfile,Newstage);

                } catch (Exception ex) {
                    Logger logger = Logger.getLogger(Main.class.getName());
                    logger.log(Level.SEVERE, "Error in OpenFile()", e);
                }
            }
        });

        fileMenu.getItems().add(openMenuItem);
    }



//检查关闭窗口时，是否保存，不能检查新建时当前是否保存（除非新建是新建窗口且关闭当前窗口）
    public void CheckIfSave()
    {
        currentStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notepad");
            alert.setHeaderText(null);
            if(file!=null)
                alert.setContentText("更改未保存，是否将更改保存到"+file.toPath());
            else
                alert.setContentText("是否保存文件");
            ButtonType buttonSave = new ButtonType("保存");
            ButtonType buttonDoNotSave = new ButtonType("不保存");
            ButtonType buttonCancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonSave, buttonDoNotSave, buttonCancel);

            if (!textArea.getText().equals(SavedString))
            {

                Optional<ButtonType> result=alert.showAndWait();

                if (result.get() == buttonSave){
                    //文件是打开状态，保存到文件
                    if(file!=null)
                    {
                        FileWriter writer;
                        try {
                            writer = new FileWriter(file);
                            writer.write("");//清空原文件内容
                            writer.write(textArea.getText());
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            Logger logger = Logger.getLogger(Main.class.getName());
                            logger.log(Level.SEVERE, "Error in CheckIfSave()", e);
                        }
                    }
                    else {
                        Stage stage = new Stage();
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("保存为");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
                        //打开资源管理器
                        file = fileChooser.showSaveDialog(stage);

                        if(file==null) event.consume();//取消保存，阻止窗口关闭事件;
                        else {
                            FileWriter writer;
                            try {
                                writer = new FileWriter(file);
                                writer.write("");//清空原文件内容
                                writer.write(textArea.getText());
                                writer.flush();
                                writer.close();
                            } catch (IOException e) {
                                Logger logger = Logger.getLogger(Main.class.getName());
                                logger.log(Level.SEVERE, "Error in CheckIfSave()", e);
                            }
                        }
                    }

                   // System.out.println("save");
                } else if (result.get() == buttonDoNotSave) {

                   // System.out.println("do not save");
                } else if (result.get() == buttonCancel) {
                    event.consume();//阻止窗口关闭事件
                   // System.out.println("cancel");
                }

                //System.out.println("文件未保存");
            }


        });

    }

    public void SaveFile() {
        MenuItem saveMenuItem = new MenuItem("保存");
        saveMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

        saveMenuItem.setOnAction(event->{

            if(file!=null)
            {
                FileWriter writer;
                try {
                    writer = new FileWriter(file);
                    writer.write("");//清空原文件内容
                    writer.write(textArea.getText());
                    SavedString=textArea.getText();//更新保存之前的文本
                    notepadTextArea.setSavedText(SavedString);
                    currentStage.setTitle(file.getName());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    Logger logger = Logger.getLogger(Main.class.getName());
                    logger.log(Level.SEVERE, "Error in SaveFile()", e);
                }
            }
            else {
                Stage stage = new Stage();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("保存为");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
                //打开资源管理器
                file = fileChooser.showSaveDialog(stage);

                if(file!=null)//新建了文件
                {
                    currentStage.setTitle(file.getName());
                    FileWriter writer;
                    try {
                        writer = new FileWriter(file);
                        writer.write("");//清空原文件内容
                        writer.write(textArea.getText());
                        SavedString=textArea.getText();//更新保存之前的文本
                        notepadTextArea.setSavedText(SavedString);

                        notepadTextArea.setFile(file);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        Logger logger = Logger.getLogger(Main.class.getName());
                        logger.log(Level.SEVERE, "Error in SaveFile()", e);
                    }
                }
            }

        });




        fileMenu.getItems().add(saveMenuItem);
        }

    public void SaveAsFile() {
        MenuItem saveAsMenuItem = new MenuItem("另存为");
        saveAsMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));

        saveAsMenuItem.setOnAction(event->{

            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存为");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
            //打开资源管理器
            file = fileChooser.showSaveDialog(stage);

            if(file!=null)//新建了文件
            {
                currentStage.setTitle(file.getName());
                FileWriter writer;
                try {
                    writer = new FileWriter(file);
                    writer.write("");//清空原文件内容
                    writer.write(textArea.getText());
                    SavedString=textArea.getText();//更新保存之前的文本

                    notepadTextArea.setFile(file);

                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    Logger logger = Logger.getLogger(Main.class.getName());
                    logger.log(Level.SEVERE, "Error in SaveAsFile()", e);
                }
            }



        });

        fileMenu.getItems().add(saveAsMenuItem);
    }


    public void ExitFile() {
        MenuItem exitMenuItem = new MenuItem("退出");
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitMenuItem.setOnAction(event -> {
            //模拟窗口关闭事件，方便CheckIfSave监听并判断是否保存
            currentStage.fireEvent(
                    new WindowEvent(
                            currentStage,
                            WindowEvent.WINDOW_CLOSE_REQUEST
                    )
            );

        });
        fileMenu.getItems().add(exitMenuItem);
    }

}

class EditMenu
{
    Menu editMenu ;

    TextArea textArea;
     Stage currentStage;


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public EditMenu(NotepadTextArea notepadTextArea )
    {

        this.textArea =  notepadTextArea.getTextArea();
        editMenu = new Menu("编辑");
        // 创建二级菜单
        Undo();//撤销
        Redo();//重做
        Cut();//剪切
        Copy();//复制
        Paste();//粘贴
        Delete();//删除
        Find();//查找
        Replace();//替换
        GoTo();//转到
        SelectAll();//全选
        TimeDate();//时间日期

    }


    public void Undo()
    {
        MenuItem undoMenuItem = new MenuItem("撤销");
        undoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        undoMenuItem.setOnAction(event -> {
            textArea.undo();
        });

        editMenu.getItems().add(undoMenuItem);

    }
    public void Redo()
    {
        MenuItem redoMenuItem = new MenuItem("重做");
        redoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
        redoMenuItem.setOnAction(event -> {
            textArea.redo();
        });

        editMenu.getItems().add(redoMenuItem);
    }

    public void Cut()
    {
        MenuItem CutMenuItem = new MenuItem("剪切");
        CutMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        // textArea.cut(); 就能完成
        try {
            CutMenuItem.setOnAction(event -> { // 添加了一个点击事件监听器
                    // 获取系统剪贴板
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(textArea.getSelectedText());
                    clipboard.setContent(content);
                    textArea.replaceSelection("");
            });
        }
        catch (Exception e) {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, "Error in Cut()", e);
        }
        editMenu.getItems().add(CutMenuItem);
    }



    public void Copy() {
        MenuItem copyMenuItem = new MenuItem("复制");
        copyMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        // textArea.copy(); 就能完成
       try {
           copyMenuItem.setOnAction(event -> { // 添加了一个点击事件监听器
                   // 获取系统剪贴板
                   final Clipboard clipboard = Clipboard.getSystemClipboard();
                   //ClipboardContent 是 JavaFX 中的一个类，它是剪贴板数据的容器。
                   // 它可以保存多种数据格式的多个数据。你可以使用 ClipboardContent 的 putString，putImage，putFiles 等方法将数据放入剪贴板。
                   final ClipboardContent content = new ClipboardContent();
                   // 将选定的文本添加到 ClipboardContent
                   content.putString(textArea.getSelectedText());
                   // 设置剪贴板内容
                   clipboard.setContent(content);
                   //System.out.println("复制成功!");
               });

       }
       catch (Exception e) {
           Logger logger = Logger.getLogger(Main.class.getName());
           logger.log(Level.SEVERE, "Error in Copy()", e);
       }
        editMenu.getItems().add(copyMenuItem);
    }


    public void Paste()
    {
        MenuItem pasteMenuItem = new MenuItem("粘贴");
        pasteMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        // textArea.paste(); 就能完成
        //在 JavaFX 中，所有的 UI 操作（例如更新 UI 组件的状态或获取剪贴板内容）都必须在 JavaFX 的事件调度线程上执行。
        // 因此，需要在事件处理方法中使用 Platform.runLater() 方法来将这些操作提交到 JavaFX 线程。
        //pasteMenuItem.setOnAction(event -> {...})；添加了一个点击事件监听器
        //先判断有无选中文本，有则替换，无则插入
        pasteMenuItem.setOnAction(event -> {
            Platform.runLater(() -> {
                try {
                    // 获取系统剪贴板
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    // 获取剪贴板中的内容
                    String text = clipboard.getString();
                    // 检查是否有选中的文本,好想默认都有选中？，检查文本长度为0才行
                    if (textArea.getSelectedText().isEmpty()) {
                        // 在光标处插入文本
                        textArea.insertText(textArea.getCaretPosition(), text);
                    } else {
                        // 替换选中的文本
                        textArea.replaceSelection(text);

                    }
                    //System.out.println("粘贴板中的文本: " + text);
                } catch (Exception e) {
                    //System.out.println("只能粘贴文本!");
                    Logger logger = Logger.getLogger(Main.class.getName());
                    logger.log(Level.SEVERE, "Error in Paste()", e);
                }
            });
        });

        editMenu.getItems().add(pasteMenuItem);

    }

    public void Delete()
    {
        MenuItem deleteMenuItem = new MenuItem("删除");
        deleteMenuItem.setAccelerator(KeyCombination.keyCombination("Del"));

        deleteMenuItem.setOnAction(event -> {
            Platform.runLater(() -> {
                // 检查是否有选中的文本
                if (textArea.getSelectedText().isEmpty()) {
                    // 删除光标后的字符,如果光标后有字符
                    if(textArea.getCaretPosition()!=textArea.getLength())
                        textArea.deleteText(textArea.getCaretPosition(), textArea.getCaretPosition() + 1);

                } else {

                    // 删除选中的文本
                    textArea.deleteText(textArea.getSelection().getStart(), textArea.getSelection().getEnd());
                }

            });
        });

        editMenu.getItems().add(deleteMenuItem);



    }


    boolean isFind = false;
    TextField currentSearchField;
    VBox currentTopBox;
    public void initEditMenu(VBox vbox) {
        this.currentTopBox=vbox;
    }
    long currentIndex=0;
    long totalCount=0;

    TextField searchField = new TextField();
    Button prevButton = new Button("查找上一个");
    Button nextButton = new Button("查找下一个");
    Button cancelButton = new Button("取消");
    CheckBox caseSensitive =  new CheckBox("区分大小写");
    CheckBox Word =  new CheckBox("全词匹配");
    GridPane gridPane = new GridPane();


    //查找功能就地取材，按照IDEA查找功能写的
    public  void Find()
    {
        MenuItem findMenuItem = new MenuItem("查找");
        findMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        //首先，创建一个 TextInputDialog 对话框，让用户输入搜索字符串。
        //然后，使用正则表达式在 TextArea 中搜索匹配的文本。


        findMenuItem.setOnAction(event -> {
                ReplaceOrFind(false);
        });


        editMenu.getItems().add(findMenuItem);

    }



    int lastMatchStart = -1;
    int lastMatchEnd = -1;
    int currentStart=-1;
    public boolean FindBefore() {


        if(currentStart==-1)
        {
            currentStart = textArea.getCaretPosition();
        }
        if(!textArea.getSelectedText().isEmpty())
        {
            currentStart = textArea.getSelection().getStart();
        }
        else {
            currentStart = textArea.getCaretPosition();
        }

        String searchText = searchField.getText();
        searchText=Pattern.quote(searchText);
        // 如果选择了全词匹配
        if (Word.isSelected())
            searchText = "\\b" + searchText + "\\b";
        // 如果选择了区分大小写
        Pattern pattern;
        if(caseSensitive.isSelected())
            pattern= Pattern.compile(searchText);
        else
            pattern= Pattern.compile(searchText,Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(textArea.getText());
        totalCount=matcher.results().count();
        if(totalCount==0)
        {
            currentStart=-1;
            textArea.selectRange(0, 0);
            return false;
        }

        matcher.reset();

        // 保存上一个匹配的位置


        // 查找所有匹配的文本
        while(matcher.find()) {
            // 如果当前匹配的位置超过了光标的位置，那么就选中上一个匹配的文本
           // System.out.println("matcher.start: "+matcher.start()+" currentStart"+currentStart);
            if(matcher.start() >= currentStart) {
                if(lastMatchStart != -1 && lastMatchEnd != -1) {
                    textArea.selectRange(lastMatchStart, lastMatchEnd);
                    currentStart=lastMatchStart;
                   // System.out.println("lastMatchStart: "+lastMatchStart);
                }
                return true;
            }

            // 更新上一个匹配的位置
            lastMatchStart = matcher.start();
            lastMatchEnd = matcher.end();
        }

        // 如果没有找到更多的匹配，那么就选中最后一个匹配的文本
        if(lastMatchStart != -1 && lastMatchEnd != -1) {
            textArea.selectRange(lastMatchStart, lastMatchEnd);
            currentStart=lastMatchStart;

           // System.out.println("lastMatch: "+lastMatchStart+" "+lastMatchEnd);
        }

        return false;

    }


    public boolean FindNext()
    {


        if(currentStart==-1)
        {
            currentStart = textArea.getCaretPosition();
        }
        if(!textArea.getSelectedText().isEmpty())
        {
            currentStart = textArea.getSelection().getEnd();
        }
        else {
            currentStart = textArea.getCaretPosition();
        }
        String searchText = searchField.getText();
        searchText=Pattern.quote(searchText);
        // 如果选择了全词匹配
        if (Word.isSelected())
            searchText = "\\b" + searchText + "\\b";
        // 如果选择了区分大小写
        Pattern pattern;
        if(caseSensitive.isSelected())
            pattern= Pattern.compile(searchText);
        else
            pattern= Pattern.compile(searchText,Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(textArea.getText());
        totalCount=matcher.results().count();
        if(totalCount==0)
        {
            currentStart=-1;
            textArea.selectRange(0, 0);
            return false;
        }

        matcher.reset();

        while(matcher.find()) {
            // 如果当前匹配的位置超过了光标的位置，那么就选中上一个匹配的文本
            //System.out.println("matcher.start: "+matcher.start()+" currentStart"+currentStart);
            if(matcher.start() >= currentStart) {
                if(lastMatchStart != -1 && lastMatchEnd != -1) {
                    lastMatchStart = matcher.start();
                    lastMatchEnd = matcher.end();
                    textArea.selectRange(lastMatchStart, lastMatchEnd);
                    currentStart=lastMatchEnd;
                    //System.out.println("lastMatchStart: "+lastMatchStart);
                }
                return true;
            }

            // 更新上一个匹配的位置

        }

        // 如果没有找到更多的匹配，那么就选中最后一个匹配的文本
        if(lastMatchStart != -1 && lastMatchEnd != -1) {
            textArea.selectRange(lastMatchStart, lastMatchEnd);
            currentStart=lastMatchStart;

           // System.out.println("lastMatch: "+lastMatchStart+" "+lastMatchEnd);
        }

        return false;
    }

    Button replaceButton = new Button("替换");
    Button replaceAllButton = new Button("全部替换");
    ToggleButton boldButton = new ToggleButton("▲");
    TextField replaceField = new TextField();

    public  void Replace() {
        MenuItem replaceMenuItem = new MenuItem("替换");
        replaceMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));

        replaceMenuItem.setOnAction(event -> {
                    ReplaceOrFind(true);
        });

        editMenu.getItems().add(replaceMenuItem);
    }

    public void ReplaceOrFind(boolean isReplace) {
        //已经打开了查找框，则不再打开新的
        if(!isFind) {
            gridPane = new GridPane();
            gridPane.setPadding(new Insets(5, 5, 5, 5));
            gridPane.setVgap(5);
            gridPane.setHgap(10);
            gridPane.add(boldButton, 0, 0);
            gridPane.add(searchField, 6, 0);
            gridPane.add(prevButton, 7, 0);
            gridPane.add(nextButton, 8, 0);
            gridPane.add(caseSensitive, 9, 0);
            gridPane.add(Word, 10, 0);
            gridPane.add(cancelButton, 15, 0);


            //gridPane.setAlignment(Pos.CENTER);
            currentTopBox.getChildren().add(gridPane);

            isFind = true;
            currentSearchField = searchField;
        }

        if(isReplace)
        {
            gridPane.add(replaceField, 6, 1);
            gridPane.add(replaceButton, 7, 1);
            gridPane.add(replaceAllButton, 8, 1);
            boldButton.setSelected(true);
        }
        boldButton.setOnAction(OnEvent-> {
            if(boldButton.isSelected()) {
                gridPane.add(replaceField, 6, 1);
                gridPane.add(replaceButton, 7, 1);
                gridPane.add(replaceAllButton, 8, 1);
                boldButton.setText("▼");
            }
            else
            {
                gridPane.getChildren().remove(replaceField);
                gridPane.getChildren().remove(replaceButton);
                gridPane.getChildren().remove(replaceAllButton);
                boldButton.setText("▲");
            }
        });



        //更新查找框的文本为选中的文本
        if(currentSearchField!=null)
        {
            currentSearchField.setText(textArea.getSelectedText());
        }


        //如果选中文本开启查找，则设置初值
        if (!textArea.getSelectedText().isEmpty()) {
            searchField.setText(textArea.getSelectedText());
            lastMatchStart = textArea.getSelection().getStart();
            lastMatchEnd = textArea.getSelection().getEnd();
        }

        prevButton.setOnAction(OnEvent->{//查找上一个

            FindBefore();


        });
        nextButton.setOnAction(OnEvent->{//查找下一个

            FindNext();

        });
        cancelButton.setOnAction(OnEvent->{
            currentTopBox.getChildren().remove(gridPane);
            currentStart=-1;
            isFind=false;
        });
        replaceButton.setOnAction(OnEvent-> {//替换

            String replaceText = replaceField.getText();
            if(lastMatchStart!=lastMatchEnd&&searchField.getText().equals(textArea.getSelectedText())) textArea.replaceSelection(replaceText);
            FindNext();



        });
        replaceAllButton.setOnAction(OnEvent-> {//替换全部

            String searchText = searchField.getText();
            searchText=Pattern.quote(searchText);
            // 如果选择了全词匹配
            if (Word.isSelected())
                searchText = "\\b" + searchText + "\\b";
            // 如果选择了区分大小写
            Pattern pattern;
            if(caseSensitive.isSelected())
                pattern= Pattern.compile(searchText);
            else
                pattern= Pattern.compile(searchText,Pattern.CASE_INSENSITIVE);

            Matcher matcher = pattern.matcher(textArea.getText());
            String replacedText = matcher.replaceAll(replaceField.getText());
            textArea.selectAll();
            textArea.replaceSelection(replacedText);
        });



    }




    public void GoTo()
    {
        MenuItem gotoMenuItem = new MenuItem("转到");
        gotoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));

        gotoMenuItem.setOnAction(event -> {

            while (true) {
                try {
                    //获取当前光标位置行号
                    int line = textArea.getText(0, textArea.getCaretPosition()).split("\n",-1).length;
                    //System.out.println("当前行号：" + currentLine);
                    TextInputDialog dialog = new TextInputDialog(Integer.toString(line));
                    dialog.setTitle("跳转到指定行号");
                    dialog.setHeaderText(null);
                    dialog.setContentText("请输入行号:");

                    // 限制只能输入数字
                    dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d*")) {
                            dialog.getEditor().setText(newValue.replaceAll("[\\D]", ""));
                        }
                    });
                    // 显示对话框
                    Optional<String> result = dialog.showAndWait();

                    if (result .isPresent()) {
                        line = Integer.parseInt(result.get());
                    }
                    else {
                        break;
                    }
                   // System.out.println("line" + line);
                    // 跳转到指定行号

                    //int caretPosition = textArea.getCaretPosition();
                    String[] str = textArea.getText().split("\n",-1);
                    //有 count 个换行符，说明有count+1行
                    Pattern pattern = Pattern.compile("\n");
                    Matcher matcher = pattern.matcher(textArea.getText());
                    int count = 1;
                    while (matcher.find()) {
                        count++;
                    }
                    //如果输入的行号不合法，则提示错误
                    if (line > count || line < 1) {
                        throw new Exception();
                    }

                    //System.out.println(count);
                    //计算需要跳转行数前有多少个字符，将光标移动到该位置
                    int cnt = 0;
                    for (int i = 0; i < line - 1; i++) {
                        cnt += str[i].length() + 1;
                    }
                    //System.out.println(cnt);
                    textArea.positionCaret(cnt);

                    break;
                } catch (Exception e1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("错误");
                    alert.setHeaderText(null);
                    alert.setContentText("请输入正确的行号!");
                    alert.showAndWait();
                   // System.out.println("请输入正确的行号!!!");

                }

            }
        });




        editMenu.getItems().add(gotoMenuItem);
    }

    public void SelectAll() {
        MenuItem selectAllMenuItem = new MenuItem("全选");
        selectAllMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));

        try {
        selectAllMenuItem.setOnAction(event -> {
            // 选择 TextArea 中的所有文本
            textArea.selectAll();
        });
        }
        catch (Exception e) {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, "Error in SelectAll()", e);
        }

        editMenu.getItems().add(selectAllMenuItem);
    }

    public void TimeDate()
    {
        MenuItem timeDateMenuItem = new MenuItem("时间/日期");
        timeDateMenuItem.setAccelerator(KeyCombination.keyCombination("F5"));

        timeDateMenuItem.setOnAction(event -> {
                    try {
                        //获取系统时间
                        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.getDefault());

                        String text =  dateFormat.format(new Date());
                        // 检查是否有选中的文本
                        if (textArea.getSelectedText() != null) {
                            // 替换选中的文本
                            textArea.replaceSelection(text);
                        } else {
                            // 在光标处插入文本
                            textArea.insertText(textArea.getCaretPosition(), text);
                        }
                    } catch (Exception e) {
                        Logger logger = Logger.getLogger(Main.class.getName());
                        logger.log(Level.SEVERE, "Error in TimeDate()", e);
                    }
        });
        editMenu.getItems().add(timeDateMenuItem);
    }
    public Menu getEditMenu()
    {
        return editMenu;
    }



}
//查看
class ViewMenu
{
    Menu viewMenu ;
    TextArea textArea;

    BorderPane borderPane;

    NotepadStatusBar statusBarHBox;
    //编码方式
    String encoding;
    File file;
    Stage primaryStage;
    public void initViewMenu(BorderPane borderPane,NotepadStatusBar statusBarHBox,Stage primaryStage) {
        this.statusBarHBox = statusBarHBox;
        this.borderPane = borderPane;
        this.primaryStage = primaryStage;
    }
    public ViewMenu(NotepadTextArea notepadTextArea ,File file) {

        this.textArea = notepadTextArea.getTextArea();
        this.file=file;
        viewMenu = new Menu("查看");


        // 创建二级菜单
        ChangeFont();
        //对齐方式
        AlignText();
        Encoding();
        ShowStatusBar();
        WrapText();
    }

    //字体样式
    NotepadTheme theme;
    public void ChangeFont()
    {
        MenuItem fontMenuItem = new MenuItem("字体");
        //fontMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
        if(theme==null)
            theme = new NotepadTheme(textArea);

        fontMenuItem.setOnAction(event -> {

            theme.showChangeStage(primaryStage);

        });
        viewMenu.getItems().add(fontMenuItem);

    }

    public void AlignText()
    {
        Menu alignMenu = new Menu("对齐方式");
        viewMenu.getItems().add(alignMenu);
        ToggleGroup group = new ToggleGroup();

        RadioMenuItem leftAlignMenuItem = new RadioMenuItem("左对齐");
        group.getToggles().add(leftAlignMenuItem);
        alignMenu.getItems().add(leftAlignMenuItem);
        leftAlignMenuItem.setUserData("left");
        leftAlignMenuItem.setSelected(true);
        RadioMenuItem centerAlignMenuItem = new RadioMenuItem("居中");
        group.getToggles().add(centerAlignMenuItem);
        alignMenu.getItems().add(centerAlignMenuItem);
        centerAlignMenuItem.setUserData("center");
        RadioMenuItem rightAlignMenuItem = new RadioMenuItem("右对齐");
        group.getToggles().add(rightAlignMenuItem);
        alignMenu.getItems().add(rightAlignMenuItem);
        rightAlignMenuItem.setUserData("right");

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                PseudoClass left = PseudoClass.getPseudoClass("left");
                PseudoClass right = PseudoClass.getPseudoClass("right");
                PseudoClass center = PseudoClass.getPseudoClass("center");
                if(newValue.getUserData().equals("left"))
                {
                   // System.out.println("left");
                    textArea.pseudoClassStateChanged(left,true);
                    textArea.pseudoClassStateChanged(right,false);
                    textArea.pseudoClassStateChanged(center,false);

                }
                else if(newValue.getUserData().equals("center"))
                {
                    //System.out.println("center");
                        textArea.pseudoClassStateChanged(center,true);
                        textArea.pseudoClassStateChanged(left,false);
                        textArea.pseudoClassStateChanged(right,false);
                }
                else if(newValue.getUserData().equals("right"))
                {
                    //System.out.println("right");
                    textArea.pseudoClassStateChanged(right,true);
                    textArea.pseudoClassStateChanged(left,false);
                    textArea.pseudoClassStateChanged(center,false);
                }

            }

        });
    }

    public void ShowStatusBar()
    {
        CheckMenuItem statusBarMenuItem = new CheckMenuItem("状态栏");

        statusBarMenuItem.setSelected(true);
        statusBarMenuItem.setOnAction(event -> {
                    if (statusBarMenuItem.isSelected()) {
                       borderPane.setBottom(statusBarHBox.getStatusBar());
                    } else {
                       borderPane.setBottom(null);
                    }

                });
        viewMenu.getItems().add(statusBarMenuItem);


    }

    public void Encoding() {

        Menu encodingMenu = new Menu("编码方式");
        viewMenu.getItems().add(encodingMenu);
        //System.out.println(file+"Encoding");
        if(file==null) encodingMenu.setDisable(true);//新建文件时禁用编码菜单，能力有限（

            //
        ToggleGroup group = new ToggleGroup();

        RadioMenuItem UTF8MenuItem = new RadioMenuItem("UTF-8");
        group.getToggles().add(UTF8MenuItem);
        encodingMenu.getItems().add(UTF8MenuItem);
        UTF8MenuItem.setUserData("UTF-8");
        UTF8MenuItem.setSelected(true);
        RadioMenuItem UTF16MenuItem = new RadioMenuItem("UTF-16");
        group.getToggles().add(UTF16MenuItem);
        encodingMenu.getItems().add(UTF16MenuItem);
        UTF16MenuItem.setUserData("UTF-16");
        RadioMenuItem GB2312MenuItem = new RadioMenuItem("GB2312");
        group.getToggles().add(GB2312MenuItem);
        encodingMenu.getItems().add(GB2312MenuItem);
        GB2312MenuItem.setUserData("GB2312");

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                encoding = (String) newValue.getUserData();
                statusBarHBox.setEncoding(encoding);

                if(file!=null) {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file), encoding))) {
                        textArea.clear();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            textArea.appendText(line);
                            textArea.appendText("\n");
                        }
                    } catch (Exception e) {
                        Logger logger = Logger.getLogger(Main.class.getName());
                        logger.log(Level.SEVERE, "Error in Encoding()", e);
                    }
                }

                //System.out.println("编码方式：" + encoding);
            }

        });
    }



    public void WrapText() {
        CheckMenuItem wrapTextMenuItem = new CheckMenuItem("自动换行");
        wrapTextMenuItem.setSelected(true);
        wrapTextMenuItem.setOnAction(event -> {
            textArea.setWrapText(wrapTextMenuItem.isSelected());
        });
        viewMenu.getItems().add(wrapTextMenuItem);
    }
    public Menu getViewMenu()
    {
        return viewMenu;
    }

}

class HelpMenu
{
    Menu helpMenu ;
    TextArea textArea;

    Stage primaryStage;


    public HelpMenu(NotepadTextArea notepadTextArea )
    {
        this.textArea = notepadTextArea.getTextArea();
        helpMenu = new Menu("帮助");
        // 创建二级菜单
        aboutInfo();


    }


    public Menu getHelpMenu()
    {
        return helpMenu;
    }


    public void aboutInfo()
    {
        MenuItem aboutMenuItem = new MenuItem("关于");


        aboutMenuItem.setOnAction(e -> {
            Stage stage = new Stage();
            primaryStage.setOnCloseRequest(event -> {
                stage.close();
            });
            stage.setTitle("关于");
            Image icon = new Image("/headshot.jpg");
            if (icon.isError()) {
                Logger logger = Logger.getLogger(Main.class.getName());
                logger.log(Level.SEVERE, "Error loading icon");
            } else {
                stage.getIcons().add(icon);
            }
            VBox vbox = new VBox(new Label("程序名称：记事本\n" +
                    "版本号：1.0\n" +
                    "完成日期：2024-05-11\n" +
                    "开发者信息：DJH0727\n" +
                    "联系方式：2724948893@qq.com\n" +
                    "仓库地址：https://github.com/DJH0727/Notepad\n" ));
            vbox.setAlignment(Pos.CENTER);  // 设置文本居中
            Scene scene = new Scene(vbox);

            stage.setScene(scene);
            stage.setMinWidth(300);  // 设置最小宽度
            stage.setMinHeight(200);  // 设置最小高度
            stage.show();
        });

        helpMenu.getItems().add(aboutMenuItem);
    }


    public void setCurrentStage(Stage stage) {
        this.primaryStage = stage;
    }
}