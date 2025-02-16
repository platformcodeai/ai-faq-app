package ai.platformcode.diagram.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "diagrams")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Diagram {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @JsonProperty("md5")
    private String md5;

    @Column(name = "modification_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime modificationDate;

    @Column(nullable = false)
    @JsonProperty("name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "json_object_id", referencedColumnName = "id", nullable = true)
    @JsonProperty("json_object")
    private JsonObject jsonObject;

    // Constructors
    public Diagram() {}

    public Diagram(String md5, ZonedDateTime modificationDate, String name, JsonObject jsonObject) {
        this.md5 = md5;
        this.modificationDate = modificationDate;
        this.name = name;
        this.jsonObject = jsonObject;
    }

    // Getters and Setters

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public ZonedDateTime getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
    	this.modificationDate = modificationDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }


}
