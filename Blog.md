# java记事本开发日志

## 目录

- [要求](#要求)
- [开发环境](#开发环境)
- [2024-05-05](#2024-05-05)
- [2024-05-06](#2024-05-06)
- [2024-05-07](#2024-05-07)
- [2024-05-08](#2024-05-08)
- [2024-05-09](#2024-05-09)
- [2024-05-10](#2024-05-10)
- [2024-05-11](#2024-05-11)


## 要求
### 1.  图形用户界面（GUI）设计

- (1)   使用JavaFX创建一个窗口，包含菜单栏、文本编辑区域和状态栏。
- (2)   菜单栏包含“文件”、“编辑”、“帮助”等选项，并实现相应的功能。
- (3)   状态栏应包括当前光标所在行列号信息。


### 2.  文本操作功能

- (1)   实现“新建”功能，能够清空当前文本并准备编辑新文件。若当前文本未保存，提示用户是否保存，若用户选择保存则执行保存后再新建。
- (2)   实现“打开”功能，，能够浏览文件系统并打开.txt文件进行编辑。
- (3)   实现“保存”和“另存为”功能，能够将当前文本保存到指定路径的.txt 文件中。


### 3.  文本编辑功能

- （1） 允许用户在文本编辑区域内输入和编辑文本。
- （2） 实现文本的撤销与重做功能。
- （3） 实现文本的查找和替换功能。其中查找应包括查找下一个、查找上一个；替换应包括替换单个和替换全部。

### 4.  格式设置功能

- （1） 允许用户更改文本的字体样式、大小和颜色。
- （2） 允许用户切换是否自动换行。


### 5.  状态显示

- （1） 在状态栏中显示当前光标所在位置的行号和列号。
- （2） 显示当前文件的编码格式（如UTF-8）。

## 开发环境
 -  IntelliJ IDEA 2021.1.2IntelliJ IDEA 2023.3.6 (Community Edition)
 -  Runtime version: 17.0.10+1-b1087.23 amd64
 -  VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
 -  Windows 11.0

## 2024-05-05

~~新建文件夹(bushi)~~

**准备基本照着Windows记事本的功能设计**


---

## 2024-05-06

- 完成菜单栏、文本编辑区域、状态栏的UI，但没有实现具体功能。

~~没有使用Scene builder，直接在javafx上实的UI。~~

### 菜单栏
`NotepadMenu.java`

设置了一个存储菜单栏对象并在构造函数中初始化。
```
private MenuBar menuBar
```
使用get方法获取菜单栏对象。

### 文本编辑区域
`NotepadTextArea.java`

### 状态栏
`NotepadStatusBar.java`

两个Label控件，分别显示当前光标所在行列号和当前文件的编码格式。

```
    private HBox statusBar;//状态栏
    private Label statusLabel;
    private Label encodingLabel;
```
构造函数
```
        //监听器
        textArea.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            int caretPosition = newVal.intValue();
            int line = textArea.getText(0, caretPosition).split("\n").length;
            int column = caretPosition - textArea.getText().lastIndexOf('\n', caretPosition - 1);
            int charnum = textArea.getLength();
            statusLabel.setText(String.format("  行: %d 列: %d | 字符数: %d", line, column, charnum));
        });
     
```
    textArea.caretPositionProperty().addListener((obs, oldVal, newVal) -> {...});
为文本编辑区域（TextArea）的光标位置属性添加了一个监听器。每当光标位置改变时，这个监听器就会被触发。

在监听器的函数体中，首先通过`newVal.intValue()`获取了新的光标位置（一个整数值）。

    int line = textArea.getText(0, caretPosition).split("\n").length;
计算光标当前所在的行号。首先获取了从文本开始到当前光标位置的所有文本，
然后以换行符（“\n”）为分隔符将文本分割成多行，最后通过计算得到的行数就是光标的行号。

    int column = caretPosition - textArea.getText().lastIndexOf('\n', caretPosition - 1);
计算光标当前所在的列号。 首先找到最后一个换行符（“\n”）的位置， 然后用当前的光标位置减去这个位置，得到的结果就是光标的列号。

***最后在Main.java通过相应的get方法构造界面并显示。***

- 文本的撤销与重做功能（自带）
- 允许用户在文本编辑区域内输入和编辑文本。（自带）
- 允许用户切换是否自动换行。
`textArea.setWrapText(true);`

***Fitten code 插件，伟大，无需多言***

----

## 2024-05-07

- 完成文本编辑的  
1. 剪切 Cut();
2. 复制 Copy();
3. 粘贴 Paste();
4. 全选 SelectAll();
5. 时间/日期  TimeDate();

基本的原理都是设置监听器监听点击事件，然后完成相应的动作。

这些功能TextArea已经自带，或者调用了TextArea的相应方法实现。

自己实现了，但是感觉多此一举）。

### 例如剪切 Cut();
```
 public void Cut()
    {
        MenuItem CutMenuItem = new MenuItem("剪切");
        CutMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        // textArea.cut(); 就能完成
        try {
            CutMenuItem.setOnAction(event -> { // 添加了一个点击事件监听器
                Platform.runLater(() -> {
                    // 获取系统剪贴板
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(textArea.getSelectedText());
                    clipboard.setContent(content);
                    textArea.replaceSelection("");
                });
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        editMenu.getItems().add(CutMenuItem);
    }
```

~~想了想还是先去搞文件操作吧，万一文件都打不开就惨了~~

------
## 2024-05-08

~~byd转到功能硬控我两个小时，发现状态栏显示行数也写错了， 如果text只有换行符行数就为0，
把分割条件改split("\n",-1)就行了;状态栏的字符统计也写错了，跟行列号的监听是一起的，只有在光标位置改变时才更新~~
### 实现了转到功能
- 转到功能用的TextInputDialog，可以让用户输入内容。用监听器和正则表达式限制输入内容。
- 输入不合法内容时，会弹出警告框，用While循环实现输入合法内容或点击取消按钮才关闭对话框。

### 实现了删除
- 选中则删除选中，否则删除光标后面的字符。

### 转到功能部分代码

```
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

```
------
## 2024-05-09

- ~~优化了一些代码，把TextArea改成了静态变量，在菜单栏的类中初始化，子菜单继承父菜单，可以直接使用TextArea。~~
- ~~statusBar状态栏也设置为静态变量，方便显示和隐藏状态栏。~~
- ***慎用静态变量！！！*** 新建窗口各窗口的状态应该保持独立。
- 鉴定为能不用尽量不用static
<br><br>
- 增加状态栏开关
- 增加新建功能
- 增加打开txt文件功能
- 实现了保存和另存为功能，另存为后，当前编辑的文件变为保存的文件
- 实现窗口关闭监听器，监听当前窗口内容是否保存

最头疼的代码片段
```
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

```

~~真的是bug满天飞，包括但不限于打开的多个窗口都链接的同一个文件~~

~~不出以外的话应该能在周末结束吧，大概（~~

- 晚上
- 增加了编码显示功能，支持UTF-8、UTF-16、GB2312
- 发现传参有亿点点不合理，基本重构，还好影响不大
- 增加文件是否保存显示，如果文件未保存，文件名前面会显示“*”号
```
 public void CheckSave(Stage stage) {

        textArea.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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
```
~~越写越乱，都快不知道自己在写什么了；增加一个功能就会出现若干bug~~

## 2024-05-10

- 研究了半天怎么打包程序，虽然还有功能没写完）

  遇到的问题和解决方法[https://blog.csdn.net/qq_52144300/article/details/125224207]
- 增加了查找功能，功能基本按照IDEA的查找功能实现
- 查找和替换应该是最难写的了,~~要不写一起算了，好麻烦~~

### 查找(废案)
```
public void find(boolean Type,TextField searchField,CheckBox caseSensitive,CheckBox word,CheckBox regex)
    {
        String searchText = searchField.getText();


        // 如果选择了正则表达式
        if(!regex.isSelected())
                 searchText = Pattern.quote(searchText);
        // 如果选择了全词匹配
        if (word.isSelected()) {
            searchText = "\\b" + searchText + "\\b";
        }


        Pattern pattern;
        if(caseSensitive.isSelected())
            pattern= Pattern.compile(searchText);
        else
            pattern= Pattern.compile(searchText,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textArea.getText());
        totalCount=matcher.results().count();
        //System.out.println("总共找到"+totalCount+"个匹配");


        if(!Type) {
            if (currentIndex < totalCount) currentIndex++;
            //System.out.println("查找下一个");
        }
            else
        {
            if(currentIndex>1)currentIndex--;
           // System.out.println("查找上一个");
        }
        matcher.reset();


        int cnt=0;
        while (matcher.find()) {

            cnt++;
            if(cnt==currentIndex)
            {
                textArea.positionCaret(matcher.start());

                textArea.selectRange(matcher.start(), matcher.end());

                break;
            }

        }
    }

```
- 之前的代码有点啸问题，推翻重写了，被硬控四个小时

## 2024-05-11

- 实现改变字体大小、颜色、字体样式功能
- 实现改变文本对齐

~~大体完成了要求的功能，太累人了，不给自己没事找事写附加功能了~~

## Main

~~~
public void newStage(File file,Stage  primaryStage)
    {
    //打开文件
        if(file==null)
            primaryStage.setTitle("Notepad");
        else
            primaryStage.setTitle(file.getName());

    //加载图标，设置标题
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.setTitle("Notepad");

        Image icon = new Image("/icon.png");
        if (icon.isError()) {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.log(Level.SEVERE, "Error loading icon");
        } else {
            primaryStage.getIcons().add(icon);
        }

    //创建组件,分别为文本编辑区域、菜单栏、状态栏、布局
        NotepadTextArea notepadTextArea;
        NotepadMenu notepadMenu;
        NotepadStatusBar statusBar;
        BorderPane borderPane;

        // 创建文本编辑区域
        notepadTextArea = new NotepadTextArea();
        TextArea textArea = notepadTextArea.getTextArea();
        //初始化TextArea的样式,原本还想搞个深色模式的，懒的搞了
        NotepadTheme.initTheme(textArea);
        //读取文件内容
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
                Logger logger = Logger.getLogger(Main.class.getName());
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
  
        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(scene);
        //以下都是因为没考虑好参数传递，需要就传的方法，就突出个高耦合
        //传入stage，用于设置状态栏是否显示
        notepadMenu.setBorderPane(borderPane,statusBar,primaryStage);
        //传入file，用于文件操作
        notepadMenu.initFileMenu(primaryStage,file);
        //传入菜单栏的BOX，用于查找和替换那里
        notepadMenu.initEditMenu(MenuBox);
        notepadMenu.filemenu.CheckIfSave();//是否保存
      
       //就只是用来获取文件名的（）
        notepadTextArea.setFile(file);
        //如果文件没有保存，显示*号
        notepadTextArea.CheckSave(primaryStage);

        primaryStage.show();


    }

~~~


