package hrstore.vender.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class Items {

    private ItemStack item;
    private double value;
    private int id;

}