package me.yacine.mC_DISCORD

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.json.JSONObject

class ChatListener(private val plugin: MC_DISCORD) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val playerName = player.name
        val uuid = player.uniqueId.toString()

        val message = PlainTextComponentSerializer.plainText().serialize(event.message()).trim()
        if (message.isBlank()) return

        plugin.logger.info("[CHAT] $playerName: $message")

        val payload = JSONObject().apply {
            put("author", playerName)
            put("uuid", uuid)
            put("content", message)
        }

        // Safe emit (won't crash if socket isn't connected)
        plugin.emitToBackend("mc_chat", payload)
    }
}
