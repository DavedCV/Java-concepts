package com.davidcv.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoService {

    // JPA/Hibernate -> Database (in the future)

    private static List<User> userList = new ArrayList<>();
    private static long id = 0;

    static {
        userList.add(new User(++id, "david", LocalDate.of(2002, 6, 4)));
        userList.add(new User(++id, "sofia", LocalDate.of(2002, 10, 31)));
        userList.add(new User(++id, "mary", LocalDate.of(1976, 10, 6)));
        userList.add(new User(++id, "testUser", LocalDate.now().minusYears(30)));
    }

    public List<User> findAll() {
        return userList;
    }

    public User findUserById(long id) {
        return userList.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public User saveUser(User user) {
        user.setId(++id);
        userList.add(user);

        return user;
    }
}
