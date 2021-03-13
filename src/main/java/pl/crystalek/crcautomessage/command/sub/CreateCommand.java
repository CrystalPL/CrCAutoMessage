package pl.crystalek.crcautomessage.command.sub;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;

import java.util.Map;

public final class CreateCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;

    public CreateCommand(final FileManager fileManager, final MessageManager messageManager) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(fileManager.getMsg("create.usage"));
            return;
        }

        int number = 0;
        final Map<Short, Message> autoMsgMap = messageManager.getAutoMsgMap();
        for (int i = 1; i <= Short.MAX_VALUE; i++) {
            if (!autoMsgMap.containsKey(Short.parseShort(String.valueOf(i)))) {
                number = i;
                break;
            }
        }
        final String join = StringUtils.join(args, ' ', 1, args.length);
        messageManager.addMessage((short) number, new Message(join, false));
        fileManager.setMessage(number + ".text", join);
        fileManager.setMessage(number + ".status", false);
        sender.sendMessage(fileManager.getMsg("create.addMessage").replace("{ID}", String.valueOf(number)));
    }
}
