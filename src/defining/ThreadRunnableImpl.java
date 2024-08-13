package defining;

public class ThreadRunnableImpl implements Runnable {

    long numero;

    public ThreadRunnableImpl(long numero) {
        this.numero = numero;
    }

    @Override
    public void run() {
        System.out.println("Thread " + numero + " executando");
    }
}
