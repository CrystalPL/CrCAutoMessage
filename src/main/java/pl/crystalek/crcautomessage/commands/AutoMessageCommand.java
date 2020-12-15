package pl.crystalek.crcautomessage.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.managers.MessageManager;
import pl.crystalek.crcautomessage.models.Message;
import pl.crystalek.crcautomessage.taska.AutoMessageTask;
import pl.crystalek.crcautomessage.utils.ChatUtil;
import pl.crystalek.crcautomessage.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AutoMessageCommand implements TabExecutor {
    private final MessageManager messageManager;
    private final FileManager fileManager;
    private final CrCAutoMessage crCAutoMessage;
    private final AutoMessageTask autoMessageTask;

    public AutoMessageCommand(final MessageManager messageManager, final FileManager fileManager, final CrCAutoMessage crCAutoMessage, final AutoMessageTask autoMessageTask) {
        this.messageManager = messageManager;
        this.fileManager = fileManager;
        this.crCAutoMessage = crCAutoMessage;
        this.autoMessageTask = autoMessageTask;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("crc.automessage")) {
            sender.sendMessage(fileManager.getMsg("nopermission"));
            return true;
        }
        if (args.length == 0 || !(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("print") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("removehover") || args[0].equalsIgnoreCase("addhover") || args[0].equalsIgnoreCase("settext") || args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("settime") || args[0].equalsIgnoreCase("reload"))) {
            fileManager.getMsgList("help").forEach(sender::sendMessage);
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            if (args.length != 1) {
                sender.sendMessage(fileManager.getMsg("list.usage"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(fileManager.getMsg("notconsole"));
                return true;
            }
            final Map<Short, Message> autoMsgMap = messageManager.getAutoMsgMap();
            for (final Short id : autoMsgMap.keySet()) {
                final Message message = autoMsgMap.get(id);
                final List<String> messageHover = message.getHover();
                final List<String> msgList = fileManager.getMsgList("list.titleMessageHover");
                final StringBuilder hover = new StringBuilder();
                for (int i = 0; i < msgList.size(); i++) {
                    final String s = msgList.get(i);
                    hover.append((s + "&r").replace("{STATUS}", String.valueOf(message.isStatus())).replace("{ID}", String.valueOf(id)));
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
            }
            return true;
        } else if (args[0].equalsIgnoreCase("print")) {
            if (args.length != 1) {
                sender.sendMessage(fileManager.getMsg("print.usage"));
                return true;
            }
            final boolean status = !messageManager.isAutoMsgStatus();
            messageManager.setAutoMsgStatus(status);
            sender.sendMessage(fileManager.getMsg("print.messageStatus").replace("{STATUS}", status ? "wlaczone" : "wylaczone"));
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length <= 1) {
                sender.sendMessage(fileManager.getMsg("create.usage"));
                return true;
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
            return true;
        } else if (args[0].equalsIgnoreCase("settime")) {
            if (args.length != 2) {
                sender.sendMessage(fileManager.getMsg("settime.usage"));
                return true;
            }
            if (!NumberUtil.isInt(args[1])) {
                sender.sendMessage(fileManager.getMsg("settime.errorNumberSetTime"));
                return true;
            }
            final int time = Integer.parseInt(args[1]);
            crCAutoMessage.getConfig().set("config.autoMessageTime", time);
            crCAutoMessage.saveConfig();
            messageManager.setAutoMsgTime(time);
            sender.sendMessage(fileManager.getMsg("settime.setTime").replace("{TIME}", args[1]));
            return true;
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (args.length != 1) {
                sender.sendMessage(fileManager.getMsg("reload.usage"));
                return true;
            }
            fileManager.reload();
            autoMessageTask.start();
            sender.sendMessage(fileManager.getMsg("reload.reload"));
            return true;
        }
        if (args.length <= 1) {
            fileManager.getMsgList("help").forEach(sender::sendMessage);
            return true;
        }
        final Map<Short, Message> autoMsgMap = messageManager.getAutoMsgMap();
        final Set<Short> idList = autoMsgMap.keySet();
        if (!NumberUtil.isShort(args[1])) {
            sender.sendMessage(fileManager.getMsg("errorNumber"));
            return true;
        }
        final short id = Short.parseShort(args[1]);
        if (!idList.contains(id)) {
            sender.sendMessage(fileManager.getMsg("messageNotExist"));
            System.out.println(2);
            return true;
        }
        final Message message = autoMsgMap.get(id);

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length != 2) {
                sender.sendMessage(fileManager.getMsg("delete.usage"));
                return true;
            }
            fileManager.setMessage(args[1], null);
            messageManager.removeMessage(id);
            sender.sendMessage(fileManager.getMsg("delete.deleteMessage").replace("{ID}", args[1]));
        } else if (args[0].equalsIgnoreCase("removehover")) {
            if (args.length != 3) {
                sender.sendMessage(fileManager.getMsg("removehover.usage"));
                return true;
            }
            if (!NumberUtil.isShort(args[2])) {
                sender.sendMessage(fileManager.getMsg("errorNumber"));
                return true;
            }
            final List<String> hover = message.getHover();
            if (hover == null) {
                sender.sendMessage(fileManager.getMsg("removehover.emptyHover"));
                return true;
            }
            final short lineNumber = Short.parseShort(args[2]);
            if (lineNumber <= 0 || lineNumber - 1 >= hover.size()) {
                sender.sendMessage(fileManager.getMsg("removehover.errorLineHover"));
                return true;
            }
            hover.remove(lineNumber - 1);
            fileManager.setMessage(id + ".hoverText", hover);
            sender.sendMessage(fileManager.getMsg("removehover.removeLineHover"));
        } else if (args[0].equalsIgnoreCase("addhover")) {
            if (args.length <= 2) {
                sender.sendMessage(fileManager.getMsg("addhover.usage"));
                return true;
            }
            final List<String> hover = message.getHover();
            if (hover == null) {
                message.setHover(new ArrayList<>());
            }
            final String join = StringUtils.join(args, ' ', 2, args.length);
            hover.add(join);
            fileManager.setMessage(id + ".hoverText", hover);
            sender.sendMessage(fileManager.getMsg("addhover.addHover"));
        } else if (args[0].equalsIgnoreCase("settext")) {
            if (args.length <= 2) {
                sender.sendMessage(fileManager.getMsg("settext.usage"));
                return true;
            }
            final String join = StringUtils.join(args, ' ', 2, args.length);
            message.setText(join);
            fileManager.setMessage(id + ".text", join);
            sender.sendMessage(fileManager.getMsg("settext.setText"));
        } else if (args[0].equalsIgnoreCase("status")) {
            if (args.length != 2) {
                sender.sendMessage(fileManager.getMsg("status.usage"));
                return true;
            }
            final boolean status = !message.isStatus();
            fileManager.setMessage(args[1] + ".status", status);
            message.setStatus(status);
            sender.sendMessage(fileManager.getMsg("status.changeStatus").replace("{ID}", args[1]).replace("{STATUS}", status ? "wlaczone" : "wylaczone"));
        } else {
            fileManager.getMsgList("help").forEach(sender::sendMessage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final List<String> tabList = new ArrayList<>();
        if (args.length == 1) {
            tabList.add("list");
            tabList.add("print");
            tabList.add("create");
            tabList.add("delete");
            tabList.add("removehover");
            tabList.add("addhover");
            tabList.add("settext");
            tabList.add("status");
            tabList.add("settime");
            tabList.add("reload");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("removehover") || args[0].equalsIgnoreCase("addhover") || args[0].equalsIgnoreCase("settext") || args[0].equalsIgnoreCase("status")) {
                tabList.addAll(messageManager.getAutoMsgMap().keySet().stream().map(String::valueOf).collect(Collectors.toList()));
            }
        }
        return tabList;
    }
}
