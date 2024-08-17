package synchronization.semaphore;

import java.util.concurrent.Semaphore;

public class ExempleDefinition {

    public void run() throws InterruptedException {
        Semaphore semaphore = new Semaphore(50, true); // Define até 50 threads simultâneas e o uso de FIFO.
        semaphore.acquire(); // Solicita 1 acesso ao semáforo.
        // ... Região crítica
        semaphore.release(); // Libera 1 acesso ao semáforo.
        // ... código não crítico.
        semaphore.acquire(4); // Solicita 4 acessos
        // ... Região crítica
        semaphore.release(4); // Libera 4 acessos
        // ... código não crítico.
    }

}
