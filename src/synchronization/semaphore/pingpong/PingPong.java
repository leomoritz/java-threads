package synchronization.semaphore.pingpong;

import java.util.concurrent.Semaphore;

import synchronization.monitor.Controle;

public class PingPong {

    private Semaphore semaphore1, semaphore2;
    private Ping ping;
    private Pong pong;
    private Controle contador;
    private int tamanhoPartida;

    // Métodos
    public PingPong(int tamanhoPartida) throws InterruptedException {
        this.semaphore1 = new Semaphore(0); // iniciado em zero para bloquear após o início
        this.semaphore2 = new Semaphore(0); // iniciado em zero para bloquear após o início
        this.contador = new Controle(tamanhoPartida);
        this.ping = new Ping(semaphore1, semaphore2, contador);
        this.pong = new Pong(semaphore1, semaphore2, contador);
        // juiz = new Juiz(tamanhoPartida / 2);
        new Thread(ping).start();
        new Thread(pong).start();
        semaphore1.release(); // desbloqueia a primeira thread
    }

}
