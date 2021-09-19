package tests.api;

import authorization.ErrorMessage;
import authorization.Token;
import authorization.User;
import org.testng.annotations.Test;
import data.Resource;
import year.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;
import static org.testng.Assert.assertTrue;

public class HomeworkTest {

    @Test
    public void avatarsCheck() {
        Resource resource =
                given()
                        .when()
                        .get("https://reqres.in/api/users?page=2")
                        .then()
                        .log().body()
                        .extract()
                        .body().as(data.Resource.class);

        assertTrue(resource.getData().get(0).getFirst_name().contains("Michael") && resource.getData().get(0).getAvatar().contains("https://reqres.in/img/faces/7-image.jpg"));
        assertTrue(resource.getData().get(1).getFirst_name().contains("Lindsay") && resource.getData().get(1).getAvatar().contains("https://reqres.in/img/faces/8-image.jpg"));
        assertTrue(resource.getData().get(2).getFirst_name().contains("Tobias") && resource.getData().get(2).getAvatar().contains("https://reqres.in/img/faces/9-image.jpg"));
        assertTrue(resource.getData().get(3).getFirst_name().contains("Byron") && resource.getData().get(3).getAvatar().contains("https://reqres.in/img/faces/10-image.jpg"));
        assertTrue(resource.getData().get(4).getFirst_name().contains("George") && resource.getData().get(4).getAvatar().contains("https://reqres.in/img/faces/11-image.jpg"));
        assertTrue(resource.getData().get(5).getFirst_name().contains("Rachel") && resource.getData().get(5).getAvatar().contains("https://reqres.in/img/faces/12-image.jpg"));
    }

    @Test
    public void authenticationCheck() {
        User user = new User("eve.holt@reqres.in", "cityslicka");

        Token token =
                given()
                        .accept("application/json")
                        .contentType("application/json")
                        .body(user)
                        .expect()
                        .statusCode(200)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().all()
                        .extract()
                        .body().as(Token.class);

        assertNotNull(token.getToken());
    }

    @Test
    public void unsuccessfulAuthenticationCheck() {
        User user = new User("peter@klaven");

        ErrorMessage message =
                given()
                        .accept("application/json")
                        .contentType("application/json")
                        .body(user)
                        .expect()
                        .statusCode(400)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().all()
                        .extract()
                        .body().as(ErrorMessage.class);

        assertNotNull(message.getError());
    }

    @Test
    public void getSortedYears() {
        year.Resource resource = given().
                when()
                .get("https://reqres.in/api/unknown?page=1")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response()
                .body().as(year.Resource.class);
        List<Integer> dates = new ArrayList<>();
        for (DataDescription x : resource.getData()) {
            dates.add(x.getYear());
        }
        assertEquals(dates, dates.stream().collect(Collectors.toList()));
        System.out.println(dates);
    }
}
