package pl.crystalek.crcautomessage.command.sub;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;

import java.util.Optional;

public final class SetTextCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;
    private final CommandUtil commandUtil;

    public SetTextCommand(final FileManager fileManager, final MessageManager messageManager, final CommandUtil commandUtil) {
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
        if (args.length <= 2) {
            sender.sendMessage(fileManager.getMsg("settext.usage"));
            return;
        }

        final String join = StringUtils.join(args, ' ', 2, args.length);
        final Message message = messageManager.getAutoMsgMap().get(messageId);
        message.setText(join);
        message.getTextComponent().setText(join);
        fileManager.setMessage(messageId + ".text", join);
        sender.sendMessage(fileManager.getMsg("settext.setText"));
    }
}
