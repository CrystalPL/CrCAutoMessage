package pl.crystalek.crcautomessage.io;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.crystalek.crcautomessage.CrCAutoMessage;
import pl.crystalek.crcautomessage.message.Message;
import pl.crystalek.crcautomessage.message.MessageManager;
import pl.crystalek.crcautomessage.util.ChatUtil;
import pl.crystalek.crcautomessage.util.NumberUtil;

import java.util.*;
import java.util.logging.Logger;

public final class FileManager {
    private final CrCAutoMessage crCAutoMessage;
    private final MessageManager messageManager;
    private final Map<String, String> messages = new HashMap<>();
    private final Map<String, List<String>> messagesList = new HashMap<>();

    public FileManager(final CrCAutoMessage crCAutoMessage, final MessageManager messageManager) {
        this.crCAutoMessage = crCAutoMessage;
        this.messageManager = messageManager;
    }

    private void loadMsgConfig(final FileConfiguration fileConfiguration) {
        final Optional<Integer> integerOptional = NumberUtil.isInt(crCAutoMessage.getConfig().getString("config.autoMessageTime"));
        if (!integerOptional.isPresent()) {
            final Logger logger = crCAutoMessage.getLogger();
            logger.warning(ChatUtil.fixColor("&4&lWartość pola &cautomessageTime &4nie jest liczbą z zakresu <1, 2147483647>!"));
            logger.warning(ChatUtil.fixColor("&6&lWyłączanie serwera..."));
            Bukkit.shutdown();
            return;
        }
        messageManager.setAutoMsgTime(integerOptional.get());

        final ConfigurationSection messagesConfigurationSection = fileConfiguration.getConfigurationSection("config.messages");
        if (messagesConfigurationSection == null) {
            return;
        }

        for (final String messageId : messagesConfigurationSection.getKeys(false)) {
            final Optional<Short> shortOptional = NumberUtil.isShort(messageId);
            if (!shortOptional.isPresent()) {
                crCAutoMessage.getLogger().warning(ChatUtil.fixColor("&4&lWiadomosc o id &c" + messageId + " &4nie zostala wczytana, ponieważ id ma być liczbą z zakresu <1, 32767>!"));
                break;
            }

            final ConfigurationSection messageSection = messagesConfigurationSection.getConfigurationSection(messageId);

            final String status = messageSection.getString("status");
            if (!(status.equalsIgnoreCase("true") || status.equalsIgnoreCase("false"))) {
                crCAutoMessage.getLogger().warning(ChatUtil.fixColor("&4&lWiadomosc o id &c" + messageId + " &4nie zostala wczytana, ponieważ wartość pola &cstatus &4nie jest wartością &ctrue &4lub &cfalse&4!"));
                break;
            }

            final Message message = new Message(messageSection.getString("text"), Boolean.parseBoolean(status));
            if (messageSection.getString("hoverText") != null) {
                final List<String> hoverTextList = messageSection.getStringList("hoverText");
                message.setHover(hoverTextList);
                message.getTextComponent().setHoverEvent(ChatUtil.getHoverEvent(String.join("\n", hoverTextList)));
            }

            messageManager.addMessage(shortOptional.get(), message);
        }
    }

    public void setMessage(final String pathName, final Object value) {
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
                    keys.forEach(x -> addMessage(messagesConfiguration, message + "." + x));
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
