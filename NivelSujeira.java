package lavadorpratos;

import java.util.Random;

public enum NivelSujeira {
    BAIXO(30_000_000),
    MEDIO(50_000_000),
    ENGORDURADO(100_000_000);

    private final int iteracoes;
    private static final Random RANDOM = new Random();

    NivelSujeira(int iteracoes) {
        this.iteracoes = iteracoes;
    }

    public int getIteracoes() {
        return iteracoes;
    }

    public static NivelSujeira sortear() {
        int valor = RANDOM.nextInt(100); 
        if (valor < 30) {
            return BAIXO;       
        } else if (valor < 90) {
            return MEDIO;       
        } else {
            return ENGORDURADO; 
        }
    }
}