import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.directory.SearchResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LdapInjectionTest {

    private static final String INJECT_SEARCH_INPUT = "*";
    private static final String INJECT_LOGIN_INPUT = "*)(uid=*))(|(uid=*";
    private static final String WRONG_PASSWORD = "12345678";


    private SimpleLdapService service;


    @BeforeEach
    void setUp() {
        service = new SimpleLdapService();
    }

    @AfterEach
    void destroy() {
        service.shutDownServer();
    }


    @Test
    void authenticateUserWithCorrectInputTest() {
        assertTrue(service.authenticateClient("bob", "bobspassword"));
    }

    @Test
    void authenticateUserWithWrongInputTest() {
        assertFalse(service.authenticateClient(INJECT_LOGIN_INPUT, WRONG_PASSWORD));
    }

    @Test
    void searchUserByUidWithCorrectInputTest() {
        String uid = "ben";
        List<SearchResult> result = service.searchByUid(uid);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("uid: " + uid, result.get(0).getAttributes().get("uid").toString());
    }

    @Test
    void searchUserByUidWithWrongInputTest() {
        List<SearchResult> result = service.searchByUid(INJECT_SEARCH_INPUT);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
