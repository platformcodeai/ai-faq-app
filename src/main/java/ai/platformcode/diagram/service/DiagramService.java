package ai.platformcode.diagram.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ai.platformcode.diagram.model.Diagram;
import ai.platformcode.diagram.repository.DiagramRepository;
import ai.platformcode.diagram.util.ServiceUtil;

@Service
public class DiagramService {
    
    @Autowired
    private DiagramRepository diagramRepository;
    
    @Autowired
    private ServiceUtil serviceUtil;

    /**
     * Retrieves all diagrams from the database.
     * @return List of diagrams
     */
    public Page<Diagram> getAllDiagrams(Pageable pageable) {
        return diagramRepository.findAll(pageable);
    }

    /**
     * Saves a new or existing diagram.
     * @param diagram Diagram entity to save
     * @return Saved diagram
     */
    public Diagram saveDiagram(Diagram diagram) {
    	diagram.setModificationDate(serviceUtil.getCurrentLocalDateTime());
        return diagramRepository.save(diagram);
    }

    /**
     * Retrieves a diagram by its ID.
     * @param id UUID of the diagram
     * @return Optional containing the found diagram, or empty if not found
     */
    public Optional<Diagram> getDiagramById(UUID id) {
        return diagramRepository.findById(id);
    }

    /**
     * Deletes a diagram by its ID.
     * @param id UUID of the diagram to delete
     */
    public void deleteDiagram(UUID id) {
        diagramRepository.deleteById(id);
    }
}
