package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import songranker.data.mappers.AppRoleJdbcRepo;
import songranker.data.mappers.AppUserJdbcRepo;
import songranker.models.AppRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppRoleJdbcRepoTest {

    @Autowired
    AppRoleJdbcRepo repo;

    @Autowired
    KnownGoodState knownGoodState;

    private final String username1 = "testUsername";

    @BeforeEach
    void setup(){
        knownGoodState.set();
    }

    @Test
    void getRolesByUsername() {
        List<AppRole> toTest = repo.getRolesByUsername(username1);
        assertNotNull(toTest);
        assertEquals(1, toTest.get(0).getAppRoleId());
        assertEquals("user", toTest.get(0).getRoleName());
        assertEquals(2, toTest.get(0).getRoleUsers());
    }
}