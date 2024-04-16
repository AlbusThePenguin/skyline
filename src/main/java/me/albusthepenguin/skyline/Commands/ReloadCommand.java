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
package me.albusthepenguin.skyline.Commands;

import me.albusthepenguin.skyline.API.ConfigType;
import me.albusthepenguin.skyline.API.SubCommand;
import me.albusthepenguin.skyline.Misc.Configuration;
import me.albusthepenguin.skyline.Skyline;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {

    private final Skyline skyline;

    private final Configuration configuration;

    public ReloadCommand(Skyline skyline) {
        this.skyline = skyline;
        this.configuration = skyline.getConfiguration();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "skyline.admin";
    }

    @Override
    public String getSyntax() {
        return "/skyline reload";
    }

    @Override
    public void perform(Player player, String[] args) {
        String syntax = skyline.getMessage("error_syntax")
                .replace("%syntax%", getSyntax());

        if(args.length != 1) {
            player.sendMessage(skyline.color(syntax));
            return;
        }

        String reloadMessage = skyline.getMessage("success_reload");

        for(ConfigType configType : ConfigType.values()) {
            configuration.reload(configType);
            configuration.save(configType);
            player.sendMessage(skyline.color(reloadMessage).replace("%type%", configType.name()));
        }
    }

    @Override
    public void perform(ConsoleCommandSender console, String[] args) {

        if(args.length != 1) {
            console.sendMessage("Incorrect syntax: " + getSyntax());
            return;
        }

        String message = "%type% has been reloaded.";
        for(ConfigType configType : ConfigType.values()) {
            configuration.reload(configType);
            configuration.save(configType);
            console.sendMessage(message.replace("%type%", configType.name()));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
