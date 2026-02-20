package me.yacine.mC_DISCORD

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.net.URL

class MC_DISCORD : JavaPlugin() {

    private var socket: BackendSocketClient? = null


    private lateinit var dataFile: File
    private val gson = Gson()

    var dataValue: String = "" // webhook url

    companion object {
        lateinit var instance: MC_DISCORD
            private set
    }


    override fun onEnable() {
        instance = this

        if (!dataFolder.exists()) dataFolder.mkdirs()
        dataFile = File(dataFolder, "Webhook-URL.json")

        loadData()

        server.pluginManager.registerEvents(ChatListener(this), this)
        server.pluginManager.registerEvents(PlayerConnectionListener(this), this)

        getCommand("set-webhook")?.setExecutor(MC_DISCORDCommand(this))
            ?: logger.severe("")

        // Try auto-connect only if saved url is valid
        if (isValidWebhookUrl(dataValue)) {
            connectSocket()
        } else if (dataValue.isNotBlank()) {
            logger.warning("Saved webhook URL is invalid. Use /set-webhook <url> to fix it.")
        }
    }

    override fun onDisable() {
        socket?.disconnect()
        socket = null
        saveData()
    }

    fun isValidWebhookUrl(url: String): Boolean {
        val u = url.trim()
        val regex = Regex("^https://(canary\\.|ptb\\.)?discord\\.com/api/webhooks/\\d+/[\\w-]+$")
        return regex.matches(u)
    }

    private fun loadData() {
        if (!dataFile.exists()) {
            dataValue = ""
            saveData()
            return
        }

        FileReader(dataFile).use { reader ->
            val json = gson.fromJson(reader, JsonObject::class.java) ?: JsonObject()
            dataValue = json.get("url")?.asString?.trim() ?: ""
        }
    }

    fun saveData() {
        val json = JsonObject()
        json.addProperty("url", dataValue.trim())

        FileWriter(dataFile).use { writer ->
            gson.toJson(json, writer)
        }
    }

    /**
     * Connects/reconnects socket safely.
     * @return true if connect started, false if url missing/invalid.
     */
    fun connectSocket(): Boolean {

        val hook = dataValue.trim()
        if (!isValidWebhookUrl(hook)) return false

        val backendUrl = fetchBackendUrl() ?: return false

        socket?.disconnect()
        socket = null

        socket = BackendSocketClient(
            backendUrl = backendUrl,
            webHook = hook,
            plugin = this
        )

        val payload = JSONObject().put("content", "Server Connected!")
        socket?.emit("server_connected", payload)

        return true
    }

    fun emitToBackend(event: String, payload: JSONObject) {
        socket?.emit(event, payload)
    }

    fun updateStatus() {
        val nbr = server.onlinePlayers.size
        val payload = JSONObject().put("nbr", nbr)
        emitToBackend("update_status", payload)
    }


    fun fetchBackendUrl(): String? {
        return try {
            val url = URL("https://gist.githubusercontent.com")
            url.readText().trim()
        } catch (e: Exception) {
            null
        }
    }


}
