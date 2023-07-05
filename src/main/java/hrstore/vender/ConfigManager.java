package hrstore.vender;

import lombok.Getter;
import lombok.val;

@Getter
public class ConfigManager {

    private String noHaveItems;
    private String itemsSell;
    private String inCooldowm;

    private long cooldown;

    public void loadConfig() {
        val config = HrVender.getInstance().getConfig();

        noHaveItems = config.getString("Mensagens.SemItems");
        itemsSell = config.getString("Mensagens.ItemsVendidos");
        inCooldowm = config.getString("Mensagens.EmCooldown");

        cooldown = config.getLong("Configuracoes.Tempo");

    }
}