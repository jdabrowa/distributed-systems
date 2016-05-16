package pl.jdabrowa.distributed.jgroups.state;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ChannelState {

    @Getter private final String name;
    @Getter private final List<String> users;

    public ChannelState(String name) {
        this.name = name;
        this.users = new ArrayList<>();
    }

    public boolean hasUser(String userName) {
        return users.contains(userName);
    }

    public void addUser(String userName) {
        users.add(userName);
    }

    public void deleteUser(String userName) {
        users.remove(userName);
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}
