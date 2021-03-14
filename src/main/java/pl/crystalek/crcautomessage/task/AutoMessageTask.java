package pl.crystalek.crcautomessage.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;

import java.util.List;
import java.util.stream.Collectors;

public final class AutoMessageTask {
    private final CrCAutoMessage crCAutoMessage;
    private final MessageManager messageManager;
    private int taskId;

    public AutoMessageTask(final CrCAutoMessage crCAutoMessage, final MessageManager messageManager) {
        this.crCAutoMessage = crCAutoMessage;
        this.messageManager = messageManager;
    }

    public void start() {
        final List<Message> enabledMessage = messageManager.getAutoMsgMap().values().stream().filter(Message::isStatus).collect(Collectors.toList());
        if (enabledMessage.isEmpty()) {
            return;
        }

        final BukkitTask bukkitTask = new BukkitRunnable() {
            private int index;

            public void run() {
                if (!messageManager.isAutoMsgStatus()) {
                    return;
                }

                Bukkit.broadcast(enabledMessage.get(index).getTextComponent());

                index++;
                if (index == enabledMessage.size()) {
                    index = 0;
                }
            }
        }.runTaskTimerAsynchronously(crCAutoMessage, 0L, messageManager.getAutoMsgTime() * 20L);
        this.taskId = bukkitTask.getTaskId();
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
