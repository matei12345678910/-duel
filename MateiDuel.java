package ro.matei.duel;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class MateiDuel extends JavaPlugin {

    private static MateiDuel instance;
    private Location arenaSpawn;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadArena();

        getCommand("duel").setExecutor(new DuelCommand(this));
        Bukkit.getPluginManager().registerEvents(new DuelListener(this), this);

        getLogger().info("MateiDuel by Matei a fost pornit.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MateiDuel a fost oprit.");
    }

    public static MateiDuel getInstance() {
        return instance;
    }

    public Location getArenaSpawn() {
        return arenaSpawn;
    }

    public void loadArena() {
        if (getConfig().contains("arena.world")) {
            String w = getConfig().getString("arena.world");
            double x = getConfig().getDouble("arena.x");
            double y = getConfig().getDouble("arena.y");
            double z = getConfig().getDouble("arena.z");
            float yaw = (float) getConfig().getDouble("arena.yaw");
            float pitch = (float) getConfig().getDouble("arena.pitch");
            arenaSpawn = new Location(Bukkit.getWorld(w), x, y, z, yaw, pitch);
        }
    }

    public void setArena(Location loc) {
        getConfig().set("arena.world", loc.getWorld().getName());
        getConfig().set("arena.x", loc.getX());
        getConfig().set("arena.y", loc.getY());
        getConfig().set("arena.z", loc.getZ());
        getConfig().set("arena.yaw", loc.getYaw());
        getConfig().set("arena.pitch", loc.getPitch());
        saveConfig();
        arenaSpawn = loc;
    }
}
