package lavadorpratos;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static final int CAPACIDADE_ESCORREDOR = 20;
    private static final int QUANTIDADE_LAVADORES = 2;
    private static final int QUANTIDADE_ENXUGADORES = 2;

    private Escorredor escorredor;
    private List<Lavador> lavadores;
    private List<Enxugador> enxugadores;
    private List<Thread> threadsLavadores;
    private List<Thread> threadsEnxugadores;

    public static void main(String[] args) {
        configurarLogger();
        App app = new App();
        app.iniciar();

        try {
           
            Thread.sleep(60_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        app.parar();
    }

    private static void configurarLogger() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO); 
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.INFO);
        }
    }

    public void iniciar() {
        LOGGER.info("=== INICIANDO O SISTEMA LAVADOR DE PRATOS ===");
        escorredor = new Escorredor(CAPACIDADE_ESCORREDOR);
        lavadores = new ArrayList<>();
        enxugadores = new ArrayList<>();
        threadsLavadores = new ArrayList<>();
        threadsEnxugadores = new ArrayList<>();

        for (int i = 0; i < QUANTIDADE_LAVADORES; i++) {
            Lavador lavador = new Lavador(escorredor);
            lavadores.add(lavador);
            Thread t = new Thread(lavador, "Lavador-" + (i + 1));
            threadsLavadores.add(t);
            t.start();
        }

        for (int i = 0; i < QUANTIDADE_ENXUGADORES; i++) {
            Enxugador enxugador = new Enxugador(escorredor);
            enxugadores.add(enxugador);
            Thread t = new Thread(enxugador, "Enxugador-" + (i + 1));
            threadsEnxugadores.add(t);
            t.start();
        }
        LOGGER.info("Trabalhadores iniciados com sucesso.");
    }

    public void parar() {
        LOGGER.info("=== SOLICITANDO ENCERRAMENTO DOS TRABALHOS ===");
        
       
        for (Lavador l : lavadores) {
            l.parar();
        }

        
        synchronized (escorredor) {
            while (escorredor.getTamanhoAtual() > 0) {
                LOGGER.info(() -> "Aguardando enxugadores limparem o escorredor restante. Pratos restantes: " + escorredor.getTamanhoAtual());
                try {
                    escorredor.wait(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        
        for (Enxugador e : enxugadores) {
            e.parar();
        }

        
        for (Thread t : threadsLavadores) {
            t.interrupt();
        }
        for (Thread t : threadsEnxugadores) {
            t.interrupt();
        }

        LOGGER.info("=== APLICAÇÃO ENCERRADA COM SUCESSO ===");
    }
}