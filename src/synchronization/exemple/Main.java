package synchronization.exemple;

public class Main {

    private static Company DEVTHREAD;

    public static void main(String[] args) {
        // Empresa(número de fitas, empregados disponíveis, número máximo de equipes, número máximo de produtos a serem empacotados)
        DEVTHREAD = new Company(20, 25, 4, 200);
    }

}
