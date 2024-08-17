package synchronization.semaphore.pingpong;

import java.util.concurrent.Semaphore;

import synchronization.monitor.Controle;

public class Pong implements Runnable {

    // Atributos
    private Semaphore semaphore1, semaphore2;
    private Controle contador;

    // Métodos
    public Pong(Semaphore semaphore1, Semaphore semaphore2, Controle contador) {
        this.semaphore1 = semaphore1;
        this.semaphore2 = semaphore2;
        this.contador = contador;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread Pong iniciou!");
            while (contador.getControle() > 0) {
                semaphore2.acquire();
                System.out.println("0 <= PONG");
                semaphore1.release();
                contador.decrementa();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("A Thread Pong finalizou!");
    }
}
