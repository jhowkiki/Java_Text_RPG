import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario implements Cloneable {
    private List<Item> itens;

    //Construtor
    public Inventario() {
        this.itens = new ArrayList<>();
    }

    //Construtor de Cópia 
    public Inventario(Inventario outro) {
        this.itens = new ArrayList<>();
        for (Item item : outro.itens) {
            this.itens.add(new Item(item));
        }
    }

    public void adicionarItem(Item novoItem) {
        int index = itens.indexOf(novoItem);
        if (index != -1) {
            Item itemExistente = itens.get(index);
            itemExistente.setQuantidade(itemExistente.getQuantidade() + novoItem.getQuantidade());
        } else {
            itens.add(new Item(novoItem));
        }
    }

    public boolean removerItem(String nomeItem) {
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            if (item.getNome().equalsIgnoreCase(nomeItem)) {
                if (item.usar()) { 
                    if (item.getQuantidade() <= 0) {
                        itens.remove(i);
                    }
                    return true;
                }
                return false; 
            }
        }
        return false; 
    }

    public void listarItens() {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        Collections.sort(itens);
        System.out.println("\n--- Inventário ---");
        for (int i = 0; i < itens.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, itens.get(i).toString());
        }
        System.out.println("------------------\n");
    }

    public Item getItem(String nomeItem) {
        for (Item item : itens) {
            if (item.getNome().equalsIgnoreCase(nomeItem)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getItens() {
        return this.itens;
    }

    @Override
    public Object clone() {
        try {
            Inventario clone = (Inventario) super.clone();
            clone.itens = new ArrayList<>();
            for (Item item : this.itens) {
                clone.itens.add(new Item(item));
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}