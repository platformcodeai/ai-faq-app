package ai.platformcode.diagram.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "diagrams")
public class Diagram {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String md5;
    @Column(name = "modification_date")
    private Long modificationDate;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "json_object_id", referencedColumnName = "id")
    private JsonObject jsonObject;
}
