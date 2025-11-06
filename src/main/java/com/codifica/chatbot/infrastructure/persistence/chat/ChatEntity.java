package com.codifica.chatbot.infrastructure.persistence.chat;

import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat")
public class ChatEntity {

    @Id
    private Long id;

    @Column(name = "passo_atual")
    private String passoAtual;

    @Type(JsonType.class)
    @Column(name = "dados_contexto", columnDefinition = "json")
    private String dadosContexto;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private ClienteEntity cliente;

    public ChatEntity(Long id, String passoAtual, String dadosContexto, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.passoAtual = passoAtual;
        this.dadosContexto = dadosContexto;
        this.dataAtualizacao = dataAtualizacao;
    }

    public ChatEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassoAtual() {
        return passoAtual;
    }

    public void setPassoAtual(String passoAtual) {
        this.passoAtual = passoAtual;
    }

    public String getDadosContexto() {
        return dadosContexto;
    }

    public void setDadosContexto(String dadosContexto) {
        this.dadosContexto = dadosContexto;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }
}
