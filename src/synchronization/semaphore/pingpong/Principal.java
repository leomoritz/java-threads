package synchronization.semaphore.pingpong;

public class Principal {

    private static PingPong partida;

    public static void main(String[] args) throws InterruptedException {
        partida = new PingPong(8);
    }

}
