package relatorios;

import modelos.Reserva;
import modelos.Quarto;
import historico.HistoricoReservas;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe responsável por gerar relatórios gerenciais do hotel.
 */
public class RelatoriosGerenciais {

    /**
     * Calcula a taxa de ocupação de quartos em um período específico.
     *
     * @param todasReservas Lista de reservas ativas
     * @param todosQuartos  Lista de todos os quartos
     * @param dataInicio    Data de início do período
     * @param dataFim       Data de fim do período
     * @return percentual de ocupação
     */
    public static double calcularTaxaOcupacao(List<Reserva> todasReservas, List<Quarto> todosQuartos,
                                               LocalDate dataInicio, LocalDate dataFim) {
        if (todosQuartos.isEmpty()) return 0.0;

        int ocupados = 0;
        for (Quarto q : todosQuartos) {
            boolean estaOcupado = false;
            for (Reserva r : todasReservas) {
                // Se houver sobreposição entre reserva e período
                if (r.getQuarto().equals(q) &&
                        !(r.getDataCheckOut().isBefore(dataInicio) || r.getDataCheckIn().isAfter(dataFim))) {
                    estaOcupado = true;
                    break;
                }
            }
            if (estaOcupado) ocupados++;
        }

        return ((double) ocupados / todosQuartos.size()) * 100;
    }

    /**
     * Lista os quartos mais reservados.
     *
     * @param todasReservas Lista de reservas ativas
     * @param topN          Número de quartos a exibir
     * @return Lista de quartos ordenados do mais reservado para menos
     */
    public static List<Map.Entry<Quarto, Integer>> quartosMaisReservados(List<Reserva> todasReservas, int topN) {
        Map<Quarto, Integer> contagem = new HashMap<>();
        for (Reserva r : todasReservas) {
            contagem.put(r.getQuarto(), contagem.getOrDefault(r.getQuarto(), 0) + 1);
        }
        return contagem.entrySet().stream()
                .sorted(Map.Entry.<Quarto, Integer>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * Lista os quartos menos reservados.
     *
     * @param todasReservas Lista de reservas ativas
     * @param todosQuartos  Lista completa de quartos
     * @param topN          Número de quartos a exibir
     * @return Lista de quartos ordenados do menos reservado para mais
     */
    public static List<Map.Entry<Quarto, Integer>> quartosMenosReservados(List<Reserva> todasReservas,
                                                                          List<Quarto> todosQuartos, int topN) {
        Map<Quarto, Integer> contagem = new HashMap<>();
        for (Quarto q : todosQuartos) {
            contagem.put(q, 0);
        }
        for (Reserva r : todasReservas) {
            contagem.put(r.getQuarto(), contagem.getOrDefault(r.getQuarto(), 0) + 1);
        }
        return contagem.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * Conta o número de reservas canceladas em um período específico.
     *
     * @param historico   Histórico de reservas canceladas
     * @param dataInicio  Data de início
     * @param dataFim     Data de fim
     * @return número de cancelamentos
     */
    public static int contarCancelamentos(HistoricoReservas historico, LocalDate dataInicio, LocalDate dataFim) {
        int total = 0;
        for (Reserva r : historico.listarReservasCanceladas()) {
            if (!r.getDataCheckIn().isBefore(dataInicio) && !r.getDataCheckIn().isAfter(dataFim)) {
                total++;
            }
        }
        return total;
    }

    /**
     * Verifica se a ocupação ultrapassa o limite e gera alerta.
     *
     * @param todasReservas Lista de reservas ativas
     * @param todosQuartos  Lista completa de quartos
     * @param data          Data a ser verificada
     * @param limitePercentual Limite de ocupação (ex: 90.0)
     * @return mensagem de alerta ou vazio
     */
    public static String alertaCapacidade(List<Reserva> todasReservas, List<Quarto> todosQuartos,
                                          LocalDate data, double limitePercentual) {
        double ocupacao = calcularTaxaOcupacao(todasReservas, todosQuartos, data, data);
        if (ocupacao >= limitePercentual) {
            return "ALERTA: Ocupação alta em " + data + " (" + String.format("%.2f", ocupacao) + "%)";
        }
        return "";
    }
}
