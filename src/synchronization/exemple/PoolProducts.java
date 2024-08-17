package synchronization.exemple;

public class PoolProducts extends CounterSync {

    public PoolProducts(int numberOfProducts) {
        super(numberOfProducts);
        if (numberOfProducts < 1) {
            throw new IllegalArgumentException("The number of products must be greater than 0");
        }
    }

    /**
     * Retorna o número de produtos solicitados. Se não houver produtos suficientes, retorna o número de produtos disponíveis e zera o estoque.
     *
     * @param numberOfProducts número de produtos solicitados
     * @return número de produtos solicitados ou disponíveis
     */
    public synchronized int getProducts(int numberOfProducts) {
        int aux = getContador();

        if ((aux - numberOfProducts) >= 0) {
            decrement(numberOfProducts);
            return numberOfProducts;
        }

        resetCounter();
        return aux;
    }
}
