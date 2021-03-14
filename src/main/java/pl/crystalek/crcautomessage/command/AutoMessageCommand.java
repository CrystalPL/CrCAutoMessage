package pl.crystalek.crcautomessage.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.command.sub.*;
import pl.crystalek.crcautomessage.io.FileManager;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.task.AutoMessageTask;
import pl.crystalek.crcautomessage.util.NumberUtil;

import java.util.*;
import java.util.stream.Collectors;

public final class AutoMessageCommand implements TabExecutor {
    private final MessageManager messageManager;
    private final FileManager fileManager;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public AutoMessageCommand(final MessageManager messageManager, final FileManager fileManager, final CrCAutoMessage crCAutoMessage, final AutoMessageTask autoMessageTask) {
        this.messageManager = messageManager;
        this.fileManager = fileManager;

        final ReloadCommand reloadCommand = new ReloadCommand(fileManager, autoMessageTask);
        final CommandUtil commandUtil = new CommandUtil(fileManager, messageManager);

        subCommands.put("list", new ListCommand(fileManager, messageManager));
        subCommands.put("print", new PrintCommand(fileManager, messageManager));
        subCommands.put("create", new CreateCommand(fileManager, messageManager));
        subCommands.put("settime", new SetTimeCommand(fileManager, messageManager, crCAutoMessage));
        subCommands.put("reload", reloadCommand);
        subCommands.put("rl", reloadCommand);
        subCommands.put("delete", new DeleteCommand(fileManager, messageManager, commandUtil));
        subCommands.put("removehover", new RemoveHoverCommand(fileManager, messageManager, commandUtil));
        subCommands.put("addhover", new AddHoverCommand(fileManager, messageManager, commandUtil));
        subCommands.put("settext", new SetTextCommand(fileManager, messageManager, commandUtil));
        subCommands.put("status", new StatusCommand(fileManager, messageManager, commandUtil));
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("crc.automessage")) {
            sender.sendMessage(fileManager.getMsg("nopermission"));
            return true;
        }

        if (args.length != 0) {
            final SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                subCommand.execute(sender, args);
                return true;
            }
        }

        fileManager.getMsgList("help").forEach(sender::sendMessage);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        List<String> tabList = new ArrayList<>();

        if (args.length == 1) {
            tabList = new ArrayList<>(subCommands.keySet());
            tabList.removeIf(subCommand -> !subCommand.startsWith(args[0].toLowerCase()));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("removehover") || args[0].equalsIgnoreCase("addhover") || args[0].equalsIgnoreCase("settext") || args[0].equalsIgnoreCase("status")) {
                tabList.addAll(messageManager.getAutoMsgMap().keySet().stream().map(String::valueOf).collect(Collectors.toList()));
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("removehover")) {
                final Optional<Short> shortOptional = NumberUtil.isShort(args[1]);
                if (shortOptional.isPresent()) {
                    final short id = shortOptional.get();
                    final Map<Short, Message> autoMsgMap = messageManager.getAutoMsgMap();

                    if (autoMsgMap.containsKey(id)) {
                        final Message message = autoMsgMap.get(id);
                        final List<String> hover = message.getHover();

                        if (hover != null) {
                            for (int i = 0; i < hover.size(); i++) {
                                tabList.add(String.valueOf(i + 1));
                            }
                        }
                    }
                }
            }
        }

        return tabList;
    }
}
