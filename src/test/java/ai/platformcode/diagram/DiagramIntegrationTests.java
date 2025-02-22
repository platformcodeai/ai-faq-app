package ai.platformcode.diagram;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DiagramIntegrationTests {

    @LocalServerPort
    private int port;

    private static String createdDiagramId;  // Armazena o UUID do diagrama criado

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    void testCreateDiagram() {
        String requestBody = """
        {
            "md5": "d77982d217ec5a9bcbad5be9bee93027",
            "name": "Diagrama_Teste",
            "json_object": {
                "data": {
                    "key": "value"
                }
            }
        }
        """;

        createdDiagramId = 
            given()
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/diagrams")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("md5", equalTo("d77982d217ec5a9bcbad5be9bee93027"))
                .body("name", equalTo("Diagrama_Teste"))
                .extract()
                .path("id");  // Captura o UUID retornado

        System.out.println("Created Diagram ID: " + createdDiagramId);
    }

    @Test
    @Order(2)
    void testUpdateDiagram() {
        if (createdDiagramId == null) {
            throw new IllegalStateException("Nenhum diagrama criado para atualizar.");
        }

        String updatedBody = """
        {
            "md5": "updated-md5-value",
            "name": "Updated Diagram",
            "json_object": {
                "data": {
                    "key": "new_value"
                }
            }
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(updatedBody)
        .when()
            .put("/diagrams/" + createdDiagramId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("md5", equalTo("updated-md5-value"))
            .body("name", equalTo("Updated Diagram"));
    }

    @Test
    @Order(3)
    void testDeleteDiagram() {
        if (createdDiagramId == null) {
            throw new IllegalStateException("Nenhum diagrama criado para deletar.");
        }

        given()
        .when()
            .delete("/diagrams/" + createdDiagramId)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
