// 
// Decompiled by Procyon v0.6.0
// 

package de.paxii.clarinet.event.events.client;

import de.paxii.clarinet.event.events.Event;

public class AddFriendEvent implements Event
{
    private String friendName;
    
    public AddFriendEvent(final String friendName) {
        this.friendName = friendName;
    }
    
    public void setFriendName(final String friendName) {
        this.friendName = friendName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddFriendEvent)) {
            return false;
        }
        final AddFriendEvent other = (AddFriendEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$friendName = this.getFriendName();
        final Object other$friendName = other.getFriendName();
        if (this$friendName == null) {
            if (other$friendName == null) {
                return true;
            }
        }
        else if (this$friendName.equals(other$friendName)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AddFriendEvent;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $friendName = this.getFriendName();
        result = result * 59 + (($friendName == null) ? 43 : $friendName.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "AddFriendEvent(friendName=" + this.getFriendName() + ")";
    }
    
    public String getFriendName() {
        return this.friendName;
    }
}