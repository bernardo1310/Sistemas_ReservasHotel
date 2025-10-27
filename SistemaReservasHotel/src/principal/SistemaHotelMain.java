package principal;

import gerenciador.GerenciadorReservas;
import historico.HistoricoReservas;
import modelos.Cliente;
import modelos.Quarto;
import modelos.Reserva;
import relatorios.RelatoriosGerenciais;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe principal do sistema de gerenciamento de reservas de hotel.
 */
public class SistemaHotelMain {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static HistoricoReservas historico = new HistoricoReservas();
    private static GerenciadorReservas gerenciador = new GerenciadorReservas(historico);

    // Lista de quartos disponíveis no hotel
    private static List<Quarto> todosQuartos = new ArrayList<>();

    public static void main(String[] args) {

        // Inicializa alguns quartos de exemplo
        inicializarQuartos();

        boolean sair = false;
        while (!sair) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> cadastrarReserva();
                case 2 -> cancelarReserva();
                case 3 -> consultarReservaPorCliente();
                case 4 -> listarReservas();
                case 5 -> listarQuartosDisponiveis();
                case 6 -> gerarRelatorios();
                case 0 -> {
                    System.out.println("Encerrando o sistema...");
                    sair = true;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE RESERVAS ===");
        System.out.println("1 - Cadastrar nova reserva");
        System.out.println("2 - Cancelar reserva");
        System.out.println("3 - Consultar reserva por cliente");
        System.out.println("4 - Listar todas as reservas");
        System.out.println("5 - Listar quartos disponíveis");
        System.out.println("6 - Relatórios gerenciais");
        System.out.println("0 - Sair");
    }

    private static void inicializarQuartos() {
        todosQuartos.add(new Quarto(101, "Economico"));
        todosQuartos.add(new Quarto(102, "Economico"));
        todosQuartos.add(new Quarto(201, "Luxo"));
        todosQuartos.add(new Quarto(202, "Luxo"));
        todosQuartos.add(new Quarto(301, "Suite"));
    }

    private static void cadastrarReserva() {
        System.out.println("\n--- Cadastrar Nova Reserva ---");

        String nome = lerTexto("Nome do cliente: ");
        String cpf = lerTexto("CPF do cliente: ");
        Cliente cliente = new Cliente(nome, cpf);

        int numeroQuarto = lerInteiro("Número do quarto: ");
        Quarto quarto = todosQuartos.stream()
                .filter(q -> q.getNumero() == numeroQuarto)
                .findFirst()
                .orElse(null);

        if (quarto == null) {
            System.out.println("Quarto inválido.");
            return;
        }

        LocalDate checkIn = lerData("Data de check-in (dd/MM/yyyy): ");
        LocalDate checkOut = lerData("Data de check-out (dd/MM/yyyy): ");

        Reserva reserva = new Reserva(cliente, quarto, checkIn, checkOut);

        gerenciador.cadastrarReserva(reserva);
    }

    private static void cancelarReserva() {
        System.out.println("\n--- Cancelar Reserva ---");
        String cpf = lerTexto("CPF do cliente: ");
        Reserva r = gerenciador.consultarReservaPorCliente(cpf);
        if (r != null) {
            gerenciador.cancelarReserva(r);
        } else {
            System.out.println("Reserva não encontrada para o CPF informado.");
        }
    }

    private static void consultarReservaPorCliente() {
        System.out.println("\n--- Consultar Reserva ---");
        String cpf = lerTexto("CPF do cliente: ");
        Reserva r = gerenciador.consultarReservaPorCliente(cpf);
        if (r != null) {
            System.out.println("Reserva encontrada: " + r);
        } else {
            System.out.println("Nenhuma reserva encontrada para este cliente.");
        }
    }

    private static void listarReservas() {
        System.out.println("\n--- Listar Reservas ---");
        List<Reserva> reservas = gerenciador.listarReservas();
        if (reservas.isEmpty()) {
            System.out.println("Não há reservas ativas.");
        } else {
            for (Reserva r : reservas) {
                System.out.println(r);
            }
        }
    }

    private static void listarQuartosDisponiveis() {
        System.out.println("\n--- Quartos Disponíveis ---");
        String categoria = lerTexto("Categoria desejada: ");
        LocalDate data = lerData("Data desejada (dd/MM/yyyy): ");

        List<Quarto> disponiveis = gerenciador.listarQuartosDisponiveis(todosQuartos, data, categoria);
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum quarto disponível para esta data e categoria.");
        } else {
            System.out.println("Quartos disponíveis:");
            for (Quarto q : disponiveis) {
                System.out.println(q);
            }
        }
    }

    private static void gerarRelatorios() {
        System.out.println("\n--- Relatórios Gerenciais ---");
        LocalDate inicio = lerData("Data início do período (dd/MM/yyyy): ");
        LocalDate fim = lerData("Data fim do período (dd/MM/yyyy): ");

        double taxaOcupacao = RelatoriosGerenciais.calcularTaxaOcupacao(
                gerenciador.listarReservas(), todosQuartos, inicio, fim);
        System.out.println("Taxa de ocupação: " + String.format("%.2f", taxaOcupacao) + "%");

        int cancelamentos = RelatoriosGerenciais.contarCancelamentos(historico, inicio, fim);
        System.out.println("Número de cancelamentos: " + cancelamentos);

        System.out.println("\nTop 3 quartos mais reservados:");
        RelatoriosGerenciais.quartosMaisReservados(gerenciador.listarReservas(), 3)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue() + " reservas"));

        System.out.println("\nTop 3 quartos menos reservados:");
        RelatoriosGerenciais.quartosMenosReservados(gerenciador.listarReservas(), todosQuartos, 3)
                .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue() + " reservas"));

        // Alertas de capacidade
        LocalDate dataAlerta = lerData("Verificar alerta de ocupação em (dd/MM/yyyy): ");
        String alerta = RelatoriosGerenciais.alertaCapacidade(gerenciador.listarReservas(), todosQuartos, dataAlerta, 90.0);
        if (!alerta.isEmpty()) {
            System.out.println(alerta);
        } else {
            System.out.println("Ocupação dentro do limite.");
        }
    }

    // ==========================
    // ===== MÉTODOS AUXILIARES ==
    // ==========================

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número.");
            }
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return LocalDate.parse(scanner.nextLine(), formatter);
            } catch (Exception e) {
                System.out.println("Data inválida. Digite no formato dd/MM/yyyy.");
            }
        }
    }
}
