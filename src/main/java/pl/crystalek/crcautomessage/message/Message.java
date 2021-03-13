package pl.crystalek.crcautomessage.message;

import net.md_5.bungee.api.chat.TextComponent;
import pl.crystalek.crcautomessage.util.ChatUtil;

import java.util.List;

public class Message {
    private final TextComponent textComponent;
    private String text;
    private boolean status;
    private List<String> hover;

    public Message(final String text, final boolean status) {
        this.text = text;
        this.status = status;
        this.textComponent = ChatUtil.getMessageTextComponent(text);
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getHover() {
        return hover;
    }

    public void setHover(final List<String> hover) {
        this.hover = hover;
    }

    public TextComponent getTextComponent() {
        return textComponent;
    }
}
