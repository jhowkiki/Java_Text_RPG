public class EfeitoAumentoAtaque implements Efeito {
    @Override
    public void aplicar(Personagem alvo) {
        System.out.printf("💥 O ataque de %s está temporariamente aprimorado!\n", alvo.getNome());
    }

    @Override
    public String getDescricaoEfeito() {
        return "Aumenta o poder de ataque base por um curto período.";
    }
}