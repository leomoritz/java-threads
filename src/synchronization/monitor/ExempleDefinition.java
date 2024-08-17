package synchronization.monitor;

public class ExempleDefinition {

    private int conta;
    private Print ex = new Print();

    /**
     * Método sincronizado.
     * Quando um método sincronizado (synchronized) é invocado, ele automaticamente dá início ao travamento da região crítica.
     * A execução do método não começa até que o bloqueio tenha sido garantido. Uma vez terminado, mesmo que o método tenha sido
     * encerrado anormalmente, o travamento é liberado. É importante perceber que quando se trata de um método de instância,
     * o travamento é feito no monitor associado àquela instância. Em oposição, métodos “static” realizam o travamento do monitor
     * associado ao objeto “Class”, representativo da classe na qual o método foi definido (ORACLE AMERICA INC., s.d.).
     */
    public synchronized void decrementa() {
        conta--;
    }

    public void impressao() {
        // Região de código sincronizada
        synchronized(ex) {
            ex.imprime(); // invoca o método "imprime() do objeto "ex" de maneira sincronizada.
        }
    }

}
