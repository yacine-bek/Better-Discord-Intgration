package me.yacine.mC_DISCORD

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MC_DISCORDCommand(private val plugin: MC_DISCORD) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /$label <webhook_url>")
            return true
        }

        // Webhook URLs should not contain spaces. Take only first arg.
        val newValue = args[0].trim()

        if (!plugin.isValidWebhookUrl(newValue)) {
            sender.sendMessage("§cInvalid webhook URL format.")
            sender.sendMessage("§7Example: https://discord.com/api/webhooks/<id>/<token>")
            return true
        }

        // Save first
        plugin.dataValue = newValue
        plugin.saveData()
        sender.sendMessage("§aWebhook saved!")

        // Reconnect
        val ok = plugin.connectSocket()
        if (ok) {
            sender.sendMessage("§aConnected!.")
        } else {
            sender.sendMessage("§cFailed to connect!")
        }

        return true
    }
}
