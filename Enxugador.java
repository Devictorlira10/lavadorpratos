package lavadorpratos;

import java.util.Random;
import java.util.logging.Logger;

public class Enxugador implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Enxugador.class.getName());

    private final Escorredor escorredor;
    private volatile boolean executando = true;
    private final Random random = new Random();

    public Enxugador(Escorredor escorredor) {
        this.escorredor = escorredor;
    }

    public void parar() {
        this.executando = false;
    }

    @Override
    public void run() {
        try {
            while (executando) {
                Prato prato = escorredor.retirar();
                if (prato == null) {
                    continue;
                }

                LOGGER.fine(() -> "Enxugador iniciou a secagem do " + prato);

                int passos = 3 + random.nextInt(8); 
                long limite = (long) passos * 10_000_000L;
                long contador = 0;
                while (executando && contador < limite) {
                    contador++;
                }

                LOGGER.fine(() -> "Enxugador terminou de enxugar o " + prato);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.fine("Enxugador interrompido.");
        }
    }
}