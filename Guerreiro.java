public class Guerreiro extends Personagem {
    public Guerreiro(String nome) {
        // HP alto, Defesa alta, Ataque moderado
        super(nome, 120, 12, 10, 1);
        this.inventario.adicionarItem(new Item("Poção de Cura", "Restaura 30 HP", new EfeitoCura(30), 2));
    }
    //Construtor de Cópia
    public Guerreiro(Guerreiro outro) { 
        super(outro); 
    }
}