package ai.platformcode.diagram.dto;

import java.time.ZonedDateTime;
import java.util.UUID;
import ai.platformcode.diagram.model.Diagram;
import org.springframework.data.domain.Page;

public record DiagramDTO(UUID id, String name, String md5, ZonedDateTime modificationDate) {
    public DiagramDTO(Diagram diagram) {
        this(diagram.getId(),
         diagram.getName(),
         diagram.getMd5(),
         diagram.getModificationDate());
    }

    public static Page<DiagramDTO> fromPage(Page<Diagram> diagrams) {
        return diagrams.map(DiagramDTO::new);
    }
}
