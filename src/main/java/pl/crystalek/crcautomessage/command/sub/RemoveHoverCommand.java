package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.util.ChatUtil;
import pl.crystalek.crcautomessage.util.NumberUtil;

import java.util.List;
import java.util.Optional;

public class RemoveHoverCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;
    private final CommandUtil commandUtil;

    public RemoveHoverCommand(final FileManager fileManager, final MessageManager messageManager, final CommandUtil commandUtil) {
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
        if (args.length != 3) {
            sender.sendMessage(fileManager.getMsg("removehover.usage"));
            return;
        }

        final Optional<Short> shortOptional = NumberUtil.isShort(args[2]);
        if (!shortOptional.isPresent()) {
            sender.sendMessage(fileManager.getMsg("errorNumber"));
            return;
        }

        final Message message = messageManager.getAutoMsgMap().get(messageId);
        final List<String> hover = message.getHover();
        if (hover == null) {
            sender.sendMessage(fileManager.getMsg("removehover.emptyHover"));
            return;
        }

        final short lineNumber = shortOptional.get();
        if (lineNumber <= 0 || lineNumber - 1 >= hover.size()) {
            sender.sendMessage(fileManager.getMsg("removehover.errorLineHover"));
            return;
        }

        hover.remove(lineNumber - 1);
        message.getTextComponent().setHoverEvent(ChatUtil.getHoverEvent(String.join("\n", hover)));
        fileManager.setMessage(messageId + ".hoverText", hover);
        sender.sendMessage(fileManager.getMsg("removehover.removeLineHover"));
    }
}
