package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


// This class is a dependency of repository test classes, and it is used to reset the test DB to a known good state
@Component
public class KnownGoodState {
    @Autowired
    JdbcTemplate jdbcTemplate; // creates an obj to interact with the test database

    // This method can be called to reset the test database to its known good state. Executes through the SQL procedure
    // in sesr-schema-test.sql
    void set() {
        jdbcTemplate.update("call set_known_good_state();");
    }
}
