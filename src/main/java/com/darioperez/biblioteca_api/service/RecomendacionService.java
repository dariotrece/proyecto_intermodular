package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.exception.UsuarioNoEncontradoException;
import com.darioperez.biblioteca_api.repository.PrestamoRepository;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

@Service
public class RecomendacionService {

    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.model:openrouter/free}")
    private String model;

    public RecomendacionService(UsuarioRepository usuarioRepository,
                                PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public String recomendar(String username) {
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        var prestamos = prestamoRepository
                .findByUsuarioIdOrderByFechaPrestamoDesc(usuario.getId(), PageRequest.of(0, 10));

        if (prestamos.isEmpty()) {
            return "Aún no tienes historial de lectura. ¡Empieza pidiendo un libro prestado!";
        }

        String historial = prestamos.stream()
                .map(p -> "- \"" + p.getLibro().getTitulo() + "\" de " + p.getLibro().getAutor())
                .collect(Collectors.joining("\n"));

        String prompt = """
                Eres un experto en literatura. El usuario ha leído estos libros:
                %s
                
                Basándote en estos libros, recomiéndale 3 libros que le podrían gustar.
                Para cada recomendación indica: título, autor y una frase breve explicando por qué encaja con sus gustos.
                Responde en español y de forma amigable.
                """.formatted(historial);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "Eres un recomendador literario útil, concreto y respondes siempre en español."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.7,
                "max_completion_tokens", 500
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.set("HTTP-Referer", "http://localhost:8080");
        headers.set("X-OpenRouter-Title", "Biblioteca API");

        try {
            var entity = new HttpEntity<>(body, headers);
            var respuesta = restTemplate.postForObject(
                    OPENROUTER_URL,
                    entity,
                    Map.class
            );

            String recomendacion = extraerContenido(respuesta);
            if (recomendacion != null && !recomendacion.isBlank()) {
                return recomendacion;
            }
        } catch (RestClientException | ClassCastException | NullPointerException | IndexOutOfBoundsException e) {        }

        return recomendarLocalmente(historial);
    }

    private String extraerContenido(Map<String, Object> respuesta) {
        if (respuesta == null) {
            return null;
        }

        var choices = (List<Map<String, Object>>) respuesta.get("choices");
        if (choices == null || choices.isEmpty()) {
            return null;
        }

        var message = (Map<String, Object>) choices.get(0).get("message");
        if (message == null) {
            return null;
        }

        Object content = message.get("content");
        return content instanceof String texto ? texto : null;
    }

    private String recomendarLocalmente(String historial) {
        return """
                No he podido conectar con la IA ahora mismo, pero basándome en tu historial:
                %s
                
                Te recomiendo:
                1. Fahrenheit 451, de Ray Bradbury. Encaja si te gustan las historias reflexivas y críticas con la sociedad.
                2. Un mundo feliz, de Aldous Huxley. Buena opción si te interesan las distopías y los futuros inquietantes.
                3. La sombra del viento, de Carlos Ruiz Zafón. Puede gustarte si buscas una novela envolvente con misterio y amor por los libros.
                """.formatted(historial);
    }
}
