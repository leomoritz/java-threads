package synchronization.exemple;

/**
 * Essa classe foi construída pensando no uso compartilhado por diversas threads, e por isso o uso de “synchronized” se faz necessário.
 * Tomemos como exemplo o método “decrement”. A operação que esse método realiza é: “contador = contador - 1”. A ocorrência de mais de
 * uma chamada concorrente a esse método pode levar a uma condição de corrida, e, assim, usamos “synchronized” para impedir isso,
 * garantindo que somente uma execução do método ocorra ao mesmo tempo.
 */
public class CounterSync {

    private int counter;
    private final int start;

    public CounterSync(int start) {
        this.start = start;
        this.counter = start;
    }

    public synchronized void increment() {
        this.counter++;
    }

    public synchronized void increment(int numberToIncrement) {
        this.counter += numberToIncrement;
    }

    public synchronized void decrement() {
        this.counter--;
    }

    public synchronized void decrement(int numberToDecrement) {
        this.counter -= numberToDecrement;
    }

    public synchronized void resetCounterToBeggining() {
        this.counter = this.start;
    }

    public synchronized void resetCounter() {
        this.counter = 0;
    }

    public synchronized int getContador() {
        return this.counter;
    }
}
