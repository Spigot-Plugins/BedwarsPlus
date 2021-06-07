package me.unldenis.bedwars.villager;

import org.bukkit.potion.PotionType;
import org.bukkit.enchantments.Enchantment;
import me.unldenis.bedwars.util.MyItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import me.unldenis.bedwars.object.GamePlayer;
import java.util.HashSet;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import me.unldenis.bedwars.object.teams.TeamType;
import java.util.Set;
import java.util.List;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Villager implements InventoryHolder
{
    public static Villager instance;
    private final Inventory inv;
    private List<Item> upItem;
    private Set<Categories> items;
    private TeamType playerTeamType;
    
    public Villager(final TeamType tm) {
        Villager.instance = this;
        this.playerTeamType = tm;
        this.inv = Bukkit.createInventory((InventoryHolder)this, 18, "Item Shop");
        this.upItem = new ArrayList<Item>();
        this.items = new HashSet<Categories>();
        this.init();
    }
    
    public void buyItemFromMaterial(final Integer pos, final GamePlayer pl, final Material mat) {
        if (pos < 9) {
            for (int j = 9; j < 18; ++j) {
                this.inv.setItem(j, (ItemStack)null);
            }
            final Categories c = this.items.stream().filter(i -> i.getIndice() == pos).findFirst().get();
            c.getItems().forEach(i -> this.inv.addItem(new ItemStack[] { i.getItem() }));
        }
        else {
            final Categories c = this.items.stream().filter(i -> i.find(mat, pos - 9) != null).findFirst().get();
            c.find(mat, pos - 9).buy(pl);
        }
    }
    
    private void init() {
        this.upItem();
        final MyItems itm = new MyItems();
        
        final Categories N_0 = new Categories(0);
        N_0.add(new Item(itm.materialFromTeam(this.playerTeamType), 2, null, Item.Currency.Bronze, 1));
        N_0.add(new Item(Material.CUT_RED_SANDSTONE, 1, null, Item.Currency.Bronze, 1));
        N_0.add(new Item(Material.END_STONE, 1, null, Item.Currency.Bronze, 15));
        N_0.add(new Item(Material.IRON_BLOCK, 1, null, Item.Currency.Iron, 4));
        this.items.add(N_0);
        
        final Categories N_2 = new Categories(1);
        N_2.add(new Item(Material.IRON_PICKAXE, Enchantment.DIG_SPEED, 1, 1, null, Item.Currency.Bronze, 12));
        N_2.add(new Item(Material.IRON_PICKAXE, Enchantment.DIG_SPEED, 2, 1, null, Item.Currency.Iron, 6));
        N_2.add(new Item(Material.DIAMOND_PICKAXE, Enchantment.DIG_SPEED, 3, 1, null, Item.Currency.Gold, 2));
        this.items.add(N_2);
        
        final Categories N_3 = new Categories(2);
        N_3.add(new Item(Material.LEATHER_CHESTPLATE, this.playerTeamType, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1, null, Item.Currency.Iron, 3));
        N_3.add(new Item(Material.LEATHER_CHESTPLATE, this.playerTeamType, Enchantment.PROTECTION_ENVIRONMENTAL, 2, 1, null, Item.Currency.Gold, 1));
        this.items.add(N_3);
        
        final Categories N_4 = new Categories(3);
        N_4.add(new Item(Material.STICK, Enchantment.KNOCKBACK, 1, 1, null, Item.Currency.Bronze, 8));
        N_4.add(new Item(Material.IRON_SWORD, Enchantment.DAMAGE_ALL, 1, 1, null, Item.Currency.Iron, 1));
        N_4.add(new Item(Material.IRON_SWORD, Enchantment.DAMAGE_ALL, 2, 1, null, Item.Currency.Iron, 3));
        N_4.add(new Item(Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, 1, 1, null, Item.Currency.Gold, 3));
        this.items.add(N_4);
        
        final Categories N_5 = new Categories(4);
        N_5.add(new Item(Material.FISHING_ROD, 1, null, Item.Currency.Bronze, 32));
        N_5.add(new Item(Material.BOW, 1, null, Item.Currency.Gold, 3));
        N_5.add(new Item(Material.ARROW, 16, null, Item.Currency.Gold, 1));
        this.items.add(N_5);
        
        final Categories N_6 = new Categories(5);
        N_6.add(new Item(Material.COOKED_BEEF, 2, null, Item.Currency.Bronze, 4));
        N_6.add(new Item(Material.GOLDEN_APPLE, 1, null, Item.Currency.Gold, 1));
        this.items.add(N_6);
        
        final Categories N_7 = new Categories(6);
        N_7.add(new Item(Material.CHEST, 1, null, Item.Currency.Iron, 3));
        this.items.add(N_7);
        
        final Categories N_8 = new Categories(7);
        N_8.add(new Item(Material.POTION, 1, PotionType.REGEN, true, false, null, Item.Currency.Iron, 3));
        N_8.add(new Item(Material.POTION, 1, PotionType.SPEED, true, false, null, Item.Currency.Iron, 5));
        N_8.add(new Item(Material.POTION, 1, PotionType.INVISIBILITY, true, false, null, Item.Currency.Gold, 2));
        this.items.add(N_8);
    }
    
    private void upItem() {
        this.inv.clear();
        this.upItem.add(new Item(Material.SANDSTONE, null, null));
        this.upItem.add(new Item(Material.GOLDEN_PICKAXE, null, null));
        this.upItem.add(new Item(Material.IRON_CHESTPLATE, null, null));
        this.upItem.add(new Item(Material.IRON_SWORD, null, null));
        this.upItem.add(new Item(Material.BOW, null, null));
        this.upItem.add(new Item(Material.APPLE, null, null));
        this.upItem.add(new Item(Material.CHEST, null, null));
        this.upItem.add(new Item(Material.EXPERIENCE_BOTTLE, null, null));
        this.upItem.add(new Item(Material.TNT, null, null));
        this.upItem.stream().map(item -> item.getItem()).forEach( arg0 -> this.inv.addItem(new ItemStack[] {  arg0 }));
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
}
