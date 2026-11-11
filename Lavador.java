package lavadorpratos;

import java.util.logging.Logger;

public class Lavador implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Lavador.class.getName());

    private final Escorredor escorredor;
    private volatile boolean executando = true;

    public Lavador(Escorredor escorredor) {
        this.escorredor = escorredor;
    }

    public void parar() {
        this.executando = false;
    }

    @Override
    public void run() {
        try {
            while (executando) {
                Prato prato = new Prato();
                LOGGER.fine(() -> "Lavador iniciou a lavagem do " + prato);

                
                long limite = prato.getNivelSujeira().getIteracoes();
                long contador = 0;
                while (executando && contador < limite) {
                    contador++;
                }

                if (!executando) {
                    break;
                }

                escorredor.colocar(prato);
                LOGGER.fine(() -> "Lavador terminou e colocou no escorredor: " + prato);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.fine("Lavador interrompido.");
        }
    }
}