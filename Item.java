import java.util.Objects;

public class Item implements Comparable<Item>, Cloneable {
    private String nome;
    private String descricao;
    private Efeito efeito; 
    private int quantidade;

    //Construtor
    public Item(String nome, String descricao, Efeito efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito; 
        this.quantidade = quantidade;
    }

    //Construtor de Cópia 
    public Item(Item outro) {
        this.nome = outro.nome;
        this.descricao = outro.descricao;
        this.efeito = outro.efeito; 
        this.quantidade = outro.quantidade; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(nome, item.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public int compareTo(Item outro) {
        return this.nome.compareTo(outro.nome);
    }
    
    //Getters e Setters
    
    public String getNome() { return nome; }
    public Efeito getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public boolean usar() {
        if (this.quantidade > 0) {
            this.quantidade--;
            return true;
        }
        return false;
    }

    public String getDescricaoEfeito() {
        if (efeito == null) return "Nenhum";
        return efeito.getDescricaoEfeito();
    }

    @Override
    public String toString() {
        return String.format("%s (x%d) - %s [Efeito: %s]", nome, quantidade, descricao, getDescricaoEfeito());
    }
}