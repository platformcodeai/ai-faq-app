package ai.platformcode.diagram;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ai.platformcode.diagram.model.Diagram;
import ai.platformcode.diagram.repository.DiagramRepository;
import ai.platformcode.diagram.service.DiagramService;
import ai.platformcode.diagram.util.ServiceUtil;

@ExtendWith(MockitoExtension.class)
class DiagramCrudTests {

    @Mock
    private DiagramRepository diagramRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @InjectMocks
    private DiagramService diagramService;

    private Diagram sampleDiagram;

    @BeforeEach
    void setUp() {
        sampleDiagram = new Diagram("d77982d217ec5a9bcbad5be9bee93027", ZonedDateTime.now(), "Diagrama_Teste", new HashMap<>());
        sampleDiagram.setId(UUID.randomUUID());
        
        Mockito.lenient().when(serviceUtil.getCurrentLocalDateTime()).thenReturn(ZonedDateTime.now());
    }

    @Test
    void testCreateDiagram() {
        when(diagramRepository.save(any(Diagram.class))).thenReturn(sampleDiagram);
        Diagram created = diagramService.saveDiagram(sampleDiagram);
        assertNotNull(created);
        assertEquals(sampleDiagram.getMd5(), created.getMd5());
        verify(diagramRepository, times(1)).save(sampleDiagram);
    }

    @Test
    void testGetDiagramById() {
        when(diagramRepository.findById(sampleDiagram.getId())).thenReturn(Optional.of(sampleDiagram));
        Optional<Diagram> found = diagramService.getDiagramById(sampleDiagram.getId());
        assertTrue(found.isPresent());
        assertEquals(sampleDiagram.getId(), found.get().getId());
    }

    @Test
    void testGetDiagramById_NotFound() {
        when(diagramRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Optional<Diagram> found = diagramService.getDiagramById(UUID.randomUUID());
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateDiagram() {
        //when(diagramRepository.findById(sampleDiagram.getId())).thenReturn(Optional.of(sampleDiagram));
        when(diagramRepository.save(any(Diagram.class))).thenReturn(sampleDiagram);

        sampleDiagram.setName("Updated Name");
        Diagram updated = diagramService.saveDiagram(sampleDiagram);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        verify(diagramRepository, times(1)).save(sampleDiagram);
    }

    @Test
    void testDeleteDiagram() {
        doNothing().when(diagramRepository).deleteById(sampleDiagram.getId());
        diagramService.deleteDiagram(sampleDiagram.getId());
        verify(diagramRepository, times(1)).deleteById(sampleDiagram.getId());
    }
}
