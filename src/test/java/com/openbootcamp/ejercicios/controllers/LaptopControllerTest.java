package com.openbootcamp.ejercicios.controllers;

import com.openbootcamp.ejercicios.entities.Laptop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LaptopControllerTest {

    private TestRestTemplate testRestTemplate;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void findAll() {
        ResponseEntity<Laptop[]> response = testRestTemplate.getForEntity("/api/laptops", Laptop[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());

//        List<Laptop> laptops = Arrays.asList(response.getBody());
//        System.out.println("Total de laptops: " + laptops.size());
    }

    @Test
    void findOneById() {
        ResponseEntity<Laptop> response = testRestTemplate.getForEntity("/api/laptops/1", Laptop.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void create() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Arrays.asList reemplazado por List.of

        String json = """
                {
                    "marca": "Laptop creado desde Spring Test",
                    "modelo": "Test laptop",
                    "precio": 1550.95
                }
                """;

        HttpEntity<String> request = new HttpEntity<>(json,headers);

        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops", HttpMethod.POST, request, Laptop.class);

        Laptop result = response.getBody();


        assertEquals(1L, result.getId());
        assertEquals("Laptop creado desde Spring Test", result.getMarca());
    }

    @Test
    void update() {
        // Creamos un nuevo laptop para poder actualizarlo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Arrays.asList reemplazado por List.of

        String json = """
                {
                    "marca": "Laptop creado desde Spring Test",
                    "modelo": "Test laptop",
                    "precio": 1550.95
                }
                """;

        HttpEntity<String> request = new HttpEntity<>(json,headers);

        testRestTemplate.exchange("/api/laptops", HttpMethod.POST, request, Laptop.class);


        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Reemplazado Arrays.asList

        json = """
                {
                    "id": 2,
                    "marca": "Laptop actualizado desde Spring Test",
                    "modelo": "Test update laptop",
                    "precio": 1550.95
                }
                """;

        request = new HttpEntity<>(json,headers);

        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops", HttpMethod.PUT, request, Laptop.class);

        Laptop result = response.getBody();

        assertEquals(2L, result.getId());
        assertEquals("Laptop actualizado desde Spring Test", result.getMarca());
    }

    @Test
    void delete() {
        testRestTemplate.delete("/api/laptops/1");

        ResponseEntity<Laptop> response = testRestTemplate.getForEntity("/api/laptops/1", Laptop.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAll() {
        ResponseEntity<Laptop[]> response = testRestTemplate.getForEntity("/api/laptops", Laptop[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());

        List<Laptop> laptops = Arrays.asList(response.getBody());
        System.out.println("Total de laptops: " + laptops.size());
        if(laptops.size() == 0) {
            create();
            delete();
        } else {
            testRestTemplate.delete("/api/laptops");
        }

        response = testRestTemplate.getForEntity("/api/laptops", Laptop[].class);

        assertEquals(0, Arrays.asList(response.getBody()).size());
    }
}