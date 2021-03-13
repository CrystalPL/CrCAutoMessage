package pl.crystalek.crcautomessage.message;

import java.util.HashMap;
import java.util.Map;

public final class MessageManager {
    private final Map<Short, Message> autoMsgMap = new HashMap<>();
    private boolean autoMsgStatus = true;
    private int autoMsgTime;

    public void addMessage(final short id, final Message message) {
        autoMsgMap.put(id, message);
    }

    public void removeMessage(final short id) {
        autoMsgMap.remove(id);
    }

    public Map<Short, Message> getAutoMsgMap() {
        return autoMsgMap;
    }

    public boolean isAutoMsgStatus() {
        return autoMsgStatus;
    }

    public void setAutoMsgStatus(final boolean autoMsgStatus) {
        this.autoMsgStatus = autoMsgStatus;
    }

    public int getAutoMsgTime() {
        return autoMsgTime;
    }

    public void setAutoMsgTime(final int autoMsgTime) {
        this.autoMsgTime = autoMsgTime;
    }
}
