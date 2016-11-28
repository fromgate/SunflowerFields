package me.fromgate.sunflowerfields;

import org.bukkit.plugin.java.JavaPlugin;

public class SunflowerFields extends JavaPlugin {

    private static SunflowerFields instance;

    public static SunflowerFields getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Cfg.init();
        Farms.init();
        getServer().getPluginManager().registerEvents(new SunflowerListener(), this);
        getCommand("sunflower").setExecutor(new Cmd());
    }


}
