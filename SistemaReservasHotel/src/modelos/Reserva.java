package modelos;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa uma Reserva de quarto em um hotel.
 */
public class Reserva implements Comparable<Reserva> {

    private Cliente cliente;         // cliente responsável pela reserva
    private Quarto quarto;           // quarto reservado
    private LocalDate dataCheckIn;   // data de entrada
    private LocalDate dataCheckOut;  // data de saída

    /**
     * Construtor da Reserva.
     *
     * @param cliente      Cliente que faz a reserva
     * @param quarto       Quarto reservado
     * @param dataCheckIn  Data de entrada
     * @param dataCheckOut Data de saída
     */
    public Reserva(Cliente cliente, Quarto quarto, LocalDate dataCheckIn, LocalDate dataCheckOut) {
        if (cliente == null) throw new IllegalArgumentException("Cliente não pode ser nulo.");
        if (quarto == null) throw new IllegalArgumentException("Quarto não pode ser nulo.");
        if (dataCheckIn == null || dataCheckOut == null)
            throw new IllegalArgumentException("Datas não podem ser nulas.");
        if (dataCheckOut.isBefore(dataCheckIn))
            throw new IllegalArgumentException("Data de check-out não pode ser antes do check-in.");

        this.cliente = cliente;
        this.quarto = quarto;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
    }

    // ==========================
    // GETTERS E SETTERS
    // ==========================
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente não pode ser nulo.");
        this.cliente = cliente;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        if (quarto == null) throw new IllegalArgumentException("Quarto não pode ser nulo.");
        this.quarto = quarto;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(LocalDate dataCheckIn) {
        if (dataCheckIn == null) throw new IllegalArgumentException("Data de check-in não pode ser nula.");
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(LocalDate dataCheckOut) {
        if (dataCheckOut == null) throw new IllegalArgumentException("Data de check-out não pode ser nula.");
        if (dataCheckOut.isBefore(this.dataCheckIn))
            throw new IllegalArgumentException("Data de check-out não pode ser antes do check-in.");
        this.dataCheckOut = dataCheckOut;
    }

    // ==========================
    // MÉTODOS AUXILIARES
    // ==========================
    @Override
    public String toString() {
        return "Reserva{" +
                "cliente=" + cliente +
                ", quarto=" + quarto +
                ", dataCheckIn=" + dataCheckIn +
                ", dataCheckOut=" + dataCheckOut +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(cliente, reserva.cliente) &&
               Objects.equals(quarto, reserva.quarto) &&
               Objects.equals(dataCheckIn, reserva.dataCheckIn) &&
               Objects.equals(dataCheckOut, reserva.dataCheckOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, quarto, dataCheckIn, dataCheckOut);
    }

    /**
     * Implementa a comparação entre reservas para ordenação na Árvore Rubro-Negra.
     * Aqui usamos a data de check-in como chave principal.
     *
     * @param outra Reserva a ser comparada
     * @return valor negativo se this < outra, 0 se iguais, positivo se this > outra
     */
    @Override
    public int compareTo(Reserva outra) {
        int comp = this.dataCheckIn.compareTo(outra.dataCheckIn);
        if (comp == 0) {
            // se a data de check-in for igual, diferenciar pelo número do quarto
            comp = Integer.compare(this.quarto.getNumero(), outra.quarto.getNumero());
        }
        return comp;
    }
}
