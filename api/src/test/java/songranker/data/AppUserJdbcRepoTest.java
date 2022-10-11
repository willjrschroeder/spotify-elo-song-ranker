package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import songranker.data.mappers.AppUserJdbcRepo;
import songranker.models.AppUser;

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
        assertEquals();
    }
}