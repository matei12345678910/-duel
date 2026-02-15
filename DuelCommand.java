package ro.matei.duel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class DuelCommand implements CommandExecutor {

    private final MateiDuel plugin;
    private final HashMap<UUID, UUID> requests = new HashMap<>();
    private final HashMap<UUID, UUID> inDuel = new HashMap<>();

    public DuelCommand(MateiDuel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Doar jucatorii pot folosi asta.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("setarena")) {
            if (!p.hasPermission("duel.setarena")) {
                p.sendMessage(ChatColor.RED + "Nu ai permisiune.");
                return true;
            }
            plugin.setArena(p.getLocation());
            p.sendMessage(ChatColor.GREEN + "Arena setata aici.");
            return true;
        }

        if (plugin.getArenaSpawn() == null) {
            p.sendMessage(ChatColor.RED + "Arena nu este setata. Foloseste /duel setarena");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.YELLOW + "Foloseste: /duel <jucator> sau /duel accept");
            return true;
        }

        if (args[0].equalsIgnoreCase("accept")) {
            UUID targetId = requests.get(p.getUniqueId());
            if (targetId == null) {
                p.sendMessage(ChatColor.RED + "Nu ai nicio cerere de duel.");
                return true;
            }

            Player target = Bukkit.getPlayer(targetId);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "Jucatorul nu mai este online.");
                return true;
            }

            startDuel(p, target);
            requests.remove(p.getUniqueId());
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || target == p) {
            p.sendMessage(ChatColor.RED + "Jucator invalid.");
            return true;
        }

        if (inDuel.containsKey(p.getUniqueId()) || inDuel.containsKey(target.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "Cineva este deja intr-un duel.");
            return true;
        }

        requests.put(target.getUniqueId(), p.getUniqueId());
        p.sendMessage(ChatColor.GREEN + "Ai trimis cerere de duel lui " + target.getName());
        target.sendMessage(ChatColor.GOLD + p.getName() + " te-a provocat la duel! Scrie /duel accept");

        return true;
    }

    private void startDuel(Player p1, Player p2) {
        Location arena = plugin.getArenaSpawn();

        inDuel.put(p1.getUniqueId(), p2.getUniqueId());
        inDuel.put(p2.getUniqueId(), p1.getUniqueId());

        // TODO: curata inventory + da kit PVP
        // TODO: salveaza locatia veche si trimite-i inapoi dupa duel

        p1.teleport(arena);
        p2.teleport(arena);

        p1.sendMessage(ChatColor.GREEN + "Duel inceput cu " + p2.getName());
        p2.sendMessage(ChatColor.GREEN + "Duel inceput cu " + p1.getName());
    }

    public HashMap<UUID, UUID> getInDuel() {
        return inDuel;
    }
}
