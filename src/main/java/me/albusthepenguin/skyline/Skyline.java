/*
 * This file is part of Skyline, licensed under the MIT License.
 *
 *  Copyright (c) AlbusThePenguin (Albus) <SlapTheTroll@Spigot>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package me.albusthepenguin.skyline;

import lombok.Getter;
import me.albusthepenguin.skyline.API.ConfigType;
import me.albusthepenguin.skyline.API.Debug;
import me.albusthepenguin.skyline.Commands.CommandManager;
import me.albusthepenguin.skyline.Hook.HookHandler;
import me.albusthepenguin.skyline.Hook.HookListener;
import me.albusthepenguin.skyline.Misc.Configuration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Skyline extends JavaPlugin {

    private Debug debug;

    private Configuration configuration;

    private HookHandler hookHandler;

    private final String usePermission = "skyline.use";

    private final String adminPermission = "skyline.admin";

    @Override
    public void onEnable() {
        debug = new Debug(this);

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        configuration = new Configuration(this);
        configuration.load();

        hookHandler = new HookHandler(this);

        PluginCommand skylineCommand = getCommand("skyline");
        if(skylineCommand != null) {
            skylineCommand.setExecutor(new CommandManager(this));
        }

        getServer().getPluginManager().registerEvents(new HookListener(this, hookHandler), this);
    }

    public String getMessage(String path) {
        ConfigurationSection section = configuration.getConfig(ConfigType.Messages).getConfigurationSection("Messages");
        if(section == null) {
            debug.write("[NullPointer] section in 'getMessage' returns null. Please notify developer.");
            return "";
        }

        String message = section.getString(path);
        if(message == null) {
            debug.write("[NullPointer] " + path + " is not available in messages.yml. Please add " + path + ": <message> in messages.yml to correct this.");
            return "";

        }
        return message;
    }

    public String color(String text) {
        String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }
}
