import org.json.JSONObject;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static groovyjarjarantlr4.v4.runtime.misc.Utils.readFile;
import static io.restassured.RestAssured.given;
public class UserTests extends BaseTest {
    //В данном тестовом классе содержатся тестовые методы для раздела User

    //Создание пользователя на основе json
    public String createUser(int id, String userName, String firstName, String lastName, String email, String password, int userStatus){
        String requestBody = null;
        String initialBody = null;
        try {
            initialBody = new String(readFile("src/test/resources/userInformation.json"));

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("id", id);
        jsonObject.put("username", userName);
        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("userStatus", userStatus);
        requestBody = jsonObject.toString();
        return requestBody;
    }

    @DisplayName("Создание пользователя")
    @ParameterizedTest
    @CsvSource({
            "1, maybeme, Marina, Malinovskaya, malina.marina@example.com, 123456, 0",
            "2, maybeyou, Will, Smith, will.smith@example.com, 789456, 0"
    })
    public void testCreateUser(int id, String userName, String firstName, String lastName, String email, String password, int userStatus)
            throws IOException{
        String requestBody = createUser(id, userName, firstName, lastName, email, password, userStatus);
        given(specification)
                .basePath("/user")
                .body(requestBody)
                .post()
                .then().log().all()
                .spec(getUsersResponse(id, userName, firstName, lastName, email, password, userStatus));

        System.out.println("Пользователь успешно добавлен" + "\n");
    }

    @ParameterizedTest
    @DisplayName("Вход пользователя в систему")
    @CsvSource({
            "maybeme, 123456",
            "maybeyou, 789456"
    })
    public void testLogUser(String userName, String password){
        given(specification)
                .basePath("/user/login")
                .contentType("application/x-www-form-urlencoded")
                .formParam("userName", userName)
                .formParam("password", password)
                .post()
                .then().log().all()
                .spec(getAssertionSpecification());
        System.out.println("Пользователь успешно вошел в систему" + "\n");
    }

    @ParameterizedTest
    @CsvSource({
            "1, maybeme, Mariya, Malinovskaya, malina.marina@example.com, 123456, 0",
            "2, maybeyou, Jaden, Smith, will.smith@example.com, 789456, 0"
    })
    @DisplayName("Обновление информации о пользователе")
    public void testUpdateUser(int id, String userName, String firstName, String lastName, String email, String password, int userStatus){
        String requestBody = createUser(id, userName, firstName, lastName, email, password, userStatus);
        given(specification)
                .basePath("/user/" + userName)
                .body(requestBody)
                .put()
                .then().log().all()
                .spec(getUsersResponse(id, userName, firstName, lastName, email, password, userStatus));
        System.out.println("Иформация о пользователе успешно изменена" + "\n");
    }

    @ParameterizedTest
    @CsvSource({
            "maybeme",
            "maybeyou"
    })
    @DisplayName("Удаление пользователя")
    public void deleteUser(String userName) {
        given(specification)
                .basePath("/user/" + userName)
                .delete()
                .then().log().all()
                .spec(getAssertionSpecification());
        System.out.println("Пользователь удален" + "\n");
    }

}
