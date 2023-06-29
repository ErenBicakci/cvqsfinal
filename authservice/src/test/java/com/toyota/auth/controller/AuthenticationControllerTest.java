package com.toyota.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.auth.config.TestDataSourceConfig;
import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.Role;
import com.toyota.auth.entity.User;
import com.toyota.auth.repository.UserRepository;
import com.toyota.auth.service.JwtService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestDataSourceConfig.class)
@TestPropertySource(properties = {
        "eureka.client.enabled=true",
        "eureka.client.service-url.defaultZone=http://localhost:8761/eureka/"
})
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserRepository userRepository;


    @Spy
    private JwtService jwtService;


    @Test
    void auth() throws Exception{

    }

    @Test
    void getUserDtoByToken() throws Exception{
        User user = User
                .builder()
                .username(randomStringGenerator(10))
                .password(randomStringGenerator(10))
                .nameSurname(randomStringGenerator(10))
                .roles(List.of(Role.builder().name("ROLE_ADMIN").build()))
                .build();


        String token = jwtService.generateToken(user);

        when(userRepository.findByUsernameAndDeletedFalse(Mockito.anyString())).thenReturn(user);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/login/getUser?token="+token)
                .contentType(MediaType.APPLICATION_JSON));

        JSONObject jsonObject = new JSONObject(response.andReturn().getResponse().getContentAsString());
        assertEquals(jsonObject.get("username"), user.getUsername());
        assertEquals(jsonObject.get("nameSurname"), user.getNameSurname());
        assertEquals(jsonObject.getJSONArray("roles").getJSONObject(0).get("name"), user.getRoles().get(0).getName());

    }

    @Test
    void testSave() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .username(randomStringGenerator(10))
                .password(randomStringGenerator(10))
                .nameSurname(randomStringGenerator(10))
                .build();
        when(userRepository.save(Mockito.any())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/login/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)));
        response.andExpect(status().isOk());
        assertEquals(jwtService.findUsername(response.andReturn().getResponse().getContentAsString()), userDto.getUsername());
        }





        public static String asJsonString(final Object obj) {
            try {
                return new ObjectMapper().writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



    static String randomStringGenerator(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}