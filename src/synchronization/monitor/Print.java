package synchronization.monitor;

public class Print {

    public void imprime() {
        System.out.println("Em Java, todo objeto possui um “wait-set” associado que implementa o conceito de conjunto de threads. " //
                                   + "Essa estrutura é utilizada para permitir a cooperação entre as threads, fornecendo os seguintes métodos:" //
                                   + "\n wait: suspende a thread atual e libera o monitor associado ao objeto. A thread é colocada no conjunto de espera do objeto." //
                                   + "\n notify: acorda uma thread do conjunto de espera do objeto. A thread acordada não pode prosseguir até que a thread que chamou notify libere o monitor." //
                                   + "\n notifyAll: acorda todas as threads do conjunto de espera do objeto. A thread acordada não pode prosseguir até que a thread que chamou notifyAll libere o monitor.");
    }
}
