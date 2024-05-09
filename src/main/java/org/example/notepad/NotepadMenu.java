package org.example.notepad;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.util.Optional;
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
    public void setBorderPane(BorderPane borderPane,HBox hBox) {
        this.viewmenu.setBorderPane(borderPane,hBox);
    }
    public void initFileMenu(Stage stage,File file)
    {
        filemenu.initFileMenu(stage,file);
    }


    public NotepadMenu() {}
    public NotepadMenu(TextArea textArea) {
        menuBar = new MenuBar();
        filemenu = new FileMenu(textArea);
        Menu fileMenu = filemenu.getFileMenu();

        editmenu= new EditMenu(textArea);
        Menu editMenu = editmenu.getEditMenu();

        viewmenu = new ViewMenu(textArea);
        Menu viewMenu =viewmenu.getViewMenu();

        helpmenu = new HelpMenu(textArea);
        Menu helpMenu = helpmenu.getHelpMenu();

        menuBar.getMenus().addAll(fileMenu, editMenu,viewMenu, helpMenu);

        this.textArea = textArea;
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
    public void initFileMenu(Stage stage,File file)
    {
    this.currentStage=stage;this.file=file;

    }
    public FileMenu(TextArea textArea)
    {
        this.textArea = textArea;
        SavedString=textArea.getText();


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
                    ex.printStackTrace();
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
                            e.printStackTrace();
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
                                e.printStackTrace();
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
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
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
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

    public EditMenu(TextArea textArea)
    {
        this.textArea = textArea;
        editMenu = new Menu("编辑");
        // 创建二级菜单
        MenuItem undoMenuItem = new MenuItem("撤销");
        editMenu.getItems().add(undoMenuItem);
        Cut();
        Copy();
        Paste();
        Delete();
        MenuItem findMenuItem = new MenuItem("查找");
        editMenu.getItems().add(findMenuItem);
        MenuItem replaceMenuItem = new MenuItem("替换");
        editMenu.getItems().add(replaceMenuItem);
        GoTo();
        SelectAll();
        TimeDate();

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
            e.printStackTrace();
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
           e.printStackTrace();
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
                    // 检查是否有选中的文本,好想默认都有选中？，检查lenth为0才行
                    if (textArea.getSelectedText().isEmpty()) {
                        // 在光标处插入文本
                        textArea.insertText(textArea.getCaretPosition(), text);
                    } else {
                        // 替换选中的文本
                        textArea.replaceSelection(text);

                    }
                    //System.out.println("粘贴板中的文本: " + text);
                } catch (Exception e) {
                    System.out.println("只能粘贴文本!");
                    e.printStackTrace();
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
    public void GoTo()
    {
        MenuItem gotoMenuItem = new MenuItem("转到");
        gotoMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));

        gotoMenuItem.setOnAction(event -> {

            while (true) {
                try {
                    //获取当前光标位置行号
                    Integer currentLine = textArea.getText(0, textArea.getCaretPosition()).split("\n",-1).length;
                    int line = currentLine;
                    //System.out.println("当前行号：" + currentLine);
                    TextInputDialog dialog = new TextInputDialog(currentLine.toString());
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
                    //conut个换行符，说明有count+1行
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
                    //e1.printStackTrace();
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
            e.printStackTrace();
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
                        java.util.Date date = new java.util.Date();
                        String text = date.toString();
                        // 检查是否有选中的文本
                        if (textArea.getSelectedText() != null) {
                            // 替换选中的文本
                            textArea.replaceSelection(text);
                        } else {
                            // 在光标处插入文本
                            textArea.insertText(textArea.getCaretPosition(), text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    HBox statusBarHBox;
    public void setBorderPane(BorderPane borderPane,HBox statusBarHBox) {
        this.statusBarHBox = statusBarHBox;
        this.borderPane = borderPane;
    }
    public ViewMenu(TextArea textArea) {

        this.textArea = textArea;
        viewMenu = new Menu("查看");
        MenuItem fontMenuItem = new MenuItem("字体");
        viewMenu.getItems().add(fontMenuItem);
        MenuItem zoomMenuItem = new MenuItem("缩放");
        viewMenu.getItems().add(zoomMenuItem);
        ShowStatusBar();
        WrapText();
    }

    public void ShowStatusBar()
    {
        CheckMenuItem statusBarMenuItem = new CheckMenuItem("状态栏");

        statusBarMenuItem.setSelected(true);
        statusBarMenuItem.setOnAction(event -> {
                    if (statusBarMenuItem.isSelected()) {
                       borderPane.setBottom(statusBarHBox);
                    } else {
                       borderPane.setBottom(null);
                    }

                });
        viewMenu.getItems().add(statusBarMenuItem);


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
    public HelpMenu(TextArea textArea)
    {
        this.textArea = textArea;
        helpMenu = new Menu("帮助");
        // 创建二级菜单
        MenuItem aboutMenuItem = new MenuItem("关于");
        helpMenu.getItems().add(aboutMenuItem);

    }


    public Menu getHelpMenu()
    {
        return helpMenu;
    }



}