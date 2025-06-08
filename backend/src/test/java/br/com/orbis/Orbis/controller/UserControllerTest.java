package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("User Tester");
        user.setEmail("tester@email.com");
        user.setPassword("strongPassword*123");
        user.setRole(Role.PARTICIPANTE);
    }

    @Test
    void testGetUserByIdFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "A resposta deve conter um usuário.");
        assertEquals("User Tester", response.getBody().getName());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllUsersEmptyList() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody()); // pois não há usuários
    }

    @Test
    void testGetAllUsersSuccess() {
        List<User> userList = Collections.singletonList(user);
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("User Tester", response.getBody().get(0).getName());
    }

    @Test
    void testGetAllUsersException() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Erro Interno"));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUserSuccess() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<HttpStatus> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUserNotFound() {
        when(userService.deleteUser(1L)).thenReturn(false);

        ResponseEntity<HttpStatus> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetUserByEmailFound() {
        when(userService.getUserByEmail("tester@email.com"))
                .thenReturn(user);

        ResponseEntity<User> response = userController.getUserByEmail("tester@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User Tester", response.getBody().getName());
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userService.getUserByEmail("tester@email.com"))
                .thenReturn(null);

        ResponseEntity<User> response = userController.getUserByEmail("tester@email.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateUserFound() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User Tester", response.getBody().getName());
    }

    @Test
    void testUpdateUserNotFound() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
