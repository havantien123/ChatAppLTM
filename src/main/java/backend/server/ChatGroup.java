package backend.server;

import java.util.HashSet;
import java.util.Set;

public class ChatGroup {
    private final String groupName;
    private final Set<String> members; // Store usernames of group members

    public ChatGroup(String groupName) {
        this.groupName = groupName;
        this.members = new HashSet<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<String> getMembers() {
        return members;
    }

    public boolean addMember(String username) {
        return members.add(username); // Add user to the group
    }

    public boolean removeMember(String username) {
        return members.remove(username); // Remove user from the group
    }

    public boolean isMember(String username) {
        return members.contains(username); // Check if user is in the group
    }
}