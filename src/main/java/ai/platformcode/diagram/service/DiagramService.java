package ai.platformcode.diagram.service;

import ai.platformcode.diagram.model.Diagram;
import ai.platformcode.diagram.repository.DiagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiagramService {
    @Autowired
    private DiagramRepository diagramRepository;

    public List<Diagram> getAllDiagrams() {
        return diagramRepository.findAll();
    }

    public Diagram saveDiagram(Diagram diagram) {
        return diagramRepository.save(diagram);
    }

    public Optional<Diagram> getDiagramById(UUID id) {
        return diagramRepository.findById(id);
    }

    public void deleteDiagram(UUID id) {
        diagramRepository.deleteById(id);
    }
}
