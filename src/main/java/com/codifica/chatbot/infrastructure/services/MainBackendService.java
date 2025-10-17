package com.codifica.chatbot.infrastructure.services;

import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.raca.Raca;
import com.codifica.chatbot.core.domain.servico.Servico;
import com.codifica.chatbot.core.domain.shared.Dia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

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

    public List<Dia> getAvailableDays(LocalDate startDate, LocalDate endDate) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "agendas/disponibilidade/dias")
                .queryParam("inicio", startDate.toString())
                .queryParam("fim", endDate.toString())
                .toUriString();

        ResponseEntity<List<Dia>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                new ParameterizedTypeReference<List<Dia>>() {}
        );
        return response.getBody();
    }

    public List<String> getAvailableTimes(LocalDate day) {
        String url = apiUrl + "agendas/disponibilidade/horarios?dia=" + day;
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHeaders(),
                new ParameterizedTypeReference<List<String>>() {}
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
