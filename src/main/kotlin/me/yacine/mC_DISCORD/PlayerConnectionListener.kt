package me.yacine.mC_DISCORD

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.json.JSONObject

class PlayerConnectionListener(private val plugin: MC_DISCORD) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val p = event.player
        plugin.logger.info("${p.name} joined the player!")
        val payload = JSONObject()
            .put("username", p.name)

        plugin.emitToBackend("player_join", payload)
        plugin.server.scheduler.runTaskLater(
            plugin,
            Runnable {
                plugin.updateStatus()
            },
            10L // 2 seconds
        )
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val p = event.player
        plugin.logger.info("${p.name} left the player!")
        val payload = JSONObject()
            .put("username", p.name)

        plugin.emitToBackend("player_left", payload)
        plugin.server.scheduler.runTaskLater(
            plugin,
            Runnable {
                plugin.updateStatus()
            },
            10L // 2 seconds
        )
    }
}
