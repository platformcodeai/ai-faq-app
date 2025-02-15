package ai.platformcode.diagram.model;

import jakarta.persistence.*;

@Entity
@Table(name = "json_objects")
public class JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "jsonb")
    private String data;
}
