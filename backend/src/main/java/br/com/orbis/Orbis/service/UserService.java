package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.exception.UserValidationException;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    @Transactional
    public User createUser(User user) {
        log.info("Criando usuário com e-mail: {}", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Tentativa de criar usuário com e-mail já existente: {}", user.getEmail());
            throw new UserValidationException("O e-mail '" + user.getEmail() + "' já está em uso.");
        } else if (!isValidPassword(user.getPassword())){
            log.warn("Senha inválida para o e-mail: {}", user.getEmail());
            throw new UserValidationException(
                    "Sua senha precisa conter pelo menos 8 caracteres, sendo pelo menos um deles maiúsculo, um número e um caracter especial.");
        }
        user.setPassword(encryptionService.encryptPassword(user.getPassword()));
        User saved = userRepository.save(user);
        log.info("Usuário criado com sucesso: {}", saved.getEmail());
        return saved;
    }

    public List<User> getAllUsers() {
        log.info("Buscando todos os usuários no banco de dados");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        log.info("Buscando usuário por ID: {}", id);
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        log.info("Atualizando usuário com ID: {}", id);
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            updatedUser.setPassword(encryptionService.encryptPassword(updatedUser.getPassword()));
            User saved = userRepository.save(updatedUser);
            log.info("Usuário com ID {} atualizado com sucesso.", id);
            return saved;
        } else {
            log.warn("Usuário com ID {} não encontrado para atualização.", id);
            return null;
        }
    }

    public boolean deleteUser(Long id) {
        log.info("Removendo usuário com ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("Usuário com ID {} removido com sucesso.", id);
            return true;
        } else {
            log.warn("Usuário com ID {} não encontrado para remoção.", id);
            return false;
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(regex);
    }
}
