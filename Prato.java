package lavadorpratos;

import java.util.concurrent.atomic.AtomicLong;

public class Prato {
    private static final AtomicLong CONTADOR_SERIE = new AtomicLong(1);

    private final long numeroSerie;
    private final NivelSujeira nivelSujeira;

    public Prato() {
        this.numeroSerie = CONTADOR_SERIE.getAndIncrement();
        this.nivelSujeira = NivelSujeira.sortear();
    }

    public long getNumeroSerie() {
        return numeroSerie;
    }

    public NivelSujeira getNivelSujeira() {
        return nivelSujeira;
    }

    @Override
    public String toString() {
        return "Prato #" + numeroSerie + " [Sujeira: " + nivelSujeira + "]";
    }
}