package br.com.orbis.Orbis.repository;

import br.com.orbis.Orbis.model.Order;
import br.com.orbis.Orbis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}