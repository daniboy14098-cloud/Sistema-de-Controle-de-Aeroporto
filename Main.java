import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * Sistema de Controle de Aeroporto
 *
 * Estruturas de dados usadas:
 *  - Lista (ArrayList): voos cadastrados
 *  - Fila  (Queue/LinkedList): reservas pendentes (FIFO - ordem de chegada)
 *  - Pilha (Stack): passageiros que fizeram check-in (LIFO)
 */
public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Voo> voos = new ArrayList<>();
    static Queue<Reserva> filaPendentes = new LinkedList<>();
    static ArrayList<Reserva> confirmadas = new ArrayList<>();
    static Stack<Reserva> pilhaCheckIn = new Stack<>();

    static final String SENHA_ADMIN = "admin123";

    public static void main(String[] args) {
        voos.add(new Voo("2904", "Vitoria", "Sao Paulo", "08:00", "09:30", 3));
        voos.add(new Voo("1500", "Vitoria", "Rio de Janeiro", "10:15", "11:15", 2));
        voos.add(new Voo("3721", "Vitoria", "Brasilia", "14:40", "16:45", 5));

        int opcao;
        do {
            System.out.println("\n===== AEROPORTO =====");
            System.out.println("1 - Reservar voo");
            System.out.println("2 - Fazer check-in");
            System.out.println("3 - Ver informacoes dos voos");
            System.out.println("4 - Area do administrador");
            System.out.println("0 - Sair");
            opcao = lerInt("Escolha: ");

            switch (opcao) {
                case 1: reservarVoo(); break;
                case 2: fazerCheckIn(); break;
                case 3: exibirInformacoes(); break;
                case 4: areaAdmin(); break;
                case 0: System.out.println("Encerrando..."); break;
                default: System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void reservarVoo() {
        System.out.println("\n--- Reserva de voo ---");
        String numero = lerNumeroVoo();
        Voo voo = buscarVoo(numero);
        if (voo == null) {
            System.out.println("Voo nao encontrado.");
            return;
        }
        if (voo.estaCheio()) {
            System.out.println("Este voo esta cheio.");
            return;
        }

        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("Nome nao pode ser vazio.");
            return;
        }

        int idade = lerInt("Idade: ");
        if (idade <= 0 || idade > 120) {
            System.out.println("Idade invalida.");
            return;
        }

        System.out.print("CPF (11 numeros, sem pontos): ");
        String cpf = sc.nextLine().trim();
        if (!cpf.matches("\\d{11}")) {
            System.out.println("CPF invalido.");
            return;
        }

        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();
        if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            System.out.println("E-mail invalido.");
            return;
        }

        if (jaTemReserva(cpf, numero)) {
            System.out.println("Este CPF ja possui reserva neste voo.");
            return;
        }

        filaPendentes.add(new Reserva(nome, idade, cpf, email, numero));
        System.out.println("Reserva solicitada! Aguardando confirmacao do administrador.");
    }

    static boolean jaTemReserva(String cpf, String numeroVoo) {
        for (Reserva r : filaPendentes) {
            if (r.cpf.equals(cpf) && r.numeroVoo.equals(numeroVoo)) return true;
        }
        for (Reserva r : confirmadas) {
            if (r.cpf.equals(cpf) && r.numeroVoo.equals(numeroVoo)) return true;
        }
        return false;
    }

    static void fazerCheckIn() {
        System.out.println("\n--- Check-in ---");
        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        String numero = lerNumeroVoo();

        for (Reserva r : confirmadas) {
            if (r.cpf.equals(cpf) && r.numeroVoo.equals(numero)) {
                if (r.checkInFeito) {
                    System.out.println("Check-in ja realizado.");
                    return;
                }
                r.checkInFeito = true;
                pilhaCheckIn.push(r);
                System.out.println("Check-in realizado com sucesso, " + r.nome + "!");
                return;
            }
        }
        System.out.println("Nenhuma reserva CONFIRMADA encontrada para esse CPF e voo.");
    }

    static void exibirInformacoes() {
        System.out.println("\n--- Voos disponiveis (com vagas) ---");
        boolean achou = false;
        for (Voo v : voos) {
            if (!v.estaCheio()) {
                System.out.println(v);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhum.");

        System.out.println("\n--- Voos com reservas pendentes ---");
        achou = false;
        for (Voo v : voos) {
            int pendentes = contarPendentes(v.numero);
            if (pendentes > 0) {
                System.out.println(v + " | Pendentes: " + pendentes);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhum.");

        System.out.println("\n--- Voos cheios ---");
        achou = false;
        for (Voo v : voos) {
            if (v.estaCheio()) {
                System.out.println(v);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhum.");
    }

    static int contarPendentes(String numeroVoo) {
        int cont = 0;
        for (Reserva r : filaPendentes) {
            if (r.numeroVoo.equals(numeroVoo)) cont++;
        }
        return cont;
    }

    static void areaAdmin() {
        System.out.print("Senha do administrador: ");
        String senha = sc.nextLine();
        if (!senha.equals(SENHA_ADMIN)) {
            System.out.println("Senha incorreta.");
            return;
        }

        int opcao;
        do {
            System.out.println("\n===== ADMINISTRADOR =====");
            System.out.println("1 - Processar reservas pendentes");
            System.out.println("2 - Cadastrar novo voo");
            System.out.println("3 - Ver pilha de check-in");
            System.out.println("4 - Chamar proximo para embarque (desempilhar)");
            System.out.println("0 - Voltar");
            opcao = lerInt("Escolha: ");

            switch (opcao) {
                case 1: processarReservas(); break;
                case 2: cadastrarVoo(); break;
                case 3: mostrarPilha(); break;
                case 4: embarcar(); break;
                case 0: break;
                default: System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void processarReservas() {
        while (!filaPendentes.isEmpty()) {
            Reserva r = filaPendentes.peek();
            Voo voo = buscarVoo(r.numeroVoo);

            System.out.println("\nProxima reserva da fila:");
            System.out.println(r);
            System.out.println(voo);
            System.out.println("1 - Confirmar | 2 - Recusar | 0 - Parar");
            int op = lerInt("Escolha: ");

            if (op == 1) {
                if (voo.estaCheio()) {
                    System.out.println("Voo cheio! Nao e possivel confirmar. Recuse a reserva.");
                } else {
                    filaPendentes.poll();
                    voo.confirmados++;
                    confirmadas.add(r);
                    System.out.println("Reserva confirmada.");
                }
            } else if (op == 2) {
                filaPendentes.poll();
                System.out.println("Reserva recusada.");
            } else if (op == 0) {
                return;
            } else {
                System.out.println("Opcao invalida!");
            }
        }
        System.out.println("Nao ha reservas pendentes.");
    }

    static void cadastrarVoo() {
        System.out.println("\n--- Cadastro de voo ---");
        String numero = lerNumeroVoo();
        if (buscarVoo(numero) != null) {
            System.out.println("Ja existe um voo com esse numero.");
            return;
        }
        System.out.print("Origem: ");
        String origem = sc.nextLine().trim();
        System.out.print("Destino: ");
        String destino = sc.nextLine().trim();
        System.out.print("Horario de partida (HH:mm): ");
        String partida = sc.nextLine().trim();
        System.out.print("Horario de chegada (HH:mm): ");
        String chegada = sc.nextLine().trim();
        int capacidade = lerInt("Quantidade maxima de passageiros: ");

        String horaRegex = "([01]\\d|2[0-3]):[0-5]\\d";
        if (origem.isEmpty() || destino.isEmpty()
                || !partida.matches(horaRegex) || !chegada.matches(horaRegex)
                || capacidade <= 0) {
            System.out.println("Dados invalidos. Voo nao cadastrado.");
            return;
        }

        voos.add(new Voo(numero, origem, destino, partida, chegada, capacidade));
        System.out.println("Voo cadastrado!");
    }

    static void mostrarPilha() {
        if (pilhaCheckIn.isEmpty()) {
            System.out.println("Nenhum passageiro fez check-in ainda.");
            return;
        }
        System.out.println("\nPilha de check-in (topo primeiro):");
        for (int i = pilhaCheckIn.size() - 1; i >= 0; i--) {
            System.out.println("- " + pilhaCheckIn.get(i));
        }
    }

    static void embarcar() {
        if (pilhaCheckIn.isEmpty()) {
            System.out.println("Pilha vazia.");
            return;
        }
        Reserva r = pilhaCheckIn.pop();
        System.out.println("Embarcando: " + r);
    }

    static Voo buscarVoo(String numero) {
        for (Voo v : voos) {
            if (v.numero.equals(numero)) return v;
        }
        return null;
    }

    static String lerNumeroVoo() {
        String numero;
        do {
            System.out.print("Numero do voo (4 algarismos): ");
            numero = sc.nextLine().trim();
            if (!numero.matches("\\d{4}")) {
                System.out.println("Numero invalido! Use exatamente 4 algarismos.");
            }
        } while (!numero.matches("\\d{4}"));
        return numero;
    }

    static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }
}
