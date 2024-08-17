package synchronization.exemple;

public class Packer implements Runnable {

    private final Team team;
    private final CounterSync packaging;
    private final String name;
    private String listThreadIds;

    public Packer(int numberPacker, Team team) {
        this.team = team;
        this.listThreadIds = new String();
        this.name = String.format("Packer[%d]@%s", numberPacker, team.getName());
        Thread.currentThread().setName(name);
        this.packaging = new CounterSync(0);
    }

    public void listPackaging() {
        System.out.println ( " |----- Empacotamentos feitos por " + name + " : " + packaging.getContador() );
    }

    public void listThreadIds() {
        System.out.println ( " |----- Lista de threads executadas por " + name + " : " + listThreadIds );
    }

    /**
     * Método que simula o empacotamento de um produto pelo empacotador.
     */
    @Override
    public void run() {
        try {
            synchronized(listThreadIds) { // Tornamos a operação a seguir atômica para evitar condição de corrida, mas neste caso não seria necessário, pois somente a thread corrente altera essa variável.
                listThreadIds += String.format("[%d] ", Thread.currentThread().getId());
            }
            System.out.printf("%s iniciou o empacotamento (%d).%n", name, System.currentTimeMillis());
            Thread.sleep((int)(Math.random() * 899 + 100)); // Coloca a thread para dormir por um tempo aleatório entre 100 e 999 milissegundos para simular um empacotamento real.
            System.out.printf("%s concluiu o empacotamento (%d).%n", name, System.currentTimeMillis());
            packaging.increment(); // Incrementa o contador de empacotamentos.
            team.releaseTapeAndFinishPackaging(); // Libera a fita utilizada e finaliza o empacotamento.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
