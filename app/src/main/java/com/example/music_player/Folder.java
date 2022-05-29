package com.example.music_player;

import java.io.Serializable;

public class Folder implements Serializable {
    public String NAME;
    public String PATH;
    public String parent_PATH;

    public Folder(){}
    public String getPATH() {
        return PATH;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getParent_PATH() {
        return parent_PATH;
    }

    public void setParent_PATH(String parent_PATH) {
        this.parent_PATH = parent_PATH;
    }
}
