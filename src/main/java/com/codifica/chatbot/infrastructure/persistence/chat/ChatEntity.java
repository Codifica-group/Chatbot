package com.codifica.chatbot.infrastructure.persistence.chat;

import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "passo_atual")
    private String passoAtual;

    @Type(JsonType.class)
    @Column(name = "dados_contexto", columnDefinition = "json")
    private String dadosContexto;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ChatClienteEntity chatCliente;

    public ChatEntity(Integer id, String passoAtual, String dadosContexto, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.passoAtual = passoAtual;
        this.dadosContexto = dadosContexto;
        this.dataAtualizacao = dataAtualizacao;
    }

    public ChatEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public ChatClienteEntity getChatCliente() {
        return chatCliente;
    }

    public void setChatCliente(ChatClienteEntity chatCliente) {
        if (chatCliente == null) {
            if (this.chatCliente != null) {
                this.chatCliente.setChat(null);
            }
        } else {
            chatCliente.setChat(this);
        }
        this.chatCliente = chatCliente;
    }
}
