package pl.crystalek.crcautomessage.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatUtil {

    public static String fixColor(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> fixColor(final List<String> list) {
        final List<String> lists = new ArrayList<>();
        for (final String string : list) {
            lists.add(fixColor(string));
        }
        return lists;
    }

    public static TextComponent getHoverMessage(final String text, final String hover) {
        final BaseComponent[] baseComponents = TextComponent.fromLegacyText(fixColor(text));
        final TextComponent message = new TextComponent(baseComponents);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(fixColor(hover)).create()));
        return message;
    }
}
