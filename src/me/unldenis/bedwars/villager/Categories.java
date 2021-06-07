package me.unldenis.bedwars.villager;

import java.util.NoSuchElementException;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class Categories
{
    private int indice;
    private List<Item> items;
    
    public Categories(final int indice) {
        this.indice = indice;
        this.items = new ArrayList<Item>();
    }
    
    public void add(final Item val) {
        this.items.add(val);
    }
    
    public int getIndice() {
        return this.indice;
    }
    
    public Item find(final Material mat, final int pos) {
        try {
            return this.items.stream().filter(i -> i.getItem().getType().equals((Object)mat)).filter(i -> this.items.indexOf(i) >= pos).findFirst().get();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }
    
    public List<Item> getItems() {
        return this.items;
    }
}
