package fr.royalpha.sheepwars.core.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.royalpha.sheepwars.api.SheepWarsSheep;
import fr.royalpha.sheepwars.core.SheepWarsPlugin;
import fr.royalpha.sheepwars.core.event.UltimateSheepWarsEventListener;
import fr.royalpha.sheepwars.core.handler.Sounds;

public class PlayerPickupItem extends UltimateSheepWarsEventListener {

    public PlayerPickupItem(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();

        if (!item.getType().toString().contains("WOOL")) {
            return;
        }

        // Identify the sheep via its NBT tag
        SheepWarsSheep sheepType = SheepWarsSheep.getCorrespondingSheepByTag(item);

        if (sheepType != null) {
            // Cancel default pickup
            event.setCancelled(true);

            // Remove the item from the ground
            event.getItem().remove();

            // Play pickup sound
            Sounds.playSound(player, player.getLocation(), Sounds.ITEM_PICKUP, 1f, 1f);

            // Give the sheep to the player with the name in their language.
            SheepWarsSheep.giveSheep(player, sheepType, item.getAmount());
        }
    }
}