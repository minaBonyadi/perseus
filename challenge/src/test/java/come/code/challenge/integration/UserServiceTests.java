package come.code.challenge.integration;

import com.code.challenge.ChallengeApplication;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.exception.EmailNotFoundException;
import com.code.challenge.exception.PhoneNumberNotFoundException;
import com.code.challenge.exception.UserLogicalException;
import com.code.challenge.exception.UserNotFoundException;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ChallengeApplication.class)
@AutoConfigureMockMvc
class UserServiceTests {

    private static final String CREATE_USER_URL = "/users/create";
    private static final String USER_URL = "/users";

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
        createSampleUser();

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
        String restResponse = responseBody.getResponse().getContentAsString();
        assertThat(restResponse).isNotEmpty();
        JSONAssert.assertEquals(restResponse,
                "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\",\"emails\":[{\"mail\":\"mina.bonyadi92@yahoo.com\"}]," +
                        "\"phoneNumbers\":[{\"number\":\"76276346\"}]}",
                true);
    }

    @Test
    void getUserByIdWhenItIsNotFoundTest() throws Exception {
        //************************
        //          Given
        //************************
        createSampleUser();

        //************************
        //          WHEN
        //************************
        //************************
        //          THEN
        //************************

        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL+"/2")
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
        createSampleUser();

        //************************
        //          WHEN
        //************************

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("firstName", "mina");
        parameters.add("lastName", "bonyadi");

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.get(USER_URL+"/spec")
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
        assertThat(restResponse).isNotEmpty();
        JSONAssert.assertEquals(restResponse,
                "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\",\"emails\":[{\"mail\":\"mina.bonyadi92@yahoo.com\"}]," +
                        "\"phoneNumbers\":[{\"number\":\"76276346\"}]}",
                true);
    }

    @Test
    void getUserByIdWhenUserItIsNotFoundTest() throws Exception {
        //************************
        //          Given
        //************************
        //************************
        //          WHEN
        //************************
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("firstName", "mina");
        parameters.add("lastName", "bonyadi");

        //************************
        //          THEN
        //************************

        mockMvc.perform(MockMvcRequestBuilders.get(USER_URL+"/spec")
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
        createSampleUser();

        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/emails");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.put(requestTemplate.expand(uriVariables).toString())
                .content("{\"id\":0,\"mail\":\"mina.bonyadi92@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();
        assertThat(restResponse).isNotEmpty();
        JSONAssert.assertEquals(restResponse,
                "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                        "\"emails\":[{\"mail\":\"mina.bonyadi92@yahoo.com\"},{\"mail\":\"mina.bonyadi92@gmail.com\"}]," +
                        "\"phoneNumbers\":[{\"number\":\"76276346\"}]}",
                true);
    }

    @Test
    void addUserDataPhoneTest() throws Exception {
        //************************
        //          Given
        //************************
        createSampleUser();

        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/phone-numbers");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.put(requestTemplate.expand(uriVariables).toString())
                .content("\"id\":0,{\"number\":\"9108732165\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();
        assertThat(restResponse).isNotEmpty();
        JSONAssert.assertEquals(restResponse,
                "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                        "\"emails\":[{\"mail\":\"mina.bonyadi92@yahoo.com\"}]," +
                        "\"phoneNumbers\":[{\"number\":\"9108732165\"},{\"number\":\"76276346\"}]}",
                true);
    }

    @Test
    void updateUserDataMail() throws Exception {
        //************************
        //          Given
        //************************
        createSampleUser();

        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/emails");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.put(requestTemplate.expand(uriVariables).toString())
                .content("{\"id\":1,\"mail\":\"mina.bonyadi92@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();
        assertThat(restResponse).isNotEmpty();
        JSONAssert.assertEquals(restResponse,
                "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                        "\"emails\":[{\"id\":0,\"mail\":\"mina.bonyadi92@gmail.com\"}]," +
                        "\"phoneNumbers\":[{\"number\":\"76276346\"}]}",
                true);
    }

    @Test
    void updateUserDataPhoneNumber() throws Exception {
        //************************
        //          Given
        //************************
        createSampleUser();

        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/phone-numbers");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.put(requestTemplate.expand(uriVariables).toString())
                .content("{\"id\":1,\"number\":\"9108732165\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //************************
        //          THEN
        //************************
        // check rest response
        String restResponse = responseBody.getResponse().getContentAsString();
        assertThat(restResponse).isNotEmpty();
        JSONAssert.assertEquals(restResponse,
                "{\"id\":1,\"lastName\":\"bonyadi\",\"firstName\":\"mina\"," +
                        "\"emails\":[{\"id\":0,\"mail\":\"mina.bonyadi92@yahoo.com\"}]," +
                        "\"phoneNumbers\":[{\"id\":0,\"number\":\"9108732165\"}]}",
                true);
    }

    @Test
    void updateUserDataMailWhenMailNotFound() throws Exception {
        //************************
        //          Given
        //************************
        createSampleUser();

        //************************
        //          WHEN
        //************************

        UriTemplate requestTemplate = new UriTemplate("/users/{id}/emails");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        MvcResult responseBody = mockMvc.perform(MockMvcRequestBuilders.put(requestTemplate.expand(uriVariables).toString())
                .content("{\"id\":11,\"mail\":\"mina.bonyadi92@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmailNotFoundException))
                .andExpect(result -> assertEquals("Email Not Found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();
    }

    @Test
    void addOrUpdateUserDataPhoneNumberWhenPhoneNoNotFound() throws Exception {
        //************************
        //          Given
        //************************
        createSampleUser();

        //************************
        //          WHEN
        //************************
        //************************
        //          THEN
        //************************
        UriTemplate requestTemplate = new UriTemplate("/users/{id}/phone-numbers");
        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 1L);

        mockMvc.perform(MockMvcRequestBuilders.put(requestTemplate.expand(uriVariables).toString())
                .content("{\"id\":12,\"number\":\"9108732165\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PhoneNumberNotFoundException))
                .andExpect(result -> assertEquals("Phone number Not Found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andReturn();
    }

    private void createSampleUser() {
        userRepository.deleteAll();
        User user = User.builder()
                .firstName("mina")
                .lastName("bonyadi")
                .build();

        userRepository.save(user);
        emailRepository.save(Email.builder().mail("mina.bonyadi92@yahoo.com").user(user).build());
        phoneNumberRepository.save(PhoneNumber.builder().number("76276346").user(user).build());
    }
}
