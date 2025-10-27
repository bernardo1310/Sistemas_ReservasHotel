package arevores;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação genérica de uma Árvore Rubro-Negra.
 * Estrutura balanceada para inserções, buscas e consultas eficientes.
 *
 * @param <T> Tipo genérico que deve implementar Comparable
 */
public class ArvoreRubroNegra<T extends Comparable<T>> {

    private enum Cor { VERMELHO, PRETO }

    /**
     * Classe interna que representa um nó da árvore
     */
    private class Nodo {
        T valor;
        Cor cor;
        Nodo esquerdo, direito, pai;

        public Nodo(T valor) {
            this.valor = valor;
            this.cor = Cor.VERMELHO; // Novo nó sempre vermelho
        }
    }

    private Nodo raiz;

    public ArvoreRubroNegra() {
        raiz = null;
    }

    // =========================================
    // INSERÇÃO
    // =========================================

    public void inserir(T valor) {
        Nodo novo = new Nodo(valor);
        raiz = inserirRec(raiz, novo);
        corrigirInsercao(novo);
    }

    private Nodo inserirRec(Nodo atual, Nodo novo) {
        if (atual == null) return novo;

        if (novo.valor.compareTo(atual.valor) < 0) {
            atual.esquerdo = inserirRec(atual.esquerdo, novo);
            atual.esquerdo.pai = atual;
        } else if (novo.valor.compareTo(atual.valor) > 0) {
            atual.direito = inserirRec(atual.direito, novo);
            atual.direito.pai = atual;
        }
        return atual;
    }

    private void corrigirInsercao(Nodo nodo) {
        while (nodo != raiz && nodo.pai.cor == Cor.VERMELHO) {
            Nodo pai = nodo.pai;
            Nodo avo = pai.pai;

            if (avo == null) break;

            if (pai == avo.esquerdo) { // Pai à esquerda
                Nodo tio = avo.direito;
                if (tio != null && tio.cor == Cor.VERMELHO) { // Caso recoloração
                    pai.cor = Cor.PRETO;
                    tio.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    nodo = avo;
                } else { // Rotação
                    if (nodo == pai.direito) {
                        nodo = pai;
                        rotacaoEsquerda(nodo);
                    }
                    pai.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    rotacaoDireita(avo);
                }
            } else { // Pai à direita (simétrico)
                Nodo tio = avo.esquerdo;
                if (tio != null && tio.cor == Cor.VERMELHO) {
                    pai.cor = Cor.PRETO;
                    tio.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    nodo = avo;
                } else {
                    if (nodo == pai.esquerdo) {
                        nodo = pai;
                        rotacaoDireita(nodo);
                    }
                    pai.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    rotacaoEsquerda(avo);
                }
            }
        }
        raiz.cor = Cor.PRETO; // raiz sempre preta
    }

    // =========================================
    // ROTAÇÕES
    // =========================================

    private void rotacaoEsquerda(Nodo nodo) {
        Nodo y = nodo.direito;
        nodo.direito = y.esquerdo;
        if (y.esquerdo != null) y.esquerdo.pai = nodo;

        y.pai = nodo.pai;
        if (nodo.pai == null) raiz = y;
        else if (nodo == nodo.pai.esquerdo) nodo.pai.esquerdo = y;
        else nodo.pai.direito = y;

        y.esquerdo = nodo;
        nodo.pai = y;
    }

    private void rotacaoDireita(Nodo nodo) {
        Nodo y = nodo.esquerdo;
        nodo.esquerdo = y.direito;
        if (y.direito != null) y.direito.pai = nodo;

        y.pai = nodo.pai;
        if (nodo.pai == null) raiz = y;
        else if (nodo == nodo.pai.direito) nodo.pai.direito = y;
        else nodo.pai.esquerdo = y;

        y.direito = nodo;
        nodo.pai = y;
    }

    // =========================================
    // CONSULTAS
    // =========================================

    public boolean contem(T valor) {
        return contemRec(raiz, valor);
    }

    private boolean contemRec(Nodo nodo, T valor) {
        if (nodo == null) return false;
        int cmp = valor.compareTo(nodo.valor);
        if (cmp == 0) return true;
        else if (cmp < 0) return contemRec(nodo.esquerdo, valor);
        else return contemRec(nodo.direito, valor);
    }

    public List<T> getElementosEmOrdem() {
        List<T> elementos = new ArrayList<>();
        percorrerEmOrdem(raiz, elementos);
        return elementos;
    }

    private void percorrerEmOrdem(Nodo nodo, List<T> elementos) {
        if (nodo != null) {
            percorrerEmOrdem(nodo.esquerdo, elementos);
            elementos.add(nodo.valor);
            percorrerEmOrdem(nodo.direito, elementos);
        }
    }

    // =========================================
    // RELATÓRIOS
    // =========================================

    public int altura() {
        return alturaRec(raiz);
    }

    private int alturaRec(Nodo nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(alturaRec(nodo.esquerdo), alturaRec(nodo.direito));
    }

    public void contarNosPorCor() {
        int[] contagem = contarNosPorCorRec(raiz);
        System.out.println("Nós PRETOS: " + contagem[0]);
        System.out.println("Nós VERMELHOS: " + contagem[1]);
    }

    private int[] contarNosPorCorRec(Nodo nodo) {
        if (nodo == null) return new int[]{0, 0};
        int[] esq = contarNosPorCorRec(nodo.esquerdo);
        int[] dir = contarNosPorCorRec(nodo.direito);
        int pretos = esq[0] + dir[0] + (nodo.cor == Cor.PRETO ? 1 : 0);
        int vermelhos = esq[1] + dir[1] + (nodo.cor == Cor.VERMELHO ? 1 : 0);
        return new int[]{pretos, vermelhos};
    }

    public void mostrarEmOrdem() {
        System.out.println("Elementos em ordem:");
        for (T t : getElementosEmOrdem()) {
            System.out.println(t);
        }
    }

    public boolean remover(T valor) {
        Nodo nodo = buscarNodo(raiz, valor);
        if (nodo == null) {
            return false; // não encontrado
        }
        removerNodo(nodo);
        return true;
    }

    // Busca o nodo correspondente a um valor
    private Nodo buscarNodo(Nodo nodo, T valor) {
        if (nodo == null) return null;

        int comp = valor.compareTo(nodo.valor);
        if (comp == 0) return nodo;
        else if (comp < 0) return buscarNodo(nodo.esquerdo, valor);
        else return buscarNodo(nodo.direito, valor);
    }

    // Remoção de nodo (versão simplificada para didática)
    private void removerNodo(Nodo nodo) {
        // Caso 1: Nodo com dois filhos -> substitui pelo sucessor
        if (nodo.esquerdo != null && nodo.direito != null) {
            Nodo sucessor = minimo(nodo.direito);
            nodo.valor = sucessor.valor;
            nodo = sucessor;
        }

        // Agora nodo tem no máximo 1 filho
        Nodo filho = (nodo.esquerdo != null) ? nodo.esquerdo : nodo.direito;

        if (filho != null) {
            filho.pai = nodo.pai;
            if (nodo.pai == null) raiz = filho;
            else if (nodo == nodo.pai.esquerdo) nodo.pai.esquerdo = filho;
            else nodo.pai.direito = filho;

            // Se o nodo removido era preto, precisamos ajustar as cores
            if (nodo.cor == Cor.PRETO) {
                // Ajustes simples: aqui podemos chamar uma função de balanceamento
                corrigirRemocao(filho);
            }
        } else {
            // Nodo sem filhos
            if (nodo.pai == null) {
                raiz = null;
            } else {
                if (nodo.cor == Cor.PRETO) {
                    corrigirRemocao(nodo);
                }
                if (nodo.pai != null) {
                    if (nodo == nodo.pai.esquerdo) nodo.pai.esquerdo = null;
                    else nodo.pai.direito = null;
                }
            }
        }
    }

    // Busca o menor nodo a partir de um nodo
    private Nodo minimo(Nodo nodo) {
        while (nodo.esquerdo != null) nodo = nodo.esquerdo;
        return nodo;
    }

    // Método simplificado de correção de remoção (balanceamento)
    private void corrigirRemocao(Nodo nodo) {
        // Aqui você pode adicionar o balanceamento de remoção
        // Para o uso didático e simples, podemos deixar vazio ou apenas recolorir
        nodo.cor = Cor.PRETO;
    }

}
