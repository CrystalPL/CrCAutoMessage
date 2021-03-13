package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.MessageManager;

import java.util.Optional;

public final class DeleteCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;
    private final CommandUtil commandUtil;

    public DeleteCommand(final FileManager fileManager, final MessageManager messageManager, final CommandUtil commandUtil) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
        this.commandUtil = commandUtil;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Optional<Short> messageId = commandUtil.checkArgument(sender, args);
        if (!messageId.isPresent()) {
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(fileManager.getMsg("delete.usage"));
            return;
        }

        fileManager.setMessage(args[1], null);
        messageManager.removeMessage(messageId.get());
        sender.sendMessage(fileManager.getMsg("delete.deleteMessage").replace("{ID}", args[1]));
    }
}
