package synchronization.exemple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Company {

    private final Semaphore poolTapes; // Controla o acesso ao recurso crítico (fitas)
    private final PoolProducts poolProducts; // Produtos a serem empacotados
    private final List<Team> shiftTeams; // Equipes de trabalho por turno. Cada equipe possui 2 ou mais empacotadores e corresponde a uma thread.
    private final int poolPackers;
    private final int maxNumberTeams;
    private final int maxNumberProductsToBePackaged;
    private int numberOfProductsPackaged;

    public Company(int numberOfTapes, int poolPackers, int maxNumberTeams, int maxNumberProductsToBePackaged) {
        validate(numberOfTapes, poolPackers, maxNumberTeams, maxNumberProductsToBePackaged);
        this.poolTapes = new Semaphore(numberOfTapes);
        this.poolPackers = poolPackers;
        this.maxNumberTeams = maxNumberTeams;
        this.maxNumberProductsToBePackaged = maxNumberProductsToBePackaged;
        this.poolProducts = new PoolProducts(maxNumberProductsToBePackaged);
        this.shiftTeams = new ArrayList<>();
        this.numberOfProductsPackaged = 0;
        createTeams(numberOfTapes);
        shiftTeams.forEach(Thread::start); // Inicia todas as threads (equipes de trabalho)
        waitAllTeamsFinish(); // A thread principal deve aguardar o fim de todas as threads Equipe para poder contabilizar os empacotamentos.
        contabilizeProductsPackaged();
    }

    /**
     * Faz o join com todas as threads de equipes de trabalho.
     * Isso garante que a empresa só será encerrada quando todas as equipes terminarem o trabalho.
     */
    private void waitAllTeamsFinish() {
        shiftTeams.forEach(team -> {
            try {
                team.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Contabiliza os produtos empacotados.
     */
    private void contabilizeProductsPackaged() {
        shiftTeams.forEach(team -> numberOfProductsPackaged += team.getNumberOfProductsPackaged());
        System.out.println("Total de produtos empacotados: " + numberOfProductsPackaged);
    }

    private static void validate(int numberOfTapes, int poolPackers, int maxNumberTeams, int maxNumberProductsToBePackaged) {
        if (numberOfTapes < 1 || poolPackers < 2 || maxNumberTeams < 1 || maxNumberProductsToBePackaged < 1) {
            throw new IllegalArgumentException("All parameters must be greater than 0");
        }
    }

    /**
     * Cria as equipes de trabalho alocando os empacotadores e armazenando as equipes em "turno".
     *
     * @param numberOfTapes número de fitas disponíveis
     */
    private void createTeams(int numberOfTapes) {
        Team team;
        int numberOfPackersByTeam;
        int packersAvailable = poolPackers;
        int i = 1;

        do {
            numberOfPackersByTeam = composeNumbersOfPackersByTeam(packersAvailable, numberOfTapes);
            team = new Team(String.format("Team %d", i), numberOfPackersByTeam, poolTapes, poolProducts);
            shiftTeams.add(team);
            packersAvailable -= numberOfPackersByTeam;
            i++;
        } while ((i < maxNumberTeams) && (packersAvailable >= 2));

        createLastTeam(packersAvailable, numberOfTapes);
    }

    /**
     * Compor equipes de trabalho com 2 ou mais empacotadores.
     *
     * @param packersAvailable número de empacotadores disponíveis
     * @return número de empacotadores na equipe.
     */
    private int composeNumbersOfPackersByTeam(int packersAvailable, int numberOfTapes) {
        int numberOfPackersByTeam = packersAvailable < 2 ? packersAvailable : new Random().nextInt(poolPackers / maxNumberTeams) + 2;
        return Math.min(numberOfPackersByTeam, numberOfTapes);
    }

    /**
     * A última equipe recebe todos os empregados restantes.
     * Se ainda existirem empacotadores disponíveis E se o número de fitas disponíveis for MENOR do que o número de empacotadores disponíveis,
     * então a quantidade de empregados será igual a quantidade de fitas disponíveis.
     * Caso contrário, a equipe iria solicitar um número de permissões superior ao número de recursos do semáforo (número de fitas disponíveis) e ficaria bloqueada).
     */
    private void createLastTeam(int packersAvailable, int numberOfTapesAvailable) {
        if (packersAvailable > 0) {
            var lastPackers = Math.min(packersAvailable, numberOfTapesAvailable);
            Team team = new Team(String.format("Team %d", shiftTeams.size() + 1), lastPackers, poolTapes, poolProducts);
            shiftTeams.add(team);
        }
    }

}
