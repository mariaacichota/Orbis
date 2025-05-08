package br.com.orbis.Orbis.repository;

import br.com.orbis.Orbis.model.Event;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizerId(Long organizerId);

    @Query("SELECT DISTINCT e FROM Event e " +
            "JOIN e.categories c " +
            "JOIN e.tags t " +
            "WHERE (:category IS NULL OR c.name = :category) " +
            "AND (:tag IS NULL OR t.name = :tag)")
    List<Event> findByCategoryAndTag(@Param("category") String category,
                                     @Param("tag") String tag);
}
