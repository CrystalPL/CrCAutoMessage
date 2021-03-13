package pl.crystalek.crcautomessage;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcautomessage.command.AutoMessageCommand;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.task.AutoMessageTask;

public final class CrCAutoMessage extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        final MessageManager messageManager = new MessageManager();
        final FileManager fileManager = new FileManager(this, messageManager);
        fileManager.load();
        final AutoMessageTask autoMessageTask = new AutoMessageTask(this, messageManager);
        autoMessageTask.start();
        this.getCommand("automsg").setExecutor(new AutoMessageCommand(messageManager, fileManager, this, autoMessageTask));
    }
}
