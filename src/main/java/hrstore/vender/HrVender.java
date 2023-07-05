package hrstore.vender;

import hrstore.vender.commands.SellCommand;
import hrstore.vender.dao.ItemsDAO;
import hrstore.vender.model.Items;
import hrstore.vender.runnable.Cooldown;
import hrstore.vender.utils.DateManager;
import hrstore.vender.utils.ItemBuilder;
import lombok.Getter;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class HrVender extends JavaPlugin {

    @Getter
    private static HrVender Instance;
    private ConfigManager settings;

    private Economy economy = null;

    public void onEnable() {
        Instance = this;
        registerYaml();
        if (setupEconomy())
        registerItems();
        registerCommands();

        loadRunnable();
        sendMessage();

    }

    private void registerCommands() {
        getCommand("vender").setExecutor(new SellCommand());
    }

    private void registerYaml() {
        settings = new ConfigManager();
        saveDefaultConfig();
        settings.loadConfig();
        DateManager.createConfig("items");
    }

    private void loadRunnable() {
        new Cooldown().runTaskTimerAsynchronously(this, 20L, 20L);
    }

    private void sendMessage() {
        Bukkit.getConsoleSender().sendMessage("§6[HrVender] §fCriado por §c[Hard]");
        Bukkit.getConsoleSender().sendMessage("§6[HrVender] §aO plugin foi iniciado com sucesso.");
    }

    private void registerItems() {
        int id = 0;

        for (String path : DateManager.getConfig("items").getConfigurationSection("Items").getKeys(false)) {

            val key = DateManager.getConfig("items").getConfigurationSection("Items." + path);

            val material = Material.valueOf(key.getString("Item").split(":")[0]);
            val data = Integer.parseInt(key.getString("Item").split(":")[1]);

            val value = key.getDouble("Valor");

            val item = new ItemBuilder(material, 1, data).build();

            val nmsItem = CraftItemStack.asNMSCopy(item);
            val itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
            itemCompound.setInt("Item-ID", id);
            nmsItem.setTag(itemCompound);

            val items = new Items(item, value, id);
            ItemsDAO.getItems().add(items);
            id++;

        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }
}