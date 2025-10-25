package com.codifica.chatbot.infrastructure.services;

import com.codifica.chatbot.core.domain.agenda.Agenda;
import com.codifica.chatbot.core.domain.disponibilidade.Disponibilidade;
import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.raca.Raca;
import com.codifica.chatbot.core.domain.servico.Servico;
import com.codifica.chatbot.core.domain.shared.Dia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MainBackendService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.url}")
    private String apiUrl;

    @Value("${internal.api.key}")
    private String internalApiKey;

    private HttpEntity<String> createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-API-Key", internalApiKey);
        return new HttpEntity<>(headers);
    }

    public List<Pet> listPetsByClienteId(Integer clienteId) {
        String url = apiUrl + "pets/filtro?clienteId=" + clienteId;
        ResponseEntity<List<Pet>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                new ParameterizedTypeReference<List<Pet>>() {}
        );
        return response.getBody();
    }

    public Raca findRacaByNome(String nome) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment("racas", "nome", nome)
                .build()
                .toUriString();

        ResponseEntity<Raca> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                Raca.class
        );
        return response.getBody();
    }

    public List<Raca> findRacasByNomeSemelhante(String nome) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment("racas", "nome", "aproximado", nome)
                .build()
                .toUriString();

        ResponseEntity<List<Raca>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                new ParameterizedTypeReference<List<Raca>>() {}
        );
        return response.getBody();
    }

    public Optional<Agenda> getFutureAgendaByPetId(Integer petId) {
        String url = apiUrl + "agendas/futuro/pet/" + petId;
        try {
            ResponseEntity<Agenda> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHeaders(),
                    Agenda.class
            );
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            } else {
                return Optional.empty();
            }
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    public List<Disponibilidade> getDisponibilidade(LocalDateTime startDate, LocalDateTime endDate) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "agendas/disponibilidade")
                .queryParam("inicio", startDate.toString())
                .queryParam("fim", endDate.toString())
                .toUriString();

        ResponseEntity<List<Disponibilidade>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                new ParameterizedTypeReference<List<Disponibilidade>>() {}
        );
        return response.getBody();
    }

    public List<Servico> getServicos() {
        String url = apiUrl + "servicos";
        ResponseEntity<List<Servico>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                new ParameterizedTypeReference<List<Servico>>() {}
        );
        return response.getBody();
    }
}
