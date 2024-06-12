import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runners.MethodSorters;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static groovyjarjarantlr4.v4.runtime.misc.Utils.readFile;
import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PetsTests extends BaseTest {
    //В данном тестовом классе содержатся тестовые методы для раздела Pet
    private final String BASE_URI = "https://petstore.swagger.io/v2";
    private final int SUCCESS_CODE = 200;

    String requestBody = null;
    Integer petId = 15;
    String petName = "Maxik";
    String newPetName = "Maximilian";
    String newPetName2 = "Max";
    String petCategory = "cat";
    String petsStatus = "sold";
    File imageFile = new File("src/test/resources/petPhoto.jpeg");


    // Задаем информацию о новом питомце
    {
        String initialBody = null;
        try {
            initialBody = new String(readFile("src/test/resources/petInformation.json"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("id", petId);
        jsonObject.put("name", petName);
        JSONObject categoryObject = jsonObject.getJSONObject("category");
        categoryObject.put("name", petCategory);
        requestBody = jsonObject.toString();
    }

    @Test
    @DisplayName("Добавление нового питомца")
    public void testAddNewPetToStore() throws IOException {
        given(specification)
                .basePath("pet")
                .body(requestBody)
                .post()
                .then().log().all()
                .spec(getCreatePetResponse(petId, petCategory, petName));

        System.out.println("Новый питомец " + petName + " успешно добавлен в магазин!" + "\n");
    }

    @Test
    @DisplayName("Добавление фото для питомца")
    public void testAddPetPhoto(){
        given()
                .baseUri(BASE_URI)
                .basePath("/pet/" + petId + "/uploadImage")
                .multiPart(imageFile)
                .post()
                .then().log().all()
                .spec(getAddPhotoResponse());

        System.out.println("Фотография питомца " + petName + " успешно загружена!" + "\n");
    }
    @Test
    @DisplayName("Поиск питомца по ID")
    public void testSearchPetById() {
        given(specification)
                .basePath("/pet/" + petId)
                .get()
                .then().log().all()
                .spec(getPetResponse(petId, petCategory, petName));
        System.out.println("Питомец " + petName + " успешно найден по ID!" + "\n");

    }

    @Test
    @DisplayName("Поиск питомцев по статусу")
    public void searchByStatus() {
        Response response = given(specification)
                .basePath("/pet/findByStatus")
                .queryParam("status", petsStatus)
                .get();

        Assertions.assertEquals(SUCCESS_CODE, response.getStatusCode());

        System.out.println("Питомцы успешно найдены по статусу " + petsStatus);
        List<String> soldPetNames = response.jsonPath().getList("name");

        System.out.println("Список имен проданных питомцев:");
        for (String petName : soldPetNames) {
            System.out.println("- " + petName);
        }
        System.out.println("\n");
    }

    @Test
    @DisplayName("Изменение информации о питомце (name)")
    public void changePetID() throws IOException{
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", newPetName);

        given(specification)
        .basePath("pet")
        .body(jsonObject.toString())
        .put()
        .then().log().all()
        .spec(getUpdatedPetResponse(petId, petCategory, newPetName));
        System.out.println("У питомца " + petName + " успешно обновлено имя: " + newPetName);

        //поиск по ID после изменения информации о питомце
        given(specification)
                .basePath("/pet/" + petId)
                .get()
                .then().log().all()
                .spec(getPetResponse(petId, petCategory, newPetName));
        System.out.println("Питомец успешно найден по ID после обновления!" + "\n");

    }

    @Test
    @DisplayName("Изменение информации о питомце с помощью FORM DATA")
    public void testUpdatePetWithFormData() {
        given(specification)
                .basePath("/pet/" + petId)
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", newPetName2)
                .formParam("status", "pending")
                .post()
                .then().log().all()
                .spec(getUpdateWithFormResponse(SUCCESS_CODE));
        System.out.println("Информация о питомце успешно обновлена с помощью FORM DATA!");

        //поиск питомца по ID после обновления с помощью FORM DATA
        given(specification)
                .basePath("/pet/" + petId)
                .get()
                .then().log().all()
                .spec(getUpdatedPetFormDataResponse(petId, "pending", newPetName2));
        System.out.println("Питомец успешно найден по ID после обновления с помощью FORM DATA!" + "\n");
    }

    @Test
    @DisplayName("Удаление питомца")
    public void deletePet() {
        given(specification)
                .basePath("/pet/" + petId)
                .delete()
                .then().log().all()
                .spec(getAssertionSpecification());
        System.out.println("Питомец удален" + "\n");
    }

}