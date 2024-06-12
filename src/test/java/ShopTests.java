import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import static groovyjarjarantlr4.v4.runtime.misc.Utils.readFile;
import static io.restassured.RestAssured.given;

@TestMethodOrder(OrderAnnotation.class)
public class ShopTests extends BaseTest {
    //В данном тестовом классе содержатся тестовые методы для раздела Shop
    String requestBody = null;
    Integer id = 1;
    Integer petId = 1;
    Integer quantity = 1;
    String shipDate = "2024-06-12T20:19:19.384+0000";
    String status = "placed";
    Boolean complete = true;

    {
        String initialBody = null;
        try {
            initialBody = new String(readFile("src/test/resources/order.json"));

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("id", id);
        jsonObject.put("petId", petId);
        jsonObject.put("quantity", quantity);
        jsonObject.put("shipDate", shipDate);
        jsonObject.put("status", status);
        jsonObject.put("complete", complete);
        requestBody = jsonObject.toString();
    }
    @Test
    @DisplayName("Показать количество питомцев по статусу")
    @Order(1)
    public void testShopInventory() {
        Response response = given(specification)
                .basePath("/store/inventory")
                .get()
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println("Питомцы найдены по статусу");

        Map<String, Integer> inventory = response.jsonPath().getMap("$");
        Assertions.assertTrue(inventory.size() > 0);
        System.out.println("Инвентарь:");
        inventory.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("\n");
    }

    @Test
    @DisplayName("Оформление заказа")
    @Order(2)
    public void testPlaceOrder() {
        given(specification)
                .basePath("/store/order/")
                .body(requestBody)
                .post()
                .then().log().all()
                .spec(getOrderResponse(id, petId, quantity, shipDate, status, complete));
        System.out.println("Заказ успешно оформлен!");
    }

    @Test
    @DisplayName("Поиск заказа по id")
    @Order(3)
    public void testSeardOrderById(){
        given(specification)
                .basePath("/store/order/" + 1)
                .get()
                .then().log().all()
                .spec(getOrderResponse(id, petId, quantity, shipDate, status, complete));
        System.out.println("Заказ найден!" + "\n");
    }

    @Test
    @DisplayName("Удаление заказа")
    @Order(4)
    public void testDeleteOrder(){
        given(specification)
                .basePath("/store/order/" + id)
                .delete()
                .then().log().all()
                .spec(getAssertionSpecification());
        System.out.println("Заказ успешно удален!" + "\n");
    }

}
