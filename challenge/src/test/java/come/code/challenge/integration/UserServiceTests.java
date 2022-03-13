package come.code.challenge.integration;

import com.code.challenge.ChallengeApplication;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.exception.UserLogicalException;
import com.code.challenge.exception.UserNotFoundException;
import com.code.challenge.repository.EmailRepository;
import com.code.challenge.repository.PhoneNumberRepository;
import com.code.challenge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ChallengeApplication.class)
@AutoConfigureMockMvc
class UserServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    private long sampleUser() {
        userRepository.deleteAll();
        User user = User.builder()
                .firstName("mina")
                .lastName("bonyadi")
                .build();

        userRepository.save(user);

        Email email = Email.builder().mail("mina.bonyadi92@yahoo.com").id(user.getId()).user(user).build();
        PhoneNumber phoneNumber = PhoneNumber.builder().number("76276346").id(user.getId()).user(user).build();
        emailRepository.save(email);
        phoneNumberRepository.save(phoneNumber);

        return user.getId();
    }

    @Test
    void createUserTest() throws Exception {
        //************************
        //          Given
        //************************
        userRepository.deleteAll();
        String requestBody = "{\"id\":0,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                "\"emails\":[{\"id\":0,\"mail\":\"mina.bonyadi92@yahoo.com\"}],\"phoneNumbers\":[{\"id\":0,\"number\":\"76276346\"}]}";
        //************************
        //          WHEN
        //************************

        MvcResult responseBody = mockMvc.perform(post("/users/create")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("76276346");
    }

    @Test
    void createUserWhenUserIsAlreadyExistsTest() throws Exception {
        //************************
        //          Given
        //************************
        sampleUser();
        String requestBody = "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                "\"emails\":[{\"id\":0,\"mail\":\"mina.bonyadi92@yahoo.com\"}],\"phoneNumbers\":[{\"id\":0,\"number\":\"76276346\"}]}";

        //************************
        //          WHEN
        //************************
        //************************
        //          THEN
        //************************
        mockMvc.perform(post("/users/create")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserLogicalException))
                .andExpect(result -> assertEquals("User is already exists!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();
    }


    @Test
    void getUserByIdTest() throws Exception {
        //************************
        //          Given
        //************************
        long userId = sampleUser();
        //************************
        //          WHEN
        //************************

        MvcResult responseBody = mockMvc.perform(get("/users/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("76276346");
    }

    @Test
    void getUserByIdWhenItIsNotFoundTest() throws Exception {
        //************************
        //          Given
        //************************
        sampleUser();
        //************************
        //          WHEN
        //************************
        //************************
        //          THEN
        //************************

        mockMvc.perform(get("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User Not Found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();

    }

    @Test
    void getUserByName() throws Exception {
        //************************
        //          Given
        //************************
        sampleUser();
        //************************
        //          WHEN
        //************************

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("firstName", "mina");
        parameters.add("lastName", "bonyadi");

        MvcResult responseBody = mockMvc.perform(get("/users/spec")
                .params(parameters)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("76276346");
    }

    @Test
    void getUserByIdWhenUserItIsNotFoundTest() throws Exception {
        //************************
        //          Given
        //************************
        userRepository.deleteAll();
        //************************
        //          WHEN
        //************************
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("firstName", "mina");
        parameters.add("lastName", "bonyadi");

        //************************
        //          THEN
        //************************

        mockMvc.perform(get("/users/spec")
                .params(parameters)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User Not Found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();

    }

    @Test
    void addUserDataMail() throws Exception {
        //************************
        //          Given
        //************************
        long id = sampleUser();
        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/emails");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", id);

        MvcResult responseBody = mockMvc.perform(put(requestTemplate.expand(uriVariables).toString())
                .content("{\"mail\":\"mina.bonyadi92@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).contains("mina.bonyadi92@gmail.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("76276346");
    }

    @Test
    void addUserDataPhoneTest() throws Exception {
        //************************
        //          Given
        //************************
        sampleUser();
        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/phone-numbers");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        MvcResult responseBody = mockMvc.perform(put(requestTemplate.expand(uriVariables).toString())
                .content("{\"number\":\"9108732165\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("9108732165");
        assertThat(restResponse).contains("76276346");
    }

    @Test
    void updateUserDataMail() throws Exception {
        //************************
        //          Given
        //************************
        long id = sampleUser();
        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/emails");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", id);

        MvcResult responseBody = mockMvc.perform(put(requestTemplate.expand(uriVariables).toString())
                .content("{\"mail\":\"mina.bonyadi92@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@gmail.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("76276346");

    }

    @Test
    void updateUserDataPhoneNumber() throws Exception {
        //************************
        //          Given
        //************************
        long id = sampleUser();
        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/phone-numbers");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", id);

        MvcResult responseBody = mockMvc.perform(put(requestTemplate.expand(uriVariables).toString())
                .content("{\"number\":\"9108732165\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).contains("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).contains("bonyadi");
        assertThat(restResponse).contains("mina");
        assertThat(restResponse).contains("9108732165");
    }

    @Test
    void deleteUser() throws Exception {
        //************************
        //          Given
        //************************
        long id = sampleUser();
        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", id);

        MvcResult responseBody = mockMvc.perform(delete(requestTemplate.expand(uriVariables).toString())
                .content("{\"id\":0,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                        "\"emails\":[{\"id\":0,\"mail\":\"mina.bonyadi92@yahoo.com\"}],\"phoneNumbers\":[{\"id\":0,\"number\":\"76276346\"}]}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();

        assertThat(restResponse).doesNotContain("mina.bonyadi92@yahoo.com");
        assertThat(restResponse).doesNotContain("bonyadi");
        assertThat(restResponse).doesNotContain("mina");
        assertThat(restResponse).doesNotContain("9108732165");
    }
}
