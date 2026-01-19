import java.util.Scanner;

public class Jogo {
    private Personagem jogador;
    private Scanner scanner;
    private Personagem savePoint; 

    public Jogo() {
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("### 🗡️ RPG de Texto: A Busca pelo Cristal de Jade ###");
        criarPersonagem();
        this.savePoint = this.clonarPersonagem(jogador); 
        loopPrincipal();
    }

    private void criarPersonagem() {
        System.out.print("Digite o nome do seu Herói: ");
        String nome = scanner.nextLine();
        int escolhaClasse = 0;

        while (escolhaClasse < 1 || escolhaClasse > 3) {
            System.out.println("\nEscolha sua Classe:");
            System.out.println("[1] Guerreiro (Alto HP/Defesa)");
            System.out.println("[2] Mago (Alto Ataque/Poder)");
            System.out.println("[3] Arqueiro (Equilibrado)");
            System.out.print("Sua escolha: ");
            try {
                escolhaClasse = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                escolhaClasse = 0;
            }
        }

        switch (escolhaClasse) {
            case 1:
                jogador = new Guerreiro(nome);
                break;
            case 2:
                jogador = new Mago(nome);
                break;
            case 3:
                jogador = new Arqueiro(nome);
                break;
        }

        System.out.println("\nBem-vindo(a), " + jogador.toString() + "!");
    }

    //Sistema de Combate
    public boolean batalhar(Inimigo inimigo) {
        System.out.printf("\n--- COMBATE INICIADO: %s vs. %s ---\n", jogador.getNome(), inimigo.getNome());
        System.out.println(jogador.toString()); 
        System.out.println(inimigo.toString());
        
        int turno = 1;
        while (jogador.getPontosVida() > 0 && inimigo.getPontosVida() > 0) {
            System.out.printf("\n--- Turno %d ---\n", turno++);
            System.out.printf("%s HP: %d | %s HP: %d\n", jogador.getNome(), jogador.getPontosVida(), inimigo.getNome(), inimigo.getPontosVida());
            
            int acao = 0;
            while (acao < 1 || acao > 3) {
                System.out.println("\nAção: [1] Atacar | [2] Usar Item | [3] Fugir");
                System.out.print("Escolha: ");
                try {
                    acao = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    acao = 0;
                }
            }

            if (acao == 1) { //Atacar
                atacarTurno(jogador, inimigo);
            } else if (acao == 2) { //Usar Item
                if (!usarItem(jogador)) {
                    System.out.println("Uso de item falhou ou cancelado. O turno é perdido.");
                }
            } else if (acao == 3) { //Fugir
                if (tentarFugir()) {
                    System.out.printf("\n%s fugiu com sucesso!\n", jogador.getNome());
                    return false; 
                } else {
                    System.out.println("\nFuga falhou! O inimigo ataca.");
                }
            }

            if (inimigo.getPontosVida() > 0 && acao != 3) {
                atacarTurno(inimigo, jogador);
            }
        }
        
        //Fim do Combate
        if (jogador.getPontosVida() <= 0) {
            System.out.printf("\n--- %s foi derrotado! FIM DE JOGO ---\n", jogador.getNome());
            return false; //Derrota
        } else {
            System.out.printf("\n--- VITÓRIA! %s derrotou %s ---\n", jogador.getNome(), inimigo.getNome());
            jogador.ganharXP(inimigo.getXpConcedida());
            
            saquear(inimigo);
            return true; //Vitória
        }
    }

    private void atacarTurno(Personagem atacante, Personagem defensor) {
        int rolagemAtaque = Dado.rolarD6();
        int ataqueTotal = atacante.getAtaque() + rolagemAtaque;
        int dano = ataqueTotal - defensor.getDefesa();

        if (dano > 0) {
            defensor.receberDano(dano);
            System.out.printf("%s rola %d. Causa %d de dano em %s. HP restante: %d\n", 
                              atacante.getNome(), rolagemAtaque, dano, defensor.getNome(), defensor.getPontosVida());
        } else {
            System.out.printf("%s rola %d. O ataque não supera a defesa de %s.\n", 
                              atacante.getNome(), rolagemAtaque, defensor.getNome());
        }
    }
    
    private boolean usarItem(Personagem p) {
        p.getInventario().listarItens();
        System.out.print("Digite o nome do item a usar (ou 'voltar' para cancelar): ");
        String nomeItem = scanner.nextLine();
        
        if (nomeItem.equalsIgnoreCase("voltar")) {
            return false;
        }

        Item item = p.getInventario().getItem(nomeItem);
        
        if (item != null && item.getQuantidade() > 0) {
            if (item.usar()) {
                aplicarEfeito(p, item);
                return true;
            }
        }
        System.out.println("Item inválido ou sem unidades.");
        return false;
    }
    
    private void aplicarEfeito(Personagem p, Item item) {
        if (item.getEfeito() != null) {
            item.getEfeito().aplicar(p);
        } else {
            System.out.printf("%s usou %s, mas não houve efeito imediato.\n", p.getNome(), item.getNome());
        }
    }
    
    private boolean tentarFugir() {
        int rolagemFuga = Dado.rolarD6();
        return rolagemFuga >= 4; 
    }
    
    private void saquear(Inimigo inimigo) {
        System.out.println("\n--- Saque ---");
        Inventario inventarioInimigo = inimigo.getInventario();
        Inventario saque = (Inventario) inventarioInimigo.clone();
        for (Item itemSaque : saque.getItens()) {
            if (itemSaque.getNome().contains("Poção") || itemSaque.getNome().contains("Moeda")) {
                 jogador.getInventario().adicionarItem(itemSaque);
                 System.out.printf("Saqueado: %s (x%d)\n", itemSaque.getNome(), itemSaque.getQuantidade());
            }
        }
    }
    
    //Geração Aleatória de Monstros

    private Inimigo gerarMonstro() {
        int nivelBase = jogador.getNivel();
        int nivelMonstro = nivelBase;
        String nomeMonstro;
        if (Dado.rolarD6() <= 3) {
            nivelMonstro = nivelBase + 1;
        }

        if (nivelMonstro < 1) {
            nivelMonstro = 1;
        }
        int tipo = Dado.rolarD6();
        if (tipo <= 2) {
            nomeMonstro = "Goblin Batedor";
        } else if (tipo <= 4) {
            nomeMonstro = "Esqueleto Guerreiro";
        } else {
            nomeMonstro = "Lobo Selvagem";
        }

        return new Inimigo(nomeMonstro, nivelMonstro);
    }
    
    //Navegação e História

    private void loopPrincipal() {
        String estado = "VILA_INICIAL";
        while (jogador.getPontosVida() > 0 && !estado.equals("FIM_JOGO")) {
            System.out.println("\n==================================");
            System.out.printf("📍 Local: %s | %s\n", estado.replace('_', ' '), jogador.toString());
            System.out.println("==================================");
            System.out.println("[1] Explorar | [2] Inventário/Usar Item | [3] Salvar Jogo | [4] Carregar Jogo | [5] Sair");
            System.out.print("Sua escolha: ");
            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    estado = explorar(estado);
                    break;
                case "2":
                    jogador.getInventario().listarItens();
                    System.out.print("Deseja usar algum item? (S/N): ");
                    String usar = scanner.nextLine();
                    if (usar.equalsIgnoreCase("s")) {
                        usarItem(jogador); 
                    }
                    break;
                case "3":
                    salvarJogo();
                    break;
                case "4":
                    carregarJogo();
                    break;
                case "5":
                    System.out.println("Até a próxima, herói!");
                    estado = "FIM_JOGO";
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
            
            if (estado.equals("VITÓRIA")) {
                System.out.println("\n🎉 Você encontrou o Cristal de Jade! Parabéns, sua jornada termina aqui.");
                estado = "FIM_JOGO";
            }
        }
        
        if (jogador.getPontosVida() <= 0) {
            System.out.println("O herói pereceu. FIM DE JOGO.");
        }
    }

    private String explorar(String localAtual) {
        System.out.println("\nVocê se aventura pelos arredores...");

        switch (localAtual) {
            case "VILA_INICIAL":
                System.out.println("Você está na beira da Floresta Sombria. Qual caminho escolher?");
                System.out.println("[1] Entrar na floresta | [2] Seguir o riacho");
                String escolha = scanner.nextLine();
                
                if (escolha.equals("1")) {
                    return "FLORESTA_SOMBRIA";
                } else if (escolha.equals("2")) {
                    return "BEIRA_RIO";
                } else {
                    return localAtual;
                }
                
            case "FLORESTA_SOMBRIA":
                if (Dado.rolarD6() <= 3) {
                    Inimigo inimigoAleatorio = gerarMonstro();
                    System.out.printf("Um **%s** Nv.%d salta dos arbustos!\n", inimigoAleatorio.getNome(), inimigoAleatorio.getNivel());
                    
                    boolean vitoria = batalhar(inimigoAleatorio); 
                    
                    if (jogador.getPontosVida() <= 0) {
                        return "FIM_JOGO"; 
                    } else if (vitoria) {
                        System.out.println("O caminho está livre. Você avança.");
                        return "RUINAS_ANTIGAS"; 
                    } else {
                        System.out.println("Você volta à clareira inicial da floresta.");
                        return localAtual; 
                    }
                } else {
                    System.out.println("Você encontra uma mochila abandonada. [Ação: Pegar? (S/N)]");
                    
                    String acaoMochila = "";
                    while (!acaoMochila.equalsIgnoreCase("S") && !acaoMochila.equalsIgnoreCase("N")) {
                        System.out.print("Sua escolha (S/N): ");
                        acaoMochila = scanner.nextLine();
                        
                        if (acaoMochila.equalsIgnoreCase("S")) {
                            jogador.getInventario().adicionarItem(new Item("Poção de Cura", "Restaura 30 HP", new EfeitoCura(30), 1));
                            System.out.println("Você encontrou 1 Poção de Cura.");
                        } else if (acaoMochila.equalsIgnoreCase("N")) {
                            System.out.println("Você ignora a mochila e segue em frente.");
                        } else {
                            System.out.println("Opção inválida. Digite S (Sim) ou N (Não).");
                        }
                    }
                    return localAtual;
                }

            case "RUINAS_ANTIGAS":
                int nivelMinimo = 3; 
                
                System.out.println("Você alcança a entrada de uma câmara. Há duas portas.");
                System.out.printf("Ambas parecem exigir pelo menos Nível %d para serem abertas.\n", nivelMinimo);
                System.out.println("[1] Porta de Pedra (Guerreiro / Força) | [2] Porta de Madeira (Mago/Arqueiro / Astúcia)");
                String porta = scanner.nextLine();
                
                if (jogador.getNivel() < nivelMinimo) {
                    System.out.printf("🚫 Seu nível (%d) é muito baixo! Você não tem força ou conhecimento para abrir nenhuma porta e se depara com uma **Armadilha**!\n", jogador.getNivel());
                    jogador.receberDano(15);
                    System.out.printf("Você perde 15 HP! HP restante: %d\n", jogador.getPontosVida());
                    return "VILA_INICIAL"; // Volta à vila
                }
                
                if (porta.equals("1")) {
                    if (jogador instanceof Guerreiro) {
                         System.out.println("💪 A força bruta do Guerreiro quebra o mecanismo!");
                         return "VITÓRIA";
                    } else {
                         System.out.println("⛔ Você tentou forçar, mas falhou! Você se depara com uma **Armadilha**!");
                         jogador.receberDano(15);
                         System.out.printf("Você perde 15 HP! HP restante: %d\n", jogador.getPontosVida());
                         return localAtual;
                    }
                } else if (porta.equals("2")) {
                    if (jogador instanceof Mago || jogador instanceof Arqueiro) {
                         System.out.println("✨ Sua astúcia/conhecimento permite destrancar o feitiço/trava!"); 
                         return "VITÓRIA";
                    } else {
                         System.out.println("⛔ Você tentou destrancar, mas falhou! Você se depara com uma **Armadilha**!");
                         jogador.receberDano(15);
                         System.out.printf("Você perde 15 HP! HP restante: %d\n", jogador.getPontosVida());
                         return localAtual;
                    }
                } else {
                    System.out.println("Opção inválida, voltando.");
                    return localAtual;
                }
                
            case "BEIRA_RIO":
                 System.out.println("Você encontra um **Troll do Rio** Nv.2 faminto!");
                 Inimigo troll = new Inimigo("Troll do Rio", 2);
                 boolean vitoriaTroll = batalhar(troll);
                 
                 if (jogador.getPontosVida() <= 0) {
                    return "FIM_JOGO"; 
                 } else if (vitoriaTroll) {
                      return "RUINAS_ANTIGAS"; 
                 } else {
                      System.out.println("O Troll desiste de te perseguir por um momento.");
                      return localAtual; 
                 }

            default:
                return localAtual;
        }
    }
    
    //Save Point

    private Personagem clonarPersonagem(Personagem original) {
        if (original instanceof Guerreiro) {
            return new Guerreiro((Guerreiro) original);
        } else if (original instanceof Mago) {
            return new Mago((Mago) original);
        } else if (original instanceof Arqueiro) {
            return new Arqueiro((Arqueiro) original);
        }
        return null;
    }

    private void salvarJogo() {
        this.savePoint = this.clonarPersonagem(jogador);
        System.out.println("\n💾 Jogo Salvo com sucesso!");
        System.out.println("SavePoint: " + savePoint.toString());
    }
    
    private void carregarJogo() {
        if (this.savePoint != null) {
            this.jogador = this.clonarPersonagem(savePoint);
            System.out.println("\n🔄 Jogo Carregado. Retornando ao último ponto salvo.");
            System.out.println("Estado Atual: " + jogador.toString());
        } else {
            System.out.println("\n❌ Não há um jogo salvo para carregar.");
        }
    }

    public static void main(String[] args) {
        new Jogo().iniciar();
    }
}