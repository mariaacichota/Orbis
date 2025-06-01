package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("[GET] /users - Buscando todos os usuários");
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                log.info("Nenhum usuário encontrado.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            log.info("{} usuários encontrados.", users.size());
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao buscar usuários", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("[GET] /users/{} - Buscando usuário por ID", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            log.info("Usuário encontrado: {}", user.get().getEmail());
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            log.info("Usuário com ID {} não encontrado.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("[PUT] /users/{} - Atualizando usuário", id);
        User user = userService.updateUser(id, updatedUser);
        if (user == null) {
            log.info("Usuário com ID {} não encontrado para atualização.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Usuário com ID {} atualizado com sucesso.", id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        log.info("[DELETE] /users/{} - Removendo usuário", id);
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            log.info("Usuário com ID {} removido com sucesso.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Usuário com ID {} não encontrado para remoção.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.info("[GET] /users/email/{} - Buscando usuário por e-mail", email);
        User user = userService.getUserByEmail(email);
        if (user == null) {
            log.info("Usuário com e-mail {} não encontrado.", email);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Usuário encontrado: {}", user.getEmail());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
