import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.*;

public class BaseTest {
    //В данном родительском классе содержится базовая спецификация и спецификации к методам тестовых классов
    private final String BASE_URI = "https://petstore.swagger.io/v2";
    RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json;;charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build();
    protected ResponseSpecification getAssertionSpecification() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        return builder.build();
    }
    protected ResponseSpecification getCreatePetResponse(int id, String categoryName, String name){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("id", equalTo(id))
                .expectBody("category.name", equalTo(categoryName))
                .expectBody("name", equalTo(name));
        return builder.build();
    }

    protected ResponseSpecification getAddPhotoResponse(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
        .expectBody("message", containsString("petPhoto.jpeg"));
        return builder.build();
    }

    protected ResponseSpecification getPetResponse(int id, String categoryName, String name) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("id", equalTo(id))
                .expectBody("category.name", equalTo(categoryName))
                .expectBody("name", equalTo(name));
        return builder.build();
    }

    protected ResponseSpecification getUpdatedPetResponse(int id, String categoryName, String name){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("id", equalTo(id))
                .expectBody("category.name", equalTo(categoryName))
                .expectBody("name", equalTo(name));
        return builder.build();
    }

    protected ResponseSpecification getUpdateWithFormResponse(int code){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("code", equalTo(code));
        return builder.build();
    }

    protected ResponseSpecification getUpdatedPetFormDataResponse(int id, String status, String name){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("id", equalTo(id))
                .expectBody("status", equalTo(status))
                .expectBody("name", equalTo(name));
        return builder.build();
    }

    protected ResponseSpecification getOrderResponse(int id, int petId, int qantity, String shipDate, String status, boolean complete){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("id", equalTo(id))
                .expectBody("petId", equalTo(petId))
                .expectBody("quantity", equalTo(qantity))
                .expectBody("shipDate", equalTo(shipDate))
                .expectBody("status", equalTo(status))
                .expectBody("complete", equalTo(complete));
        return builder.build();
    }

    protected ResponseSpecification getOrderAfterCreationResponse(int code){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("code", equalTo(code));
        return builder.build();
    }

    protected  ResponseSpecification getUsersResponse(int id, String userName, String firstName, String lastName, String email, String password, int userStatus){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("id", equalTo(id))
                .expectBody("username", equalTo(userName))
                .expectBody("firstName", equalTo(firstName))
                .expectBody("lastName", equalTo(lastName))
                .expectBody("email", equalTo(email))
                .expectBody("password", equalTo(password))
                .expectBody("userStatus", equalTo(userStatus));
        return builder.build();
    }

}
