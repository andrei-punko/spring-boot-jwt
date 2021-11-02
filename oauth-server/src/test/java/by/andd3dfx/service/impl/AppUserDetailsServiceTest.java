package by.andd3dfx.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import by.andd3dfx.domain.Role;
import by.andd3dfx.domain.User;
import by.andd3dfx.repository.UserRepository;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class AppUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppUserDetailsService service;

    @Test
    public void loadUserByUsername() {
        final String userName = "Bobby";
        User user = buildUser(userName);
        when(userRepository.findByUsername(userName)).thenReturn(user);

        UserDetails result = service.loadUserByUsername(userName);

        assertThat(result.getUsername(), is(user.getUsername()));
        assertThat(result.getPassword(), is(user.getPassword()));
        final Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertThat(authorities.size(), is(1));
        assertThat(authorities.iterator().next().getAuthority(), is(user.getRoles().get(0).getRoleName()));
    }

    @Test
    public void loadUserByUsernameForAbsentUser() {
        final String userName = "Bobby";
        when(userRepository.findByUsername(userName)).thenReturn(null);

        try {
            service.loadUserByUsername(userName);
            fail("Exception should be thrown!");
        } catch (UsernameNotFoundException ex) {
            assertThat(ex.getMessage(), is("The username=" + userName + " doesn't exist"));
        }
    }

    private User buildUser(String userName) {
        User user = new User();
        user.setId(123L);
        user.setUsername(userName);
        user.setPassword("secret-passwd");
        user.setRoles(Arrays.asList(buildRole()));
        return user;
    }

    private Role buildRole() {
        Role role = new Role();
        role.setRoleName("some-role-name");
        return role;
    }
}
