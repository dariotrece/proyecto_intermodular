package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.config.SecurityConfig;
import com.darioperez.biblioteca_api.dto.LibroView;
import com.darioperez.biblioteca_api.exception.GlobalExceptionHandler;
import com.darioperez.biblioteca_api.model.Libro;
import com.darioperez.biblioteca_api.service.BibliotecaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BibliotecaService bibliotecaService;

    @Test
    void listarLibrosRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/libros"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void crearLibroRequiresBibliotecarioRole() throws Exception {
        mockMvc.perform(post("/libros")
                        .with(user("usuario").roles("USUARIO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "1234567890",
                                  "titulo": "Domain-Driven Design",
                                  "autor": "Eric Evans"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void crearLibroReturnsCreatedBookForValidRequest() throws Exception {
        Libro libro = new Libro("1234567890", "Domain-Driven Design", "Eric Evans");
        when(bibliotecaService.crearLibro("1234567890", "Domain-Driven Design", "Eric Evans"))
                .thenReturn(libro);

        mockMvc.perform(post("/libros")
                        .with(user("bibliotecario").roles("BIBLIOTECARIO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "1234567890",
                                  "titulo": "Domain-Driven Design",
                                  "autor": "Eric Evans"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.titulo").value("Domain-Driven Design"))
                .andExpect(jsonPath("$.autor").value("Eric Evans"));
    }

    @Test
    void crearLibroReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/libros")
                        .with(user("bibliotecario").roles("BIBLIOTECARIO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "12",
                                  "titulo": "",
                                  "autor": "A"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());

        verify(bibliotecaService, never()).crearLibro(anyString(), anyString(), anyString());
    }

    @Test
    void buscarLibroByIsbnIsPublic() throws Exception {
        when(bibliotecaService.buscarLibro("1234567890"))
                .thenReturn(new LibroView("1234567890", "Clean Architecture", "Robert C. Martin", true));

        mockMvc.perform(get("/libros/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void listarLibrosReturnsServicePayload() throws Exception {
        when(bibliotecaService.listarLibrosConEstado()).thenReturn(List.of(
                new LibroView("1234567890", "Clean Code", "Robert C. Martin", false)
        ));

        mockMvc.perform(get("/libros")
                        .with(user("bibliotecario").roles("BIBLIOTECARIO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$[0].disponible").value(false));

        verify(bibliotecaService).listarLibrosConEstado();
    }
}
