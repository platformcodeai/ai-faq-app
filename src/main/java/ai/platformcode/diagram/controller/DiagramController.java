package ai.platformcode.diagram.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.platformcode.diagram.model.Diagram;
import ai.platformcode.diagram.service.DiagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Diagram API", description = "API For manage diagrams")
@RestController
@RequestMapping("/diagrams")
public class DiagramController {
    @Autowired
    private DiagramService diagramService;
    
    @Operation(summary = "List all diagrams",  responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of diagrams",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = Diagram[].class)))
    })
    @GetMapping
    public List<Diagram> getAllDiagrams() {
        return diagramService.getAllDiagrams();
    }
    
    @Operation(summary = "Creates a new diagram", responses = {
        @ApiResponse(responseCode = "201", description = "Diagram successfully created",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = Diagram.class)))
    })
    @PostMapping
    public Diagram createDiagram(@RequestBody Diagram diagram) {
        diagram.setModificationDate(null);
        return diagramService.saveDiagram(diagram);
    }
    
    @Operation(summary = "Retrieves a diagram by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Diagram found",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = Diagram.class))),
        @ApiResponse(responseCode = "404", description = "Diagram not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Diagram> getDiagramById(@PathVariable UUID id) {
        Optional<Diagram> diagram = diagramService.getDiagramById(id);
        return diagram.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Updates an existing diagram", responses = {
        @ApiResponse(responseCode = "200", description = "Diagram successfully updated",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = Diagram.class))),
        @ApiResponse(responseCode = "404", description = "Diagram not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Diagram> updateDiagram(@PathVariable UUID id, @RequestBody Diagram updatedDiagram) {
        Optional<Diagram> existingDiagram = diagramService.getDiagramById(id);
        if (existingDiagram.isPresent()) {
            Diagram diagram = existingDiagram.get();
            diagram.setMd5(updatedDiagram.getMd5());
            diagram.setName(updatedDiagram.getName());
            diagram.setJsonObject(updatedDiagram.getJsonObject());
            return ResponseEntity.ok(diagramService.saveDiagram(diagram));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletes a diagram by ID", responses = {
        @ApiResponse(responseCode = "204", description = "Diagram successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Diagram not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagram(@PathVariable UUID id) {
        diagramService.deleteDiagram(id);
        return ResponseEntity.noContent().build();
    }
}
