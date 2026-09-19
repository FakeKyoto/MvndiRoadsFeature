package net.mvndicraft.townyroads.util;

import com.palmergames.bukkit.towny.object.CellBorder;
import com.palmergames.bukkit.towny.object.WorldCoord;
import com.palmergames.bukkit.towny.utils.BorderUtil;
import com.palmergames.bukkit.util.BukkitParticle;
import net.mvndicraft.townyroads.Road;
import net.mvndicraft.townyroads.TownyRoadsPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public final class RoadClaimEffect {

    private RoadClaimEffect() {}

    public static void playClaimEffect(Player player, WorldCoord worldCoord, Road road) {
        CellBorder border = BorderUtil.getPlotBorder(worldCoord);
        if (border == null || !border.hasAnyBorder()) return;

        World world = worldCoord.getBukkitWorld();
        if (world == null) return;

        Particle particle = BukkitParticle.getBorderParticle();
        Color color = road.isValid()
            ? Color.fromRGB(0xFF, 0xA5, 0x00)
            : Color.fromRGB(0x8B, 0x45, 0x13);

        Consumer<Location> spawner = loc -> {
            world.spawnParticle(particle, loc, 3, 0.15, 0, 0.15, 0,
                new Particle.DustOptions(color, 2.0f));
        };

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 40 || !player.isOnline()) {
                    cancel();
                    return;
                }
                border.runBorderedOnSurface(1, 1, spawner);
                ticks += 5;
            }
        }.runTaskTimer(TownyRoadsPlugin.getInstance(), 0L, 5L);
    }
}
