package ai.platformcode.diagram.repository;

import ai.platformcode.diagram.model.JsonObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JsonObjectRepository extends JpaRepository<JsonObject, Long> {}
