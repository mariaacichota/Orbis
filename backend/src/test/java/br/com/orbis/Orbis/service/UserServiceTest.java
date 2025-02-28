package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.exception.UserValidationException;
import br.com.orbis.Orbis.model.Role;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private UserService userService;

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
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(encryptionService.encryptPassword(user.getPassword())).thenReturn("senhaCriptografada");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("User Tester", createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        Exception exception = assertThrows(UserValidationException.class, () -> userService.createUser(user));

        assertEquals("O e-mail '" + user.getEmail() + "' já está em uso.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("User Tester", foundUser.get().getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testGetUserByEmail_Found() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User found = userService.getUserByEmail(user.getEmail());

        assertNotNull(found);
        assertEquals("User Tester", found.getName());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        User found = userService.getUserByEmail(user.getEmail());

        assertNull(found);
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllUsers_NonEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User Tester", result.get(0).getName());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(encryptionService.encryptPassword(anyString())).thenReturn("senhaCriptografada");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updated = userService.updateUser(1L, user);

        assertNotNull(updated);
        verify(userRepository).save(any(User.class));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(encryptionService).encryptPassword(captor.capture());
        assertEquals("strongPassword*123", captor.getValue());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        User updated = userService.updateUser(1L, user);

        assertNull(updated);
        verify(userRepository, never()).save(any(User.class));
    }
}
