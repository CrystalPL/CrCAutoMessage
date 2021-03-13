package pl.crystalek.crcautomessage.command.sub;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AddHoverCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;
    private final CommandUtil commandUtil;

    public AddHoverCommand(final FileManager fileManager, final MessageManager messageManager, final CommandUtil commandUtil) {
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

        final short messageId = messageIdOptional.get();
        if (args.length <= 2) {
            sender.sendMessage(fileManager.getMsg("addhover.usage"));
            return;
        }

        final Message message = messageManager.getAutoMsgMap().get(messageId);
        final List<String> hover = message.getHover();
        if (hover == null) {
            message.setHover(new ArrayList<>());
        }

        final String join = StringUtils.join(args, ' ', 2, args.length);
        hover.add(join);
        message.getTextComponent().setHoverEvent(ChatUtil.getHoverEvent(String.join("\n", hover)));

        fileManager.setMessage(messageId + ".hoverText", hover);
        sender.sendMessage(fileManager.getMsg("addhover.addHover"));
    }
}
