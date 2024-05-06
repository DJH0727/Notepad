package org.example.notepad;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class NotepadMenu {
    private MenuBar menuBar;

    public NotepadMenu() {
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("文件");
        Menu editMenu = new Menu("编辑");
        Menu helpMenu = new Menu("帮助");
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
