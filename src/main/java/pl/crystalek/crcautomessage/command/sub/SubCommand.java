package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;

interface SubCommand {

    void execute(final CommandSender sender, final String[] args);

}