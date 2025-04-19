package backend.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<String> users;

    public Group(String name) {
        this.name = name;
        this.users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getUsers() {
        return users;
    }

    public void addUser(String user) {
        if (!user.isEmpty() && !users.contains(user)) {
            users.add(user);
        }
    }
}