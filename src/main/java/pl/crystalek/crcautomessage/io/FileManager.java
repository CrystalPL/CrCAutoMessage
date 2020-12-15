package pl.crystalek.crcautomessage.io;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.managers.MessageManager;
import pl.crystalek.crcautomessage.models.Message;
import pl.crystalek.crcautomessage.utils.ChatUtil;
import pl.crystalek.crcautomessage.utils.NumberUtil;

import java.util.*;

public class FileManager {
    private final CrCAutoMessage crCAutoMessage;
    private final MessageManager messageManager;
    private final Map<String, String> messages = new HashMap<>();
    private final Map<String, List<String>> messagesList = new HashMap<>();

    public FileManager(final CrCAutoMessage crCAutoMessage, final MessageManager messageManager) {
        this.crCAutoMessage = crCAutoMessage;
        this.messageManager = messageManager;
    }

    private void loadMsgConfig(final FileConfiguration fileConfiguration) {
        final ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("config.messages");
        if (configurationSection == null) {
            return;
        }
        for (final String key : configurationSection.getKeys(false)) {
            final ConfigurationSection messageSection = configurationSection.getConfigurationSection(key);
            final Message message = new Message(messageSection.getString("text"), messageSection.getBoolean("status"));
            if (messageSection.getString("hoverText") != null) {
                message.setHover(messageSection.getStringList("hoverText"));
            }
            if (!NumberUtil.isShort(key)) {
                System.err.println("Blad w odczytywaniu wiadomosci o id: " + key);
                return;
            }
            messageManager.addMessage(Short.parseShort(key), message);
        }
        final String timeInString = String.valueOf(crCAutoMessage.getConfig().get("config.autoMessageTime"));
        if (!NumberUtil.isInt(timeInString)) {
            System.out.println("WARTOSC POLA \"automessageTime\" NIE JEST LICZBA CALKOWITA");
        }
        messageManager.setAutoMsgTime(Integer.parseInt(timeInString));
    }

    public final void setMessage(final String pathName, final Object value) {
        final FileConfiguration fileConfiguration = crCAutoMessage.getConfig();

        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("config.messages");
        if (configurationSection == null) {
            configurationSection = fileConfiguration.createSection("config.messages");
        }
        configurationSection.set(pathName, value);
        crCAutoMessage.saveConfig();
    }

    private void loadMessage(final FileConfiguration fileConfiguration) {
        final ConfigurationSection messagesConfiguration = fileConfiguration.getConfigurationSection("messages");
        for (final String message : messagesConfiguration.getKeys(false)) {

            final ConfigurationSection configurationSection = messagesConfiguration.getConfigurationSection(message);
            if (configurationSection != null) {
                final Set<String> keys = configurationSection.getKeys(false);
                if (keys != null) {
                    configurationSection.getKeys(false).forEach(x -> addMessage(messagesConfiguration, message + "." + x));
                }
                continue;
            }
            addMessage(messagesConfiguration, message);
        }
    }

    private void addMessage(final ConfigurationSection configurationSection, final String path) {
        final Object objectMessage = configurationSection.get(path);
        if (objectMessage instanceof List) {
            this.messagesList.put(path, (ArrayList) objectMessage);
        } else {
            this.messages.put(path, ChatUtil.fixColor(configurationSection.getString(path)));
        }
    }

    public void load() {
        crCAutoMessage.saveDefaultConfig();
        final FileConfiguration config = crCAutoMessage.getConfig();
        loadMessage(config);
        loadMsgConfig(config);
    }

    public void reload() {
        messages.clear();
        messagesList.clear();
        crCAutoMessage.reloadConfig();
        load();
    }

    public String getMsg(final String pathMessage) {
        return messages.get(pathMessage);
    }

    public List<String> getMsgList(final String pathMessage) {
        return ChatUtil.fixColor(messagesList.get(pathMessage));
    }
}
