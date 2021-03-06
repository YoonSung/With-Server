package acceptance_test.controller;

import acceptance_test.controller.common.WithIntergrationTest;
import acceptance_test.controller.common.WithTokenRule;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import gaongil.dto.UserDTO;
import gaongil.support.web.converter.ResponseMessage;
import gaongil.support.web.status.ApplicationCode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by yoon on 15. 6. 19..
 */
public class UserControllerTest extends WithIntergrationTest {

    @Test
    public void 위드유저_자동가입() {

        //TODO AOP적용, 중복제거
        UserDTO userDTO = new UserDTO();

        userDTO.setPhoneNumber("01012341234");
        userDTO.setRegId("testRegId");
        userDTO.setUuid("testUuid");

        //ContentType을 JSON으로 지정하면, JSON 형태의 body데이터를 스프링서버에서 @RequestBody annotation을 통해 객체화 시켜준다.
        //반대로 ContentType이 일반 String일 경우, formData로 전송하며(contentType form-url-encoding & multipart), 스프링서버에서는 @RequestBody를 사용하면 안된다.
        //http://stackoverflow.com/questions/4007969/application-x-www-form-urlencoded-or-multipart-form-data
        Response response =
            given().
//                    contentType(ContentType.JSON).
//                    body(userDTO).
                    contentType(ContentType.URLENC).
                    formParam("phoneNumber", "01099258547").
                    formParam("regId", "testRegId").
                    formParam("uuid", "testUuid").
            when().
                    post("/users").

            then().
                    statusCode(200).
                    extract().response();

        ResponseMessage responseMessage = response.as(ResponseMessage.class);
        assertEquals(ApplicationCode.CREATE_NEWDATA.intValue(), responseMessage.getCode());

        String token = response.getCookie(WithTokenRule.AUTH_COOKIE_NAME);
        assertNotNull(token);
    }
}