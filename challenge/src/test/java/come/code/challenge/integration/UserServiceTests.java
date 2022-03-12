package come.code.challenge.integration;

import com.code.challenge.ChallengeApplication;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.exception.UserLogicalException;
import com.code.challenge.repository.EmailRepository;
import com.code.challenge.repository.PhoneNumberRepository;
import com.code.challenge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ChallengeApplication.class)
@AutoConfigureMockMvc
class UserServiceTests {

    private static final String CREATE_USER_URL = "/user/create";
    private static final String USER_URL = "/user";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    PhoneNumberRepository phoneNumberRepository;

    @Test
    void createUserTest() throws Exception {
        //************************
        //          Given
        //************************
        String requestBody = "{\"lastName\":\"mina\",\"firstName\":\"bonyadi\",\"emails\":[\"min\"],\"phoneNumbers\":[\"phon\"]}";

        //************************
        //          WHEN
        //************************

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        assertThat(responseBody.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    void createUserWhenUserIsAlreadyExistsTest() throws Exception {
        //************************
        //          Given
        //************************
        userRepository.deleteAll();
        userRepository.save(User.builder()
                .firstName("mina")
                .lastName("bonyadi")
                .emails(Collections.singleton(Email.builder().mail("mina.bonyadi92@yahoo.com").build()))
                .phoneNumbers(Collections.singleton(PhoneNumber.builder().number("76276346").build()))
                .build());

        String requestBody = "{\"lastName\":\"bonyadi\",\"firstName\":\"mina\",\"emails\":[\"mina.bonyadi92@yahoo.com\"],\"phoneNumbers\":[\"76276346\"]}";

        //************************
        //          WHEN
        //************************

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserLogicalException))
                .andExpect(result -> assertEquals("User is already exists!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        assertThat(responseBody.getResponse().getContentAsString()).isNotEmpty();
    }


    @Test
    void getUserByIdTest() throws Exception {
        //************************
        //          Given
        //************************
        userRepository.deleteAll();
        userRepository.save(
                User.builder()
                .firstName("mina")
                .lastName("bonyadi")
                .emails(Collections.singleton(Email.builder().mail("mina.bonyadi92@yahoo.com").build()))
                .phoneNumbers(Collections.singleton(PhoneNumber.builder().number("76276346").build()))
                .build());
        //************************
        //          WHEN
        //************************

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.get(USER_URL+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        assertThat(responseBody.getResponse().getContentAsString()).isNotEmpty();
//        JSONAssert.assertEquals();
    }
}
