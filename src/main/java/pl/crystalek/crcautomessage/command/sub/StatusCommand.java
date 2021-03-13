package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.message.Message;

import java.util.Optional;

public class StatusCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;
    private final CommandUtil commandUtil;

    public StatusCommand(final FileManager fileManager, final MessageManager messageManager, final CommandUtil commandUtil) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
        this.commandUtil = commandUtil;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Optional<Short> messageIdOptional = commandUtil.checkArgument(sender, args);
        if (!messageIdOptional.isPresent()) {
            return;
        }

        final Short messageId = messageIdOptional.get();
        if (args.length != 2) {
            sender.sendMessage(fileManager.getMsg("status.usage"));
            return;
        }

        final Message message = messageManager.getAutoMsgMap().get(messageId);
        final boolean status = !message.isStatus();
        fileManager.setMessage(args[1] + ".status", status);
        message.setStatus(status);
        sender.sendMessage(fileManager.getMsg("status.changeStatus").replace("{ID}", args[1]).replace("{STATUS}", status ? "wlaczona" : "wylaczona"));
    }
}
