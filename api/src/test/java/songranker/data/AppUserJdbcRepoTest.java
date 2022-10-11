package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import songranker.data.mappers.AppUserJdbcRepo;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserJdbcRepoTest {

    @Autowired
    AppUserJdbcRepo repo;

    @Autowired
    KnownGoodState knownGoodState;

    private final String username1 = "testUsername";

    @BeforeEach
    void setup(){
        knownGoodState.set();
    }

    @Test
    void shouldFindUserByUsername() {
        AppUser toTest = repo.getUserByUsername(username1);
        assertNotNull(toTest);
        assertEquals(1, toTest.getAppUserId());
        assertEquals("testUsername", toTest.getUsername());
        assertEquals("John Smith", toTest.getDisplayName());
        assertEquals("user", toTest.getRoles().get(0).getRoleName());
    }

    @Test
    void shouldAddValidUser(){
       AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = List.of(role);


        AppUser toAdd = new AppUser(0, "newTestUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy", "Test Create", false, roles);


        AppUser toTest = repo.add(toAdd);

        assertNotNull(toTest);

   }
}