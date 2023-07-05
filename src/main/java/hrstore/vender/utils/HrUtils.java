package hrstore.vender.utils;

import hrstore.vender.HrVender;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class HrUtils {

    private static final HrVender main = HrVender.getPlugin(HrVender.class);
    private static final FileConfiguration config = main.getConfig();

    private static String prefix;

    public static void sendMessage(CommandSender p, String message) {
        if (config.getBoolean("Prefixo.Ativar")) {

            prefix = config.getString("Prefixo.Prefixo").replace("&", "§");

            p.sendMessage(prefix + " " + message.replace("&", "§"));

        } else
            p.sendMessage(message.replace("&", "§"));
    }

    public static void sendActionBar(String text, Player p) {
        sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text.replace("&", "§") + "\"}"), (byte) 2),
                p);
    }

    public static void sendTitle(Player p, String title, String subTitle) {
        title = title.replace("&", "§").replace("{jogador}", p.getName());
        subTitle = subTitle.replace("&", "§").replace("{jogador}", p.getName());

        p.sendTitle(title, subTitle);
    }

    public static void removeItem(Player p, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;

        if (p.getGameMode() == GameMode.CREATIVE) return;

        p.getInventory().removeItem(item);
    }

    public static void playSound(CommandSender s, Sound sound) {
        if (!(s instanceof Player))
            return;

        Player p = (Player)s;
        p.playSound(p.getLocation(), sound, 1.0F, 1.0F);
    }

    public static void runCommandList(Player p, List<String> commands) {
        commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{jogador}", p.getName())));
    }

    public static ItemStack getPlayerHead(String playerName) {
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        meta.setOwner(playerName);
        playerHead.setItemMeta(meta);

        return playerHead;
    }

    public static String Serializer(Location location) {
        String world = location.getWorld().getName();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        return world + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
    }

    public static Location Unserializer(String location) {
        String[] split = location.split(";");

        World world = Bukkit.getWorld(split[0]);

        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);

        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }

    @SuppressWarnings("rawtypes")
    private static void sendPacket(Packet pa, Player p) {
        CraftPlayer cp = (CraftPlayer) p;
        (cp.getHandle()).playerConnection.sendPacket(pa);
    }
}