package me.yacine.mC_DISCORD

import io.socket.client.IO
import io.socket.client.Socket
import org.bukkit.Bukkit
import org.json.JSONObject
import java.net.URI

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.hover.content.Text

class BackendSocketClient(
    backendUrl: String,
    webHook: String,
    private val plugin: MC_DISCORD,
) {

    private val socket: Socket

    init {
        val opts = IO.Options().apply {
            path = "/socket.io"
            auth = mapOf("webHook" to webHook)
            reconnection = true
        }

        socket = IO.socket(URI.create("$backendUrl/ws"), opts)

        socket.on(Socket.EVENT_CONNECT) {
            plugin.logger.info("connected socket=${socket.id()}")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            plugin.logger.info("disconnected socket")
        }

        // Backend asks MC to show link code to a player
        socket.on("Link_Request") { args ->
            val payload = (args.getOrNull(0) as? JSONObject) ?: JSONObject()
            val mcUser = payload.optString("mc_user", "")
            val code = payload.optString("code", "")

            if (mcUser.isBlank() || code.isBlank()) {
                plugin.logger.warning("Link_Request received but missing fields: $payload")
                return@on
            }

            Bukkit.getScheduler().runTask(plugin, Runnable {
                val player = Bukkit.getPlayerExact(mcUser)
                if (player != null) {
                    player.sendMessage("§aYour Discord link code: §e$code")
                    player.sendMessage("§7Use this code in Discord to finish linking.")
                } else {
                    plugin.logger.warning("Player $mcUser is not online, cannot send link code")
                }
            })
        }

        // Backend error event (your backend sends {message: "..."} not {content: "..."} sometimes)
        socket.on("error") { args ->
            val payload = (args.getOrNull(0) as? JSONObject) ?: JSONObject()

            // Accept multiple possible keys
            val msg =
                payload.optString("content").ifBlank {
                    payload.optString("message").ifBlank {
                        payload.optString("error").ifBlank {
                            payload.toString()
                        }
                    }
                }

            Bukkit.getScheduler().runTask(plugin, Runnable {
                Bukkit.broadcastMessage("§8[§cERROR§8] §7$msg")
            })
        }

        // Chat message from backend
        socket.on("message") { args ->
            val payload = (args.getOrNull(0) as? JSONObject) ?: JSONObject()
            val content = payload.optString("content", "")
            val author = payload.optString("author", "Discord")

            if (content.isBlank()) return@on

            Bukkit.getScheduler().runTask(plugin, Runnable {
                Bukkit.broadcastMessage("§8<§5$author§8> §7: §f$content")
            })
        }

        socket.on("discord_info") { args ->
            val payload = (args.getOrNull(0) as? JSONObject) ?: JSONObject()
            val botUrl = payload.optString("content", "")

            if (botUrl.isBlank()) return@on

            Bukkit.getScheduler().runTask(plugin, Runnable {
                val message = TextComponent("§8[§5Click to add Discord Bot§8]")
                message.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, botUrl)
                message.hoverEvent = HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    Text("§aClick to add the Discord bot to your server")
                )

                Bukkit.spigot().broadcast(message)
            })
        }




        socket.connect()
    }

    fun emit(event: String, payload: JSONObject) {
        socket.emit(event, payload)
    }

    fun disconnect() {
        try {
            socket.off() // remove listeners
            socket.disconnect()
            socket.close()
        } catch (_: Exception) {
        }
    }
}
