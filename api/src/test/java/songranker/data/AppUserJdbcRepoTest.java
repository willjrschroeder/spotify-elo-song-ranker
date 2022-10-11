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
//        List<AppRole> testRoles = new ArrayList<>();
//
//        testRoles.add()
//        AppUser toAdd = new AppUser(0, "newTestUsername",
//                "fd5cb51bafd60f6fdbedde6e62c473da6f247db271633e15919bab78a02ee9eb", "Test Create", true, );
//        toAdd.setUsername("newTestUsername");
//        toAdd.setPasswordHash("fd5cb51bafd60f6fdbedde6e62c473da6f247db271633e15919bab78a02ee9eb");
//        toAdd.setDisplayName("Test Create");
//        toAdd.setDisabled(true);
   }
}