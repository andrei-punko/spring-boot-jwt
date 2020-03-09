package by.andd3dfx.service;

import by.andd3dfx.domain.User;
import java.util.List;

public interface GenericService {

    User findByUsername(String username);

    List<User> findAllUsers();
}
