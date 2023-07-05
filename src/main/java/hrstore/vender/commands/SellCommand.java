package hrstore.vender.commands;

import hrstore.vender.ConfigManager;
import hrstore.vender.HrVender;
import hrstore.vender.dao.ItemsDAO;
import hrstore.vender.dao.cooldown.CooldownDAO;
import hrstore.vender.utils.Format;
import hrstore.vender.utils.HrUtils;
import hrstore.vender.utils.TimeFormatter;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SellCommand implements CommandExecutor {

    private final ConfigManager settings = HrVender.getInstance().getSettings();
    private final Economy economy = HrVender.getInstance().getEconomy();

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lb, String[] a) {

        if (s instanceof ConsoleCommandSender) {
            s.sendMessage("§cO console não executa esse comando.");
            return true;
        }

        Player p = (Player)s;
        if (!p.hasPermission("hrvender.vender")) {
            p.sendMessage("§cVocê não possui permissão para esse comando.");
            return true;
        }

        double price = 0;
        int selled = 0;

        if (a.length <= 0) {
            if (CooldownDAO.contains(p) && !p.hasPermission("hrvender.bypass.cooldown")) {
                val time = TimeFormatter.format(CooldownDAO.getRemainingTime(p));

                HrUtils.sendMessage(p, settings.getInCooldowm().replace("{tempo}", time));
                return true;
            }

            val contents = p.getInventory().getContents();
            for (int index = 0; index < contents.length; index++) {
                val item = contents[index];
                if (item == null || ItemsDAO.findItem(item) == null) continue;

                val sellable = ItemsDAO.findItem(item);

                selled += item.getAmount();
                price += sellable.getValue() * item.getAmount();

                economy.depositPlayer(p, price);
                p.getInventory().setItem(index, null);

            }
            if (selled > 0) {
                CooldownDAO.add(p);
                HrUtils.sendMessage(p, settings.getItemsSell().replace("{quantia}", Format.formatNumber(selled)).replace("{valor}", Format.formatNumber(price)));

            } else
                HrUtils.sendMessage(p, settings.getNoHaveItems());
        }
        return false;
    }
}