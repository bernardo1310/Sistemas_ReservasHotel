package gerenciador;

import modelos.Reserva;
import modelos.Quarto;
import historico.HistoricoReservas;
import arevores.ArvoreRubroNegra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gerencia as reservas ativas do hotel.
 * Utiliza uma árvore Rubro-Negra para armazenar reservas.
 */
public class GerenciadorReservas {

    private ArvoreRubroNegra<Reserva> reservasAtivas;
    private HistoricoReservas historico;

    /**
     * Construtor do Gerenciador.
     *
     * @param historico Histórico de reservas canceladas
     */
    public GerenciadorReservas(HistoricoReservas historico) {
        this.reservasAtivas = new ArvoreRubroNegra<>();
        this.historico = historico;
    }

    // ============================================================
    // CADASTRO DE RESERVAS
    // ============================================================

    /**
     * Cadastra uma nova reserva se não houver conflito de datas.
     *
     * @param reserva Reserva a ser cadastrada
     * @return true se cadastrada com sucesso, false se houver conflito
     */
    public boolean cadastrarReserva(Reserva reserva) {
        if (verificarConflito(reserva.getQuarto(), reserva.getDataCheckIn(), reserva.getDataCheckOut())) {
            System.out.println("Erro: Conflito de reserva para o quarto " + reserva.getQuarto().getNumero());
            return false;
        }
        reservasAtivas.inserir(reserva);
        System.out.println("Reserva cadastrada com sucesso: " + reserva);
        return true;
    }

    /**
     * Verifica se o quarto está disponível entre as datas fornecidas.
     *
     * @param quarto   Quarto a verificar
     * @param checkIn  Data de check-in
     * @param checkOut Data de check-out
     * @return true se houver conflito, false se disponível
     */
    public boolean verificarConflito(Quarto quarto, LocalDate checkIn, LocalDate checkOut) {
        for (Reserva r : reservasAtivas.getElementosEmOrdem()) {
            if (r.getQuarto().equals(quarto)) {
                if (!(checkOut.isBefore(r.getDataCheckIn()) || checkIn.isAfter(r.getDataCheckOut()))) {
                    return true; // conflito encontrado
                }
            }
        }
        return false; // sem conflito
    }

    // ============================================================
    // CANCELAMENTO DE RESERVAS
    // ============================================================

    /**
     * Cancela uma reserva existente.
     *
     * @param reserva Reserva a ser cancelada
     * @return true se cancelada com sucesso, false se não encontrada
     */
    public boolean cancelarReserva(Reserva reserva) {
        // Remoção balanceada na RBTree
        boolean removido = reservasAtivas.remover(reserva);
        if (removido) {
            historico.adicionarReservaCancelada(reserva);
            System.out.println("Reserva cancelada com sucesso: " + reserva);
            return true;
        }
        System.out.println("Reserva não encontrada: " + reserva);
        return false;
    }

    // ============================================================
    // CONSULTAS
    // ============================================================

    /**
     * Consulta reserva por CPF do cliente.
     *
     * @param cpf CPF do cliente
     * @return Reserva encontrada ou null
     */
    public Reserva consultarReservaPorCliente(String cpf) {
        for (Reserva r : reservasAtivas.getElementosEmOrdem()) {
            if (r.getCliente().getCpf().equals(cpf)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Lista todas as reservas ativas em ordem de check-in.
     *
     * @return Lista de reservas
     */
    public List<Reserva> listarReservas() {
        return reservasAtivas.getElementosEmOrdem();
    }

    /**
     * Lista quartos disponíveis em uma data e categoria específicas.
     *
     * @param todosQuartos Lista completa de quartos
     * @param data         Data desejada
     * @param categoria    Categoria desejada
     * @return Lista de quartos disponíveis
     */
    public List<Quarto> listarQuartosDisponiveis(List<Quarto> todosQuartos, LocalDate data, String categoria) {
        List<Quarto> disponiveis = new ArrayList<>();
        for (Quarto q : todosQuartos) {
            if (!q.getCategoria().equalsIgnoreCase(categoria)) continue;

            boolean ocupado = false;
            for (Reserva r : reservasAtivas.getElementosEmOrdem()) {
                if (r.getQuarto().equals(q)) {
                    if (!(data.isBefore(r.getDataCheckIn()) || data.isAfter(r.getDataCheckOut()))) {
                        ocupado = true;
                        break;
                    }
                }
            }

            if (!ocupado) {
                disponiveis.add(q);
            }
        }
        return disponiveis;
    }

    /**
     * Retorna o total de reservas ativas.
     *
     * @return total de reservas
     */
    public int totalReservasAtivas() {
        return reservasAtivas.getElementosEmOrdem().size();
    }
}
