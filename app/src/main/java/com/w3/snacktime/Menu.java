package com.w3.snacktime;

/**
 * Created by W3E16 on 05-Apr-18.
 */

public class Menu {

    private int id;
    private String menuname;
    private int image;
    private boolean checkbox;

    public Menu(int id, String menuname, int image, boolean checkbox) {
        this.id = id;
        this.menuname = menuname;
        this.image = image;
        this.checkbox = checkbox;
    }

    public int getId() {
        return id;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public String getMenuname() {
        return menuname;
    }

    public int getImage() {
        return image;
    }
}
