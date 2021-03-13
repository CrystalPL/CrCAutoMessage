package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.task.AutoMessageTask;

public final class ReloadCommand implements SubCommand {
    private final FileManager fileManager;
    private final AutoMessageTask autoMessageTask;

    public ReloadCommand(final FileManager fileManager, final AutoMessageTask autoMessageTask) {
        this.fileManager = fileManager;
        this.autoMessageTask = autoMessageTask;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("reload.usage"));
            return;
        }

        fileManager.reload();
        autoMessageTask.cancelTask();
        autoMessageTask.start();
        sender.sendMessage(fileManager.getMsg("reload.reload"));
    }
}
