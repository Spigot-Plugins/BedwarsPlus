package me.unldenis.bedwars.villager;

import java.util.Map;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import me.unldenis.bedwars.object.GamePlayer;
import org.bukkit.potion.PotionData;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import me.unldenis.bedwars.object.teams.TeamType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import me.unldenis.bedwars.util.MyItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item
{
    private final ItemStack item;
    private Currency currencyItem;
    private int price;
    private final boolean isBuyable;
    
    public Item(final Material mat, final int amount, final String name, final Currency itemCurrency, final int price) {
        final MyItems itm = new MyItems();
        this.item = itm.nameItem(new ItemStack(mat, amount), name, String.valueOf(price) + " " + itemCurrency.toString());
        this.price = price;
        this.currencyItem = itemCurrency;
        this.isBuyable = true;
    }
    
    public Item(final Material mat, final String name, final String lore) {
        final MyItems itm = new MyItems();
        this.item = itm.nameItem(new ItemStack(mat, 1), name, lore);
        this.isBuyable = false;
    }
    
    public Item(final Material mat, final Enchantment enchantment, final int livello, final int amount, final String name, final Currency itemCurrency, final int price) {
        final MyItems itm = new MyItems();
        this.item = itm.nameItem(new ItemStack(mat, amount), name, String.valueOf(price) + " " + itemCurrency.toString());
        final ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchantment, livello, false);
        this.item.setItemMeta(meta);
        this.price = price;
        this.currencyItem = itemCurrency;
        this.isBuyable = true;
    }
    
    public Item(final Material mat, final TeamType team, final Enchantment enchantment, final int livello, final int amount, final String name, final Currency itemCurrency, final int price) {
        final MyItems itm = new MyItems();
        this.item = itm.nameItem(new ItemStack(mat, amount), name, String.valueOf(price) + " " + itemCurrency.toString());
        final LeatherArmorMeta meta = (LeatherArmorMeta)this.item.getItemMeta();
        meta.setColor(itm.colorFromString(team.toString()));
        meta.addEnchant(enchantment, livello, false);
        this.item.setItemMeta((ItemMeta)meta);
        this.price = price;
        this.currencyItem = itemCurrency;
        this.isBuyable = true;
    }
    
    public Item(final Material mat, final int amount, final PotionType type, final boolean extend, final boolean upgraded, final String name, final Currency itemCurrency, final int price) {
        final MyItems itm = new MyItems();
        this.item = itm.nameItem(new ItemStack(mat, amount), name, String.valueOf(price) + " " + itemCurrency.toString());
        final PotionMeta meta = (PotionMeta)this.item.getItemMeta();
        meta.setBasePotionData(new PotionData(type, extend, upgraded));
        this.item.setItemMeta((ItemMeta)meta);
        this.price = price;
        this.currencyItem = itemCurrency;
        this.isBuyable = true;
    }
    
    @SuppressWarnings("deprecation")
	public void buy(final GamePlayer player) {
        if (this.isBuyable) {
            if (Currency.Bronze.equals(this.currencyItem)) {
                if (this.consumeItem(player.getPlayer(), this.price, Material.LEGACY_CLAY_BRICK)) {
                    player.getPlayer().getInventory().addItem(new ItemStack[] { this.item });
                }
                else {
                    player.sendMessage(ChatColor.RED + "You don't have enough resources to buy this item");
                }
            }
            else if (Currency.Iron.equals(this.currencyItem)) {
                if (this.consumeItem(player.getPlayer(), this.price, Material.IRON_INGOT)) {
                    player.getPlayer().getInventory().addItem(new ItemStack[] { this.item });
                }
                else {
                    player.sendMessage(ChatColor.RED + "You don't have enough resources to buy this item");
                }
            }
            else if (Currency.Gold.equals(this.currencyItem)) {
                if (this.consumeItem(player.getPlayer(), this.price, Material.GOLD_INGOT)) {
                    player.getPlayer().getInventory().addItem(new ItemStack[] { this.item });
                }
                else {
                    player.sendMessage(ChatColor.RED + "You don't have enough resources to buy this item");
                }
            }
        }
    }
    
    public boolean consumeItem(final Player player, int count, final Material mat) {
        final Map<Integer, ? extends ItemStack> ammo = (Map<Integer, ? extends ItemStack>)player.getInventory().all(mat);
        int found = 0;
        for (final ItemStack stack : ammo.values()) {
            found += stack.getAmount();
        }
        if (count > found) {
            return false;
        }
        for (final Integer index : ammo.keySet()) {
            final ItemStack stack2 = (ItemStack)ammo.get(index);
            final int removed = Math.min(count, stack2.getAmount());
            count -= removed;
            if (stack2.getAmount() == removed) {
                player.getInventory().setItem((int)index, (ItemStack)null);
            }
            else {
                stack2.setAmount(stack2.getAmount() - removed);
            }
            if (count <= 0) {
                break;
            }
        }
        player.updateInventory();
        return true;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
    
    public Currency getCurrencyItem() {
        return this.currencyItem;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public enum Currency
    {
        Bronze("Bronze", 0), 
        Iron("Iron", 1), 
        Gold("Gold", 2);
        
        private Currency(final String name, final int ordinal) {
        }
    }
}
