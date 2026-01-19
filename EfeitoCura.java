public class EfeitoCura implements Efeito {
    private final int valorCura;

    public EfeitoCura(int valorCura) {
        this.valorCura = valorCura;
    }

    @Override
    public void aplicar(Personagem alvo) {
        alvo.setPontosVida(alvo.getPontosVida() + valorCura);
        System.out.printf("✨ %s restaurou %d HP! HP atual: %d\n", alvo.getNome(), valorCura, alvo.getPontosVida());
    }

    @Override
    public String getDescricaoEfeito() {
        return "Restaura " + valorCura + " de HP.";
    }
}