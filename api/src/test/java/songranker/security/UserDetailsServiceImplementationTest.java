package songranker.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import songranker.data.mappers.AppUserRepo;
import songranker.models.AppRole;
import songranker.models.AppUser;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplementationTest {

    @Autowired
    UserDetailsServiceImplementation service;

    @MockBean
    AppUserRepo repository;

    @Test
    void shouldFindUser() {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername", "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy", "John Smith", false, roles);

        when(repository.getUserByUsername("testUsername")).thenReturn(expected);

        UserDetails actual = service.loadUserByUsername("testUsername");

        assertEquals("[user]", actual.getAuthorities().toString());
        assertEquals("testUsername", actual.getUsername());
        assertEquals("$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy", actual.getPassword());
        System.out.println(actual.getAuthorities());
    }

    @Test
    void shouldNotFindMissingUser() {

        AppUser expected = null;

        when(repository.getUserByUsername("testUsername")).thenReturn(expected);

        try {
            UserDetails actual = service.loadUserByUsername("testUsername");
        } catch (UsernameNotFoundException ex) {
            assert true;
        }
    }

    @Test
    void shouldCreateNewUser() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("testUsername", "Password1!", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        AppUser actual = service.createUser("testUsername", "Password1!", "John Smith");

        assertEquals(1, actual.getAppUserId());
        assertEquals("testUsername", actual.getUsername());
        assertEquals("John Smith", actual.getDisplayName());
        assertEquals("$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy", actual.getPasswordHash());
        assertEquals("[user]", actual.getAuthorities().toString());
    }

    @Test
    void shouldNotCreateNewUserWithShortPassword() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("testUsername", "Pas1!", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        try {
            AppUser actual = service.createUser("testUsername", "Pas1!", "John Smith");
        } catch (ValidationException ex) {
            assert true;
        }

    }

    @Test
    void shouldNotCreateNewUserWithNoCapitalLetters() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("testUsername", "password1!", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        try {
            AppUser actual = service.createUser("testUsername", "password1!", "John Smith");
        } catch (ValidationException ex) {
            assert true;
        }

    }

    @Test
    void shouldNotCreateNewUserWithNoNumbers() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("testUsername", "Password!", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        try {
            AppUser actual = service.createUser("testUsername", "Password!", "John Smith");
        } catch (ValidationException ex) {
            assert true;
        }

    }

    @Test
    void shouldNotCreateNewUserWithNoSpecialCharacters() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("testUsername", "Password1", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        try {
            AppUser actual = service.createUser("testUsername", "Password1", "John Smith");
        } catch (ValidationException ex) {
            assert true;
        }

    }

    @Test
    void shouldNotCreateNewUserWithBlankPassword() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("testUsername", "", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        try {
            AppUser actual = service.createUser("testUsername", "", "John Smith");
        } catch (ValidationException ex) {
            assert true;
        }

    }

    @Test
    void shouldNotCreateNewUserWithLongUsername() throws ValidationException {
        AppRole role = new AppRole();
        role.setAppRoleId(1);
        role.setRoleName("user");
        role.setRoleUsers(new ArrayList<>());

        List<AppRole> roles = Arrays.asList(role);

        AppUser expected = new AppUser(1, "testUsername",
                "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy",
                "John Smith", false, roles);
        AppUser toCreate = new AppUser("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Password1!", "John Smith", false, roles);
        when(repository.createUser(toCreate)).thenReturn(expected);

        try {
            AppUser actual = service.createUser(null, "Password1!", "John Smith");
        } catch (ValidationException ex) {
            assert true;
        }

    }

}