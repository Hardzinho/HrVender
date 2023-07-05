package hrstore.vender.dao;

import hrstore.vender.model.Items;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemsDAO {

    @Getter
    private static List<Items> items = new ArrayList<>();

    public static Items findItem(ItemStack item) {
        return items.stream().filter(items1 -> items1.getItem().isSimilar(item)).findFirst().orElse(null);
    }
}