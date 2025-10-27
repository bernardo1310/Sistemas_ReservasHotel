package historico;

import modelos.Reserva;
import arevores.ArvoreRubroNegra;

import java.util.List;

/**
 * Classe que gerencia o histórico de reservas canceladas.
 * Utiliza uma árvore Rubro-Negra para manter a ordenação e permitir consultas rápidas.
 */
public class HistoricoReservas {

    private ArvoreRubroNegra<Reserva> reservasCanceladas;

    public HistoricoReservas() {
        this.reservasCanceladas = new ArvoreRubroNegra<>();
    }

    /**
     * Adiciona uma reserva ao histórico de cancelamentos.
     *
     * @param reserva Reserva cancelada
     */
    public void adicionarReservaCancelada(Reserva reserva) {
        reservasCanceladas.inserir(reserva);
        System.out.println("Reserva adicionada ao histórico: " + reserva);
    }

    /**
     * Retorna todas as reservas canceladas em ordem de check-in.
     *
     * @return Lista de reservas canceladas
     */
    public List<Reserva> listarReservasCanceladas() {
        return reservasCanceladas.getElementosEmOrdem();
    }

    /**
     * Verifica se existe alguma reserva cancelada para um determinado cliente.
     *
     * @param cpf CPF do cliente
     * @return true se houver, false caso contrário
     */
    public boolean contemReservaCancelada(String cpf) {
        for (Reserva r : reservasCanceladas.getElementosEmOrdem()) {
            if (r.getCliente().getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Total de reservas canceladas.
     *
     * @return número total de reservas canceladas
     */
    public int totalReservasCanceladas() {
        return reservasCanceladas.getElementosEmOrdem().size();
    }
}
