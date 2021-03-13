package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.MessageManager;

public class PrintCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;

    public PrintCommand(final FileManager fileManager, final MessageManager messageManager) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("print.usage"));
            return;
        }

        final boolean status = !messageManager.isAutoMsgStatus();
        messageManager.setAutoMsgStatus(status);
        sender.sendMessage(fileManager.getMsg("print.messageStatus").replace("{STATUS}", status ? "wlaczone" : "wylaczone"));
    }
}
