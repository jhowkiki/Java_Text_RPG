public class Mago extends Personagem {
    public Mago(String nome) {
        // HP baixo, Ataque alto, Defesa baixa
        super(nome, 80, 15, 6, 1);
        this.inventario.adicionarItem(new Item("Essência Arcana", "Aumenta Ataque por 1 turno", new EfeitoAumentoAtaque(), 1));
    }
    //Construtor de Cópia
    public Mago(Mago outro) { 
        super(outro); 
    }
}