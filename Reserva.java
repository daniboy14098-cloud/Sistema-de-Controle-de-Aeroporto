/**
 * Representa a reserva de um passageiro em um voo.
 * Enquanto não confirmada, fica na FILA de reservas pendentes.
 * Depois de confirmada pelo administrador, o passageiro pode fazer check-in
 * e então vai para a PILHA de check-in.
 */
public class Reserva {
    String nome;
    int idade;
    String cpf;
    String email;
    String numeroVoo;
    boolean checkInFeito;

    public Reserva(String nome, int idade, String cpf, String email, String numeroVoo) {
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
        this.email = email;
        this.numeroVoo = numeroVoo;
        this.checkInFeito = false;
    }

    public String toString() {
        return nome + " (" + idade + " anos) | CPF: " + cpf
                + " | E-mail: " + email + " | Voo: " + numeroVoo;
    }
}
