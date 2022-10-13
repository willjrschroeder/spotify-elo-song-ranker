package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    private final String roleNameUser = "user";

    private final String roleNameAdmin = "admin";

    @BeforeEach
    void setup(){
        knownGoodState.set();
    }


    @Test
    void shouldGetAllRoles(){
        List<AppRole> toTest = repo.getAllRoles();
        assertNotNull(toTest);
        assertEquals(1, toTest.get(0).getAppRoleId());
        assertEquals(roleNameUser, toTest.get(0).getRoleName());
        assertEquals(2, toTest.get(1).getAppRoleId());
        assertEquals(roleNameAdmin, toTest.get(1).getRoleName());
    }
}