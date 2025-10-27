package modelos;

import java.util.Objects;

/**
 * Classe que representa um Quarto de hotel.
 */
public class Quarto {

    private int numero;         // número do quarto
    private String categoria;   // categoria do quarto (ex: econômico, luxo, suíte)

    /**
     * Construtor do Quarto.
     *
     * @param numero    Número do quarto (único)
     * @param categoria Categoria do quarto
     */
    public Quarto(int numero, String categoria) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número do quarto deve ser maior que zero.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia.");
        }
        this.numero = numero;
        this.categoria = categoria.trim();
    }

    // ==========================
    // GETTERS E SETTERS
    // ==========================
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número do quarto deve ser maior que zero.");
        }
        this.numero = numero;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser vazia.");
        }
        this.categoria = categoria.trim();
    }

    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================
    @Override
    public String toString() {
        return "Quarto{" +
                "numero=" + numero +
                ", categoria='" + categoria + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quarto)) return false;
        Quarto quarto = (Quarto) o;
        return numero == quarto.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
