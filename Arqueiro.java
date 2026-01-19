public class Arqueiro extends Personagem {
    public Arqueiro(String nome) {
        // HP moderado, Ataque moderado, Defesa moderada
        super(nome, 100, 13, 8, 1);
        this.inventario.adicionarItem(new Item("Flechas Mágicas", "Dano extra no próximo ataque", new EfeitoAumentoAtaque(), 3));
    }
    //Construtor de Cópia
    public Arqueiro(Arqueiro outro) { 
        super(outro); 
    }
}