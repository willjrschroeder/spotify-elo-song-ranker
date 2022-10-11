package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import songranker.data.mappers.AppRoleJdbcRepo;
import songranker.data.mappers.AppUserJdbcRepo;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppRoleJdbcRepoTest {

    @Autowired
    AppRoleJdbcRepo repo;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){
        knownGoodState.set();
    }

    @Test
    void getRolesByUsername() {
    }
}