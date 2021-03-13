package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.util.NumberUtil;

import java.util.Optional;
import java.util.Set;

public final class CommandUtil {
    private final FileManager fileManager;
    private final MessageManager messageManager;

    public CommandUtil(final FileManager fileManager, final MessageManager messageManager) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
    }

    public Optional<Short> checkArgument(final CommandSender sender, final String[] args) {
        if (args.length <= 1) {
            fileManager.getMsgList("help").forEach(sender::sendMessage);
            return Optional.empty();
        }

        final Set<Short> idList = messageManager.getAutoMsgMap().keySet();
        final Optional<Short> shortOptional = NumberUtil.isShort(args[1]);
        if (!shortOptional.isPresent()) {
            sender.sendMessage(fileManager.getMsg("errorNumber"));
            return Optional.empty();
        }

        final short id = shortOptional.get();
        if (!idList.contains(id)) {
            sender.sendMessage(fileManager.getMsg("messageNotExist"));
            return Optional.empty();
        }

        return Optional.of(id);
    }
}
