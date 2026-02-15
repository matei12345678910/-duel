package ro.matei.duel;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class DuelListener implements Listener {

    private final MateiDuel plugin;

    public DuelListener(MateiDuel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        MateiDuel main = MateiDuel.getInstance();

        DuelCommand cmd = (DuelCommand) main.getCommand("duel").getExecutor();
        if (cmd == null) return;

        if (!cmd.getInDuel().containsKey(dead.getUniqueId())) return;

        UUID otherId = cmd.getInDuel().get(dead.getUniqueId());
        Player winner = dead.getServer().getPlayer(otherId);

        cmd.getInDuel().remove(dead.getUniqueId());
        if (winner != null) {
            cmd.getInDuel().remove(winner.getUniqueId());
            winner.sendMessage(ChatColor.GOLD + "Ai castigat duelul contra lui " + dead.getName() + "!");
        }

        dead.sendMessage(ChatColor.RED + "Ai pierdut duelul.");
    }
}
