package fr.royalpha.sheepwars.v1_15_R1;

import fr.royalpha.sheepwars.api.PlayerData;
import fr.royalpha.sheepwars.core.handler.Particles;
import fr.royalpha.sheepwars.core.manager.ExceptionManager;
import fr.royalpha.sheepwars.core.version.IParticleSpawner;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Generic ParticleSpawner using only Bukkit API for unsupported versions.
 * Warning: Particles requiring material data (ITEM_CRACK, BLOCK_CRACK, BLOCK_DUST)
 * will either show missing textures (magenta/black checkerboard) or not display at all,
 * depending on the Minecraft version.
 */

public class ParticleSpawner implements IParticleSpawner {

    private static boolean reportedError = false;

    @Override
    public void playParticles(Particles particle, Location location, Float fx, Float fy, Float fz, int amount, Float particleData, int... list) {
        ArrayList<OfflinePlayer> copy = new ArrayList<>(PlayerData.getParticlePlayers());
        if (!copy.isEmpty())
            try {
                for (OfflinePlayer p : copy)
                    if ((p.isOnline()) && (p != null))
                        playParticles(p.getPlayer(), particle, location, fx, fy, fz, amount, particleData, list);
            } catch (Exception ex) {
                if (!reportedError) {
                    reportedError = true;
                    ExceptionManager.register(ex, true);
                }
                // Do nothing
            }
    }

    @Override
    public void playParticles(Player player, Particles particle, Location location, Float fx, Float fy, Float fz, int amount, Float particleData, int... list) {
        try {
            player.spawnParticle(Particle.valueOf(particle.toString()), location, amount, (double) fx, (double) fy, (double) fz, (double) particleData);
        } catch (Exception ex) {
            if (!reportedError) {
                reportedError = true;
                ExceptionManager.register(ex, true);
            }
            // Do nothing
        }
    }
}