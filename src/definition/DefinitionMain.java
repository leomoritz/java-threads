package definition;

public class DefinitionMain {

    public static void main(String[] args) {
        // Extensão de Thread
        ThreadInheritance newThread = new ThreadInheritance(200);
        newThread.start();

        // Implementação de Runnable
        ThreadRunnableImpl newRunnableThread = new ThreadRunnableImpl(200);
        new Thread(newRunnableThread).start();
    }

}
