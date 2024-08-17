package synchronization.monitor;

public class Controle {

    // Atributos
    private int controle;

    // Métodos
    public Controle(int controle) {
        this.controle = controle;
    }

    // A classe e atributo controle não são imutáveis (final) e por este motivo este método precisa ser synchronized.
    public synchronized int getControle() {
        return controle;
    }

    // A classe e atributo controle não são imutáveis (final) e por este motivo este método precisa ser synchronized.
    public synchronized void decrementa() {
        controle--;
    }

    public void incrementa() {
        controle++;
    }

}
