public abstract class Personagem {
    protected String nome;
    protected int pontosVida;
    protected int ataque; 
    protected int defesa;
    protected int nivel;
    protected int experiencia;      
    protected int xpProximoNivel;   
    protected Inventario inventario;

    //Construtor
    public Personagem(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.experiencia = 0;
        this.xpProximoNivel = nivel * 100; 
        this.inventario = new Inventario();
    }

    //Construtor de Cópia
    public Personagem(Personagem outro) {
        this.nome = outro.nome;
        this.pontosVida = outro.pontosVida;
        this.ataque = outro.ataque;
        this.defesa = outro.defesa;
        this.nivel = outro.nivel;
        this.experiencia = outro.experiencia;      
        this.xpProximoNivel = outro.xpProximoNivel;  
        this.inventario = new Inventario(outro.inventario);
    }
    
    public void ganharXP(int xpGanho) {
        this.experiencia += xpGanho;
        System.out.printf("\n🌟 %s ganhou %d de experiência!\n", this.nome, xpGanho);
        
        while (this.experiencia >= this.xpProximoNivel) {
            subirNivel();
        }
    }

    protected void subirNivel() {
        this.experiencia -= this.xpProximoNivel;
        this.nivel++;
        this.xpProximoNivel = this.nivel * 100 + 50; 
        this.pontosVida += 20;
        this.ataque += 3;
        this.defesa += 2;
        
        System.out.println("🎉🎉 PARABÉNS! " + this.nome + " alcançou o Nível " + this.nivel + "!");
        System.out.printf("HP: +20, Ataque: +3, Defesa: +2. Próximo Nível em %d XP.\n", this.xpProximoNivel);
    }

    //Getters e Setters
    public String getNome() { return nome; }
    public int getPontosVida() { return pontosVida; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public int getNivel() { return nivel; }
    public Inventario getInventario() { return inventario; }
    public void setPontosVida(int pontosVida) { this.pontosVida = pontosVida; }

    public void receberDano(int dano) {
        this.pontosVida -= dano;
        if (this.pontosVida < 0) this.pontosVida = 0;
    }

    @Override
    public String toString() {
        return String.format("%s [Nível %d, XP: %d/%d] | HP: %d | Atk: %d | Def: %d", 
                             nome, nivel, experiencia, xpProximoNivel, pontosVida, ataque, defesa);
    }
}