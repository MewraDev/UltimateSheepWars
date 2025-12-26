package fr.royalpha.sheepwars.v1_9_R2;

import fr.royalpha.sheepwars.api.PlayerData;
import fr.royalpha.sheepwars.core.handler.Particles;
import fr.royalpha.sheepwars.core.manager.ExceptionManager;
import fr.royalpha.sheepwars.core.version.IParticleSpawner;
import net.minecraft.server.v1_9_R2.EnumParticle;
import net.minecraft.server.v1_9_R2.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
            String particleName = particle.toString();

            // Particles with block/item data require NMS as the Bukkit API
            // doesn't properly handle data in 1.9, causing missing textures
            if (particleName.equals("ITEM_CRACK") || particleName.equals("BLOCK_CRACK") || particleName.equals("BLOCK_DUST")) {
                EnumParticle enumParticle = EnumParticle.valueOf(particleName);
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                        enumParticle,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        fx, fy, fz,
                        particleData,
                        amount,
                        list
                );
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                return;
            }

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
