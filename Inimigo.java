public class Inimigo extends Personagem {
    public Inimigo(String nome, int nivel) {
        //Valores baseados no nível para escala
        super(nome, 40 * nivel, 8 + 2 * nivel, 5 + nivel, nivel);
        if (nivel == 1) {
            this.inventario.adicionarItem(new Item("Moeda de Cobre", "Vale pouco.", null, 3));
        } else if (nivel > 1) {
            this.inventario.adicionarItem(new Item("Poção de Cura", "Restaura 30 HP", new EfeitoCura(30), 1));
        }
    }
    //Construtor de Cópia
    public Inimigo(Inimigo outro) { 
        super(outro); 
    }

    public int getXpConcedida() {
        return this.nivel * 50;
    }
}