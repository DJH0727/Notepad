# java记事本开发日志

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

## 2024-05-05

~~新建文件夹(bushi)~~

**准备基本照着Windows记事本的功能设计**


---

## 2024-05-06

- 完成菜单栏、文本编辑区域、状态栏的UI，但没有实现具体功能。

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







