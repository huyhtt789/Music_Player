package com.example.music_player;

public class ItemsList {
    private Folder[] items;

    public ItemsList(Folder[] items){
        this.items = items;
    }

    public Folder[] getItems() {
        return this.items;
    }

    public void setItems(Folder[] items) {
        this.items = items;
    }
}
