package hrstore.vender.runnable;

import hrstore.vender.dao.cooldown.CooldownDAO;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown extends BukkitRunnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(CooldownDAO::contains).forEach(players -> {
            long time = CooldownDAO.getRemainingTime(players);

            if (time <= 0) {
                CooldownDAO.remove(players);
                return;
            }

            CooldownDAO.getTime().replace(players.getName(), time, (time - 1000L));
        });
    }
}