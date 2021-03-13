package pl.crystalek.crcautomessage.command.sub;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.util.ChatUtil;

import java.util.List;
import java.util.Map;

public final class ListCommand implements SubCommand {
    private final FileManager fileManager;
    private final MessageManager messageManager;

    public ListCommand(final FileManager fileManager, final MessageManager messageManager) {
        this.fileManager = fileManager;
        this.messageManager = messageManager;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(fileManager.getMsg("list.usage"));
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(fileManager.getMsg("notconsole"));
            return;
        }

        for (final Map.Entry<Short, Message> entry : messageManager.getAutoMsgMap().entrySet()) {
            final Message message = entry.getValue();
            final List<String> messageHover = message.getHover();
            final List<String> msgList = fileManager.getMsgList("list.titleMessageHover");
            final StringBuilder hover = new StringBuilder();
            for (int i = 0; i < msgList.size(); i++) {
                final String configText = msgList.get(i);
                hover.append((configText + "&r").replace("{STATUS}", String.valueOf(message.isStatus())).replace("{ID}", String.valueOf(entry.getKey())));
                if (i != msgList.size() - 1) {
                    hover.append("\n");
                }
            }
            if (messageHover != null) {
                final String listMessageHover = fileManager.getMsg("list.listMessageHover");
                for (final String s : messageHover) {
                    hover.append("\n");
                    hover.append((listMessageHover + "&r").replace("{TEXT}", s));
                }
            }

            final TextComponent messageListHover = ChatUtil.getHoverMessage(fileManager.getMsg("list.list").replace("{TEXT}", message.getText()), hover.toString());
            final Player player = (Player) sender;
            player.spigot().sendMessage(messageListHover);
            return;
        }

        sender.sendMessage(fileManager.getMsg("list.empty"));
    }
}
