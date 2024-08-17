# Threads e Processos

## Conceito de Processos

Um **processo** é uma instância de um programa em execução. Ele contém o código do programa, seus dados e o estado do processador (registradores e outras informações de execução). Em um sistema operacional multitarefa, vários processos podem ser executados simultaneamente. Cada processo tem seu próprio espaço de memória, o que significa que os processos são isolados uns dos outros, garantindo que um processo não possa acessar diretamente a memória de outro.

### Características dos Processos:
- **Independência:** Cada processo opera em seu próprio espaço de memória.
- **Isolamento:** Processos não compartilham dados diretamente.
- **Overhead:** A criação e gestão de processos pode ser cara em termos de recursos, devido à necessidade de manutenção de espaços de memória separados e à comunicação entre processos.

## Conceito de Threads

Uma **thread** (ou linha de execução) é a menor unidade de execução em um processo. Diferente dos processos, várias threads dentro do mesmo processo compartilham o mesmo espaço de memória. Isso facilita a comunicação entre threads, mas também pode levar a problemas de concorrência, como condições de corrida e deadlocks, se não forem gerenciadas corretamente.

### Características das Threads:
- **Compartilhamento:** Threads compartilham o espaço de memória e recursos do processo pai.
- **Leveza:** Threads são mais leves do que processos e têm menos overhead.
- **Concorrência:** Várias threads podem ser executadas simultaneamente, permitindo que diferentes partes de um programa sejam processadas em paralelo.

## Threads em Java

Java fornece suporte robusto para threads, permitindo a criação e gestão de threads de maneira fácil. 

Em java, há dois tipos de threads:
- **Daemon Threads:** São threads em segundo plano que são executadas em segundo plano e são usadas para tarefas de manutenção, como coleta de lixo. Se todas as threads não-daemon terminarem, o programa será encerrado, independentemente do estado das threads daemon.
- **User:** Threads de usuário são criadas pela aplicação e finalizadas por ela. A JVM não força sua finalização e aguardará que as threads completem suas tarefas. Esse tipo de thread executa em primeiro plano e possui prioridades mais altas que as daemon threads. Isso não permite ao usuário ter certeza de quando sua thread entrará em execução, por isso mecanismos adicionais precisam ser usados para garantir a sincronicidade entre as threads.

Quando a JVM inicia, normalmente há apenas uma thread não daemon, que tipicamente chama o método “main” das classes designadas. A MVJ continua a executar threads até que o método “exit” da classe “Runtime” é chamado e o gerenciador de segurança permite a saída ou até que todas as threads que não são daemon estejam mortas (ORACLE AMERICA INC., s.d.).

Em Java, uma thread pode ser criada de duas maneiras principais:

1. **Implementando a interface `Runnable`:** A interface `Runnable` tem um único método `run()`, que deve ser implementado pela classe. Esta abordagem é útil quando a classe já está estendendo outra classe e não pode estender `Thread`.

2. **Estendendo a classe `Thread`:** A classe `Thread` tem o método `run()`, que pode ser sobrescrito para definir o comportamento da thread. Esta abordagem é útil quando não há necessidade de estender outra classe.

Toda thread possui um nome, mesmo que ele não seja especificado. Se não for especificado, a JVM atribuirá um nome padrão a ela.

Uma thread pode existir em 6 estados diferentes:
- **New:** O estado inicial de uma thread após ser criada, mas antes de ser iniciada com o método `start()`. Nesse estado, a thread ainda não começou a executar.
- **Runnable:** Uma vez que a thread é iniciada com o método `start()`, ela entra no estado **Runnable**. Neste estado, a thread está apta a ser executada, mas pode não estar sendo executada imediatamente, pois a decisão de quando executar a thread é gerenciada pelo agendador de threads da JVM.
- **Blocked:** Uma thread entra no estado Blocked quando está tentando adquirir um monitor de objeto que já está sendo mantido por outra thread. Isso ocorre geralmente quando a thread está tentando acessar um recurso sincronizado que está ocupado.
- **Waiting:** Uma thread entra no estado Waiting quando está esperando indefinidamente por outra thread para realizar uma ação. Isso pode ocorrer quando a thread está esperando por outra thread para liberar um recurso ou notificar que uma condição foi atendida. Isso acontece quando a thread chama métodos como `Object.wait()`, `Thread.join()`, ou `LockSupport.park()` e não há um limite de tempo definido.
- **Time Waiting:** Uma thread entra no estado Timed Waiting quando ela está esperando um período específico de tempo para uma condição ser satisfeita. Isso ocorre quando a thread chama métodos como `Thread.sleep()` ou `Object.wait(long timeout)`.
- **Terminated:** Uma thread entra no estado Terminated quando ela termina sua execução. Isso pode ocorrer quando a thread completa a execução do método `run()` ou quando uma exceção não tratada é lançada.
### Criando Threads em Java

#### 1. Implementando a Interface `Runnable`

```java
public class MinhaRunnable implements Runnable {
    @Override
    public void run() {
        // Código que a thread irá executar
        for(int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " está executando: " + i);
        }
    }
    
    public static void main(String[] args) {
        MinhaRunnable minhaRunnable = new MinhaRunnable();
        Thread thread = new Thread(minhaRunnable);
        thread.start();  // Inicia a thread
    }
}
```

#### 2. Estendendo a Classe `Thread`

```java
public class MinhaThread extends Thread {
    @Override
    public void run() {
        // Código que a thread irá executar
        for(int i = 0; i < 5; i++) {
            System.out.println(getName() + " está executando: " + i);
        }
    }
    
    public static void main(String[] args) {
        MinhaThread thread = new MinhaThread();
        thread.start();  // Inicia a thread
    }
}
```

### Considerações sobre Threads em Java
- **Sincronização:** Quando várias threads acessam os mesmos recursos, é importante sincronizá-las para evitar condições de corrida.
- **Executores:** Java fornece a API Executors para gerenciamento mais eficiente de threads, como o uso de pools de threads.

## Conclusão
A programação paralela é desafiadora. É fácil pensar de maneira sequencial, com todas as instruções ocorrendo de forma encadeada ao longo de uma única linha de execução, mas quando o programa envolve múltiplas linhas que se entrecruzam, a situação suscita problemas inexistentes no caso de uma única linha.

A chamada condição de corrida, frequentemente, se faz presente, exigindo do programador uma atenção especial. Vimos os mecanismos que Java oferece para permitir a sincronização de threads, mas esses mecanismos precisam ser apropriadamente empregados. Dependendo do tamanho do programa e do número de threads, controlar essa dinâmica mentalmente é desejar o erro.

Erros em programação paralela são mais difíceis de localizar, pela própria forma como o sistema funciona.

Há algumas práticas simples que podem auxiliar o programador a evitar os erros, como:
- **Evitar o uso de variáveis globais:** Variáveis globais são compartilhadas por todas as threads e podem levar a condições de corrida.
- **Usar variáveis locais:** Variáveis locais são armazenadas na pilha de execução da thread e não são compartilhadas entre threads.
- **Usar métodos sincronizados:** Métodos sincronizados garantem que apenas uma thread possa acessar o método por vez.
- **Usar blocos sincronizados:** Blocos sincronizados permitem que apenas uma thread possa acessar um bloco de código por vez.
- **Usar uma IDE adequada:** IDEs modernas, como IntelliJ IDEA e Eclipse, possuem ferramentas para ajudar a depurar problemas de concorrência.
- **Usar metodologias como UML:** Um bom profissional de programação é ligado a metodologias. E uma boa prática, nesse caso, é a elaboração de diagramas dinâmicos do sistema, como o diagrama de sequência e o diagrama de objetos da UML (em inglês, Unified Modeling Language; em português, Linguagem Unificada de Modelagem), por exemplo. Esses são mecanismos formais que permitem compreender a interação entre os componentes do sistema.
- **Atenção aos detalhes:** Além dessas questões, há sutilezas na programação que muitas vezes passam despercebidas e podem levar o software a se comportar de forma diferente da esperada, já que a linguagem Java oculta os mecanismos de apontamento de memória. Se por um lado isso facilita a programação, por outro exige atenção do programador quando estiver trabalhando com tipos não primitivos. Por exemplo, uma variável do tipo “int” é passada por cópia, mas uma variável do tipo de uma classe definida pelo usuário é passada por referência. Isso tem implicações importantes quando estamos construindo um tipo de dado imutável.

Veja a classe mostrada no código a seguir:

```java
public final class Imutavel {

    private final Contador conta;

    protected Imutavel() {
        this.conta = new Contador(0);
    }
}
```

Queremos construir uma classe que nos fornecerá um objeto imutável. Por sua simplicidade, e já que a tornamos final, assim como seu único atributo, esse deveria ser o caso. Mas examinemos melhor a linha 3. Essa linha diz que “conta” é uma referência imutável. Isso quer dizer que, uma vez instanciada (linha 7), ela não poderá se referenciar a outro objeto, mas nada impede que o objeto por ela apontado se modifique, o que pode ocorrer se a referência vazar ou se o próprio objeto realizar interações que o levem a tal.

### Atenção
Lembre-se: quando se trata de tipos não primitivos, a variável é uma referência de um tipo, e não o tipo em si.

Como se não bastassem todas essas questões, temos o escalonador do sistema, que pode fazer o software se comportar diferentemente do esperado, se tivermos em mente uma política distinta da do escalonador. Questões relativas à carga do sistema também podem interferir, e por isso a corretude do software tem de ser garantida. É comum, quando há falhas na garantia da sincronização, que o programa funcione em algumas execuções e falhe em outras, sem que nada tenha sido modificado. Essa sensibilidade às condições de execução é praticamente um atestado de problemas e condições de corrida que não foram adequadamente tratadas.

Por fim, um bom conhecimento do como as threads se comportam é essencial. Isso é importante para evitar que threads morram inadvertidamente, transformando outras em “zumbis”. Também é um ponto crítico quando operações de E/S ocorrem, pois são operações que muitas vezes podem bloquear a thread indefinidamente.

## Referências
- PROGRAMAÇÃO PARALELA EM JAVA: THREADS (Prof. Marlos de Mendonça Corrêa) - UNIVERSIDADE ESTÁCIO DE SÁ. Disponível em: https://online.estacio.br/l/certificacao-back-end. Acesso em: 17 de agosto de 2024.