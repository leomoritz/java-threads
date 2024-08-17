package synchronization.exemple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Team extends Thread {

    private CountDownLatch latch; // Vamos usá-lo para controlar o bloqueio da thread corrente de equipe.
    private final int numberOfPackersByTeam;
    private final Semaphore poolTapes;
    private final PoolProducts poolProducts;
    private final List<Packer> packers;
    private final CounterSync productsPackagedOfTeam;

    public Team(String name, int numberOfPackersByTeam, Semaphore poolTapes, PoolProducts poolProducts) {
        this.setName(name);
        this.numberOfPackersByTeam = numberOfPackersByTeam;
        this.poolTapes = poolTapes;
        this.poolProducts = poolProducts;
        this.packers = new ArrayList<>();
        this.productsPackagedOfTeam = new CounterSync(0);
        preparePackers();
    }

    /**
     * Adiciona um empacotador à equipe.
     */
    private void preparePackers() {
        for (int i = 1; i <= numberOfPackersByTeam; i++) {
            var packer = new Packer(i, this);
            packers.add(packer);
        }
    }

    /**
     * Obtém o número de produtos empacotados pela equipe.
     *
     * @return número de produtos empacotados pela equipe.
     */
    public synchronized int getNumberOfProductsPackaged() {
        return productsPackagedOfTeam.getContador();
    }

    /**
     * Para cada empregador, se houver pacote disponível para empacotar, decrementa pool_pacotes, dispara uma
     * thread (Empacotador) para realizar o trabalho de empacotamento. Do contrário, não faz nada.
     *
     * @return Se for possível retirar um pacote para cada (Empacotador), retorna true, caso contrário, retorna false.
     */
    private boolean pack(int numberOfPackersByTeam) {
        Thread thread;
        int threadCreated = poolProducts.getProducts(numberOfPackersByTeam);

        if (threadCreated == 0) { // Caso não houver produtos para empacotamento
            releaseUnnecessaryTapes(numberOfPackersByTeam);
            return false;
        }

        for (int i = 1; i <= threadCreated; i++) {
            thread = new Thread(packers.get(i - 1));
            thread.setPriority(Thread.currentThread().getPriority() + 2);
            thread.start();
        }

        releaseUnnecessaryTapes(numberOfPackersByTeam - threadCreated); // Devolve todas as fitas excedentes
        return block(); // Bloqueia a thread (equipe) até que todos os empacotadores terminem o trabalho.
    }

    /**
     * Libera as fitas que não foram utilizadas.
     * Como? Realiza atomicamente as operações de decremento do latch e libera as travas (número de fitas excedentes) sobre o semáforo poolTapes.
     *
     * @param numberToRelease número de fitas a serem liberadas.
     */
    public synchronized void releaseUnnecessaryTapes(int numberToRelease) {
        poolTapes.release(numberToRelease); // libera a trava do semáforo para disponibilizar as fitas para outras equipes.
        while (numberToRelease > 0) { // Quando cada thread correspondente a “Empacotador” finaliza, “latch” é decrementado. Quando o contador zera, a thread de “Equipe” desbloqueia.
            latch.countDown();
            numberToRelease--;
        }
    }

    /**
     * Libera a fita utilizada.
     * Como? Realiza atomicamente as operações de decremento do latch e libera a trava sobre o semáforo poolTapes.
     * Além disso, finaliza o empacotamento incrementando o contador de produtos empacotados pela equipe.
     */
    public synchronized void releaseTapeAndFinishPackaging() {
        latch.countDown();
        productsPackagedOfTeam.increment();
        poolTapes.release();
    }

    /**
     * Realiza o bloqueio da thread (equipe) até que todos os empacotadores terminem o trabalho.
     *
     * @return true se todos os empacotadores terminaram o trabalho, caso contrário, retorna false.
     */
    private boolean block() {
        try {
            latch.await(); // Bloqueia a thread (equipe) até que todos os empacotadores terminem o trabalho.
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Relatório final da equipe.
     */
    private synchronized void report() {
        System.out.println("\n/----------------------------------------\\");
        System.out.println(getName() + " (thread: " + Thread.currentThread().getId() + ") FINALIZOU");
        System.out.println(" |- Nr Integrantes: " + this.numberOfPackersByTeam);
        System.out.println(" |- Empacotamentos da equipe: " + this.productsPackagedOfTeam.getContador());
        System.out.println(" |- Empacotamentos por integrante:");
        packers.forEach(Packer::listPackaging);
        System.out.println(" |- Threads por objeto Empacotador:");
        packers.forEach(Packer::listThreadIds);
        System.out.println("\\----------------------------------------/\n");
    }

    @Override
    public void run() {
        try {
            boolean control;
            System.out.println(getName() + " iniciou o turno.");
            do {
                poolTapes.acquire(numberOfPackersByTeam);
                this.latch = new CountDownLatch(numberOfPackersByTeam);
                control = pack(numberOfPackersByTeam);
            } while (control);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        report();
    }
}
