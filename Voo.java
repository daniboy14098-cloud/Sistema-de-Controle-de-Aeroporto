/**
 * Representa um voo do aeroporto.
 * Os voos ficam guardados em uma lista (ArrayList) na classe Main.
 */
public class Voo {
    String numero;      // 4 algarismos, ex: "2904"
    String origem;
    String destino;
    String partida;     // formato HH:mm
    String chegada;     // formato HH:mm
    int capacidade;     // quantidade máxima de passageiros
    int confirmados;    // quantos passageiros já têm reserva confirmada

    public Voo(String numero, String origem, String destino,
               String partida, String chegada, int capacidade) {
        this.numero = numero;
        this.origem = origem;
        this.destino = destino;
        this.partida = partida;
        this.chegada = chegada;
        this.capacidade = capacidade;
        this.confirmados = 0;
    }

    public boolean estaCheio() {
        return confirmados >= capacidade;
    }

    public String toString() {
        return "Voo " + numero + " | " + origem + " -> " + destino
                + " | Partida: " + partida + " | Chegada: " + chegada
                + " | Vagas: " + (capacidade - confirmados) + "/" + capacidade;
    }
}
