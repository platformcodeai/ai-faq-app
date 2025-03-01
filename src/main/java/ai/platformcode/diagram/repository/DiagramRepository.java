package ai.platformcode.diagram.repository;

import ai.platformcode.diagram.model.Diagram;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DiagramRepository extends JpaRepository<Diagram, UUID> {
    Page<Diagram> findAll(Pageable pageable);
}
