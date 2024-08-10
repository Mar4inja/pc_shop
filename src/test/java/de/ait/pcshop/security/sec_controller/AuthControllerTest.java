package de.ait.pcshop.security.sec_controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginTest() throws Exception {
        // 1. Sagatavo testa datus: lietotāja e-pasts un parole, ko izmantos testā.
        String userJson = "{\"email\":\"mar4inja19@gmail.com\",\"password\":\"Martins35#\"}";

        // 2. Veic POST pieprasījumu uz API galapunktu "/api/users/login"
        this.mockMvc.perform(post("/api/users/login")
                        // 3. Iestata pieprasījuma saturu kā JSON formāta datu
                        .contentType("application/json")
                        // 4. Pievieno testēšanas datu ķermeni pieprasījumam
                        .content(userJson))
                // 5. Izvada pieprasījuma un atbildes detalizētu informāciju konsolē
                .andDo(print())
                // 6. Pārbauda, vai HTTP atbildes statuss ir 200 OK
                .andExpect(status().isOk())
                // 7. Pārbauda, vai `accessToken` lauks ir klāt atbildē
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                // 8. Pārbauda, vai `accessToken` lauks nav tukšs
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                // 9. Pārbauda, vai `refreshToken` lauks ir klāt atbildē
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                // 10. Pārbauda, vai `refreshToken` lauks nav tukšs
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                // 11. Pārbauda, vai `user` objekts ir klāt atbildē
                .andExpect(jsonPath("$.user").exists())
                // 12. Pārbauda, vai `user.email` atbildē ir pareizs e-pasts
                .andExpect(jsonPath("$.user.email").value("mar4inja19@gmail.com"))
                // 13. Pārbauda, vai `user.firstName` atbildē ir pareizs vārds
                .andExpect(jsonPath("$.user.firstName").value("Martins"))
                // 14. Pārbauda, vai `user.lastName` atbildē ir pareizs uzvārds
                .andExpect(jsonPath("$.user.lastName").value("Groza"))
                // 15. Pārbauda, vai `user.roles[0].authority` atbildē ir pareiza loma
                .andExpect(jsonPath("$.user.roles[0].authority").value("ROLE_USER"))
                // 16. Pārbauda, vai `user.country` atbildē ir pareiza valsts
                .andExpect(jsonPath("$.user.country").value("Latvia"))
                // 17. Pārbauda, vai `user.city` atbildē ir pareiza pilsēta
                .andExpect(jsonPath("$.user.city").value("Vilani"))
                // 18. Pārbauda, vai `user.street` atbildē ir pareiza iela
                .andExpect(jsonPath("$.user.street").value("Rigas iela 41"))
                // 19. Pārbauda, vai `user.postIndex` atbildē ir pareizs pasta indekss
                .andExpect(jsonPath("$.user.postIndex").value("LV-4838"));
    }

    @Test
    public void logoutTest() throws Exception {
        this.mockMvc.perform(post("/api/users/logout")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"))
                // 1. Pārbauda, vai `Access-Token` sīkdatne ir izdzēsta
                .andExpect(cookie().value("Access-Token", nullValue()))
                .andExpect(cookie().maxAge("Access-Token", 0))
                .andExpect(cookie().httpOnly("Access-Token", true));
    }

    @Test
    public void getNewAccessTokenTest() throws Exception {
        // Ja nepieciešams, aizvietojiet ar derīgu refreshToken
        String refreshTokenJson = "{\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXI0aW5qYTE5QGdtYWlsLmNvbSIsImV4cCI6MTcyNTkxNzY3NH0.0bcky0g4qDvU00cMkDbQoRK15eTdkc95efSHqi_v66g\"}";
        this.mockMvc.perform(post("/api/users/access")
                        .contentType("application/json")
                        .content(refreshTokenJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(cookie().value("Access-Token", notNullValue()))
                .andExpect(cookie().httpOnly("Access-Token", true));
    }

}

