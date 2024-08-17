package definition;

/**
 * Definindo Thread por extensão da classe "Thread"
 */
public class ThreadInheritance extends Thread {

    long numero;

    public ThreadInheritance(long numero) {
        this.numero = numero;
    }

    @Override
    public void run() {
        System.out.println("Thread " + numero + " executando");
    }

}
