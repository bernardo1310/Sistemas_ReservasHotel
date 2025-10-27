package modelos;

import java.util.Objects;

/**
 * Classe que representa um Cliente de hotel.
 */
public class Cliente {

    private String nome;
    private String cpf; // CPF como identificador único

    /**
     * Construtor do Cliente.
     *
     * @param nome Nome completo do cliente
     * @param cpf  CPF do cliente (apenas números, 11 dígitos)
     */
    public Cliente(String nome, String cpf) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (!validarCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        this.nome = nome.trim();
        this.cpf = cpf.trim();
    }

    // ==========================
    // GETTERS E SETTERS
    // ==========================
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (!validarCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        this.cpf = cpf.trim();
    }

    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================
    /**
     * Validação simples de CPF (apenas formato: 11 dígitos numéricos).
     * Pode ser substituído por validação mais completa posteriormente.
     */
    private boolean validarCPF(String cpf) {
        if (cpf == null) return false;
        String numeros = cpf.replaceAll("\\D", ""); // remove caracteres não numéricos
        return numeros.length() == 11;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
