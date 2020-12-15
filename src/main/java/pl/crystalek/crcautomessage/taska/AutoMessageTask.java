package pl.crystalek.crcautomessage.taska;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.managers.MessageManager;
import pl.crystalek.crcautomessage.models.Message;
import pl.crystalek.crcautomessage.utils.ChatUtil;

import java.util.List;
import java.util.stream.Collectors;

public class AutoMessageTask {
    private final CrCAutoMessage crCAutoMessage;
    private final MessageManager messageManager;
    private int taskId;

    public AutoMessageTask(final CrCAutoMessage crCAutoMessage, final MessageManager messageManager) {
        this.crCAutoMessage = crCAutoMessage;
        this.messageManager = messageManager;
    }

    public void start() {
        Bukkit.getScheduler().cancelTask(taskId);
        final List<Message> enableMessage = messageManager.getAutoMsgMap().values().stream().filter(Message::isStatus).collect(Collectors.toList());
        if (enableMessage.isEmpty()) {
            return;
        }
        final BukkitTask bukkitTask = new BukkitRunnable() {
            private int i;

            public void run() {
                if (!messageManager.isAutoMsgStatus()) {
                    return;
                }
                final Message message = enableMessage.get(i);
                final List<String> messageHover = message.getHover();
                final TextComponent messageListHover = ChatUtil.getHoverMessage(message.getText(), messageHover != null ? String.join("\n", messageHover) : "");
                Bukkit.broadcast(messageListHover);
                i++;
                if (i == enableMessage.size()) {
                    i = 0;
                }
            }
        }.runTaskTimerAsynchronously(crCAutoMessage, 0L, messageManager.getAutoMsgTime() * 20L);
        this.taskId = bukkitTask.getTaskId();
    }
}
