package by.andd3dfx.repository;

import by.andd3dfx.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    private User entity1 = buildUser("strange login");
    private User entity2 = buildUser("strange login 2");
    private User entity3 = buildUser("strange login 3");

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.save(entity1);
        userRepository.save(entity2);
        userRepository.save(entity3);
    }

    @Test
    public void findByUsername() {
        var result = userRepository.findByUsername(entity2.getUsername());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getUsername(), is(entity2.getUsername()));
        assertThat(result.getPassword(), is(entity2.getPassword()));
        assertThat(result.getFirstName(), is(entity2.getFirstName()));
        assertThat(result.getLastName(), is(entity2.getLastName()));
    }

    private User buildUser(String username) {
        User entity = new User();
        entity.setUsername(username);
        entity.setPassword(username + "-passwd");
        entity.setFirstName(username + "-first-name");
        entity.setLastName(username + "-last-name");
        return entity;
    }
}
