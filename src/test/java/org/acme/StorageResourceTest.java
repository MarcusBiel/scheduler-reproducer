package org.acme;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;

import org.apache.http.HttpStatus;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(StorageResource.class)
@TestProfile(StorageResourceTest.MyTestProfile.class)
class StorageResourceTest {

    @Test
    void listData() {
        given()
                .when()
                .get("/" + "images")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void uploadData() {

        given()
                .multiPart("file", getImage(), "application/octet-stream")
                .formParam("filename", "ANY_FILE_NAME")
                .formParam("mimeType", "image/png")

                .when()
                .post("/upload")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
    
    private File getImage() {
        return FileHelper.getFile("/image.png");
    }

    public static class MyTestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                    "quarkus.devservices.enabled", "true",
                    "quarkus.s3.devservices.buckets", "mybucket.com",
                    " s3.bucket.name", "mybucket.com"
            );
        }
    }
}
