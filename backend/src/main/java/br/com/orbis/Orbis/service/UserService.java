package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.exception.UserValidationException;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Transactional
    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserValidationException("O e-mail '" + user.getEmail() + "' já está em uso.");
        }

        user.setPassword(encryptionService.encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            updatedUser.setPassword(encryptionService.encryptPassword(updatedUser.getPassword()));
            return userRepository.save(updatedUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
