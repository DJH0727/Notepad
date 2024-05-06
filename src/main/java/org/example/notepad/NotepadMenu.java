package org.example.notepad;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
    时间/日期   timeDateMenuItem
    字体  fontMenuItem
    缩放  zoomMenuItem
帮助  helpMenu
    关于  aboutMenuItem
*/
public class NotepadMenu {
    private MenuBar menuBar;

    public NotepadMenu() {
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("文件");
        // 创建二级菜单
        MenuItem newMenuItem = new MenuItem("新建");
        fileMenu.getItems().add(newMenuItem);
        MenuItem openMenuItem = new MenuItem("打开");
        fileMenu.getItems().add(openMenuItem);
        MenuItem saveMenuItem = new MenuItem("保存");
        MenuItem saveAsMenuItem = new MenuItem("另存为");
        fileMenu.getItems().add(saveAsMenuItem);
        fileMenu.getItems().add(saveMenuItem);
        MenuItem exitMenuItem = new MenuItem("退出");
        fileMenu.getItems().add(exitMenuItem);
        //--------------------------------------------
        Menu editMenu = new Menu("编辑");
        MenuItem undoMenuItem = new MenuItem("撤销");
        editMenu.getItems().add(undoMenuItem);
        MenuItem cutMenuItem = new MenuItem("剪切");
        editMenu.getItems().add(cutMenuItem);
        MenuItem copyMenuItem = new MenuItem("复制");
        editMenu.getItems().add(copyMenuItem);
        MenuItem pasteMenuItem = new MenuItem("粘贴");
        editMenu.getItems().add(pasteMenuItem);
        MenuItem deleteMenuItem = new MenuItem("删除");
        editMenu.getItems().add(deleteMenuItem);
        MenuItem findMenuItem = new MenuItem("查找");
        editMenu.getItems().add(findMenuItem);
        MenuItem replaceMenuItem = new MenuItem("替换");
        editMenu.getItems().add(replaceMenuItem);
        MenuItem gotoMenuItem = new MenuItem("转到");
        editMenu.getItems().add(gotoMenuItem);
        MenuItem selectAllMenuItem = new MenuItem("全选");
        editMenu.getItems().add(selectAllMenuItem);
        MenuItem timeDateMenuItem = new MenuItem("时间/日期");
        editMenu.getItems().add(timeDateMenuItem);
        MenuItem fontMenuItem = new MenuItem("字体");
        editMenu.getItems().add(fontMenuItem);
        MenuItem zoomMenuItem = new MenuItem("缩放");
        editMenu.getItems().add(zoomMenuItem);
        //--------------------------------------------
        Menu helpMenu = new Menu("帮助");
        MenuItem aboutMenuItemMenuItem = new MenuItem("关于");
        helpMenu.getItems().add(aboutMenuItemMenuItem);



        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }






}
