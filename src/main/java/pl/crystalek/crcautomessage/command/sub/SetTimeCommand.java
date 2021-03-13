package pl.crystalek.crcautomessage.command.sub;

import org.bukkit.command.CommandSender;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.util.NumberUtil;
import pl.crystalek.crcautomessage.util.TimeUtil;

import java.util.Optional;

public final class SetTimeCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;
    private final CrCAutoMessage crCAutoMessage;

    public SetTimeCommand(final FileManager fileManager, final MessageManager messageManager, final CrCAutoMessage crCAutoMessage) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
        this.crCAutoMessage = crCAutoMessage;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage(fileManager.getMsg("settime.usage"));
            return;
        }

        final Optional<Integer> integerOptional = NumberUtil.isInt(args[1]);
        if (!integerOptional.isPresent()) {
            sender.sendMessage(fileManager.getMsg("settime.errorNumberSetTime"));
            return;
        }

        final int time = integerOptional.get();
        crCAutoMessage.getConfig().set("config.autoMessageTime", time);
        crCAutoMessage.saveConfig();
        messageManager.setAutoMsgTime(time);
        sender.sendMessage(fileManager.getMsg("settime.setTime").replace("{TIME}", TimeUtil.getDateInString(time * 1000L, ", ", false)));
    }
}
