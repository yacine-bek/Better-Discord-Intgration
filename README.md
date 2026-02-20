## Better Discord Integration

🔗 **Download on Modrinth:**  
[Better Discord Integration on Modrinth](https://modrinth.com/plugin/better-discord-integration)

---

## Setup Guide (From Zero)

Follow these steps to connect your Minecraft server to Discord.

---

### 1️⃣ Create a Discord Webhook
- Open your Discord server
- Go to **Server Settings → Integrations → Webhooks**
- Create a new webhook
- Choose the **channel** where Minecraft messages should appear
- Copy the webhook URL

---

### 2️⃣ Set the Webhook in Minecraft
- Join your Minecraft server as **admin**
- Run this command:
```
/set-webhook <webhook_url>
```

- If you **don’t have admin permissions**:
  - Open:
    ```
    /plugins/MC-DISCORD/Webhook-URL.json
    ```
  - Paste the webhook URL inside the file
  - Save and restart the server

- If successful, you will see:
  - `Webhook saved!`
  - `Connected!`
  - A clickable message to add the Discord bot

![Webhook connected](https://cdn.modrinth.com/data/cached_images/e774de5f85ad30bb3fc02425ed2b73ca176031b9.png)

---

### 3️⃣ Add the Discord Bot
- Click the message:  
  **[Click to add Discord Bot]**
- Add the bot **Nexus** to your Discord server
- Grant the required permissions

Once added, the setup is complete.

---

## What Happens After Setup

### ✅ Server Status & Online Players
- When the server starts, the plugin connects automatically
- The Discord bot nickname updates to show:
  - Server status
  - Number of online players  
  **Example:** `MC: 0 online`

![Server status](https://cdn.modrinth.com/data/cached_images/caf4bc19056629f724ad61e3d791fd9eb2b6551f.png)

---

### 👋 Player Join & Leave
- When a player joins or leaves:
  - A message is sent to the Discord channel

![Join / Leave](https://cdn.modrinth.com/data/cached_images/1fb156d64f1a667023c3715d73135c7d0b446c12.png)

---

### 💬 Chat Synchronization

- **Minecraft → Discord**
  - Messages appear in Discord
  - The player’s Minecraft head is shown as the profile picture

- **Discord → Minecraft**
  - Messages appear in Minecraft chat
  - Colored in **purple** so you know they come from Discord

![Discord side](https://cdn.modrinth.com/data/cached_images/15a6865874caaf90ede48c090558f32e9003b787.png)
![Minecraft side](https://cdn.modrinth.com/data/cached_images/a637e55bfe4103c130ed15c66850c79692218d19.png)

---

## Account Linking (Optional)

### 🔗 How Linking Works
1. In Discord, type:
```
/link <minecraft_username>
```
2. You **must be online** in Minecraft
3. A verification code appears in Minecraft chat
4. Enter the code in Discord
5. Accounts are now linked

![Link command](https://cdn.modrinth.com/data/cached_images/112bcbba4d50b6e659dbfde58fe314fb47a01fd7.png)
![Enter code](https://cdn.modrinth.com/data/cached_images/5fc155c6140f488283e5f25656f5eb4fdf6469d7.png)
![Minecraft code](https://cdn.modrinth.com/data/cached_images/78da326f32e2193d9207967bceecb8d158406436.png)

Once linked:
- Discord messages show your **Minecraft username**
- No impersonation
- Clean and consistent identity

---

## Done ✅
Your Minecraft server is now fully connected to Discord.
