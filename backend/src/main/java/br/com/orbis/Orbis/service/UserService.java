package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.exception.UserValidationException;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    @Transactional
    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserValidationException("O e-mail '" + user.getEmail() + "' já está em uso.");
        } else if (!isValidPassword(user.getPassword())){
            throw new UserValidationException(
                    "Sua senha precisa conter pelo menos 8 caracteres, sendo pelo menos um deles maiúsculo, um número e um caracter especial.");
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

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(regex);
    }
}
