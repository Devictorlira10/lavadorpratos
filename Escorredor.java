package lavadorpratos;

import java.util.logging.Logger;

public class Escorredor {
    private static final Logger LOGGER = Logger.getLogger(Escorredor.class.getName());

    private final Prato[] buffer;
    private int inicio;
    private int fim;
    private int tamanhoAtual;
    private final int capacidade;

    public Escorredor(int capacidade) {
        this.capacidade = capacidade;
        this.buffer = new Prato[capacidade];
        this.inicio = 0;
        this.fim = 0;
        this.tamanhoAtual = 0;
    }

    public synchronized void colocar(Prato prato) throws InterruptedException {
        while (tamanhoAtual == capacidade) {
            LOGGER.info(() -> "[ESCORREDOR CHEIO] Capacidade máxima atingida (" + tamanhoAtual + "/" + capacidade + "). Lavador aguardando...");
            wait();
        }

        buffer[fim] = prato;
        fim = (fim + 1) % capacidade;
        tamanhoAtual++;

        LOGGER.fine(() -> "Prato colocado. Total no escorredor: " + tamanhoAtual + "/" + capacidade);

        
        if (tamanhoAtual == capacidade) {
            LOGGER.info(() -> "[ESCORREDOR CHEIO] Escorredor ficou cheio (" + tamanhoAtual + " pratos). Notificando enxugadores.");
            notify();
        }
    }

    public synchronized Prato retirar() throws InterruptedException {
        while (tamanhoAtual == 0) {
            LOGGER.info(() -> "[ESCORREDOR VAZIO] Nenhum prato disponível. Enxugador aguardando...");
            wait();
        }

        Prato prato = buffer[inicio];
        buffer[inicio] = null;
        inicio = (inicio + 1) % capacidade;
        tamanhoAtual--;

        LOGGER.fine(() -> "Prato retirado. Total no escorredor: " + tamanhoAtual + "/" + capacidade);

        
        if (tamanhoAtual == 0) {
            LOGGER.info(() -> "[ESCORREDOR VAZIO] Escorredor esvaziou (0 pratos). Notificando lavadores.");
            notify();
        }

        return prato;
    }

    public synchronized int getTamanhoAtual() {
        return tamanhoAtual;
    }
}