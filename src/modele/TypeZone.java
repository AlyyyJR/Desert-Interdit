// ==================================================================
// FICHIER : TypeZone.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Enumeration des differents types de zones sur le plateau
//
// Ce que fait TypeZone :
//   - On definit chaque type de case du plateau 5x5
//   - On identifie les indices de ligne et colonne des pieces
//   - On fournit le nom en francais de chaque type de zone
//
// Les indices (INDICE_LIGNE_x et INDICE_COLONNE_x) nous permettent
// de localiser les pieces. Chaque piece a un indice de ligne et un
// indice de colonne, leur intersection donne la position de la piece
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

// ==================================================================
// Enumeration TypeZone
// ==================================================================
public enum TypeZone {
    // Zone normale sans effet particulier
    NORMAL("Normal"),
    // Oeil de la tempete - case vide au centre qui se deplace
    OEIL("Oeil de la tempete"),
    // Site du crash - point de depart des joueurs
    CRASH("Site du crash"),
    // Piste d'atterrissage - lieu ou les joueurs doivent s'echapper
    PISTE("Piste d'atterrissage"),
    // Oasis - permet de recuperer de l'eau
    OASIS("Oasis"),
    // Mirage - ressemble a une oasis mais ne donne rien
    MIRAGE("Mirage"),
    // Tunnel - abri contre la chaleur et deplacement rapide
    TUNNEL("Tunnel"),
    // Indice de ligne pour le moteur (piece 0)
    INDICE_LIGNE_0("Indice ligne - Moteur"),
    // Indice de colonne pour le moteur (piece 0)
    INDICE_COLONNE_0("Indice colonne - Moteur"),
    // Indice de ligne pour l'helice (piece 1)
    INDICE_LIGNE_1("Indice ligne - Helice"),
    // Indice de colonne pour l'helice (piece 1)
    INDICE_COLONNE_1("Indice colonne - Helice"),
    // Indice de ligne pour le gouvernail (piece 2)
    INDICE_LIGNE_2("Indice ligne - Gouvernail"),
    // Indice de colonne pour le gouvernail (piece 2)
    INDICE_COLONNE_2("Indice colonne - Gouvernail"),
    // Indice de ligne pour le capteur solaire (piece 3)
    INDICE_LIGNE_3("Indice ligne - Capteur solaire"),
    // Indice de colonne pour le capteur solaire (piece 3)
    INDICE_COLONNE_3("Indice colonne - Capteur solaire"),
    // Zone contenant un equipement a recuperer
    EQUIPEMENT("Equipement");

    // Nom en francais du type de zone
    private String nom;
    // ==================================================================
    // Constructeur
    // ==================================================================
    // Constructeur de l'enum TypeZone avec le nom en francais
    TypeZone(String nom) {
        this.nom = nom;
    }

    // ==================================================================
    // Methodes publiques
    // ==================================================================
    // Retourne le nom en francais du type de zone
    public String getNom() {
        return nom;
    }
    // Verifie si ce type de zone est un indice pour localiser une piece
    // Les indices sont les types INDICE_LIGNE_x et INDICE_COLONNE_x
    public boolean isPieceIndice() {
        return this == INDICE_LIGNE_0 || this == INDICE_COLONNE_0
            || this == INDICE_LIGNE_1 || this == INDICE_COLONNE_1
            || this == INDICE_LIGNE_2 || this == INDICE_COLONNE_2
            || this == INDICE_LIGNE_3 || this == INDICE_COLONNE_3
            ;
    }
    // Retourne l'index de la piece associee a cet indice (0 a 3)
    // 0 = moteur, 1 = helice, 2 = gouvernail, 3 = capteur solaire
    // Retourne -1 si ce n'est pas un indice
    public int getPieceIndex() {
        switch (this) {
            case INDICE_LIGNE_0:
            case INDICE_COLONNE_0:
                return 0;
            case INDICE_LIGNE_1:
            case INDICE_COLONNE_1:
                return 1;
            case INDICE_LIGNE_2:
            case INDICE_COLONNE_2:
                return 2;
            case INDICE_LIGNE_3:
            case INDICE_COLONNE_3:
                return 3;
            default:
                return -1;
        }
    }
    // Verifie si cet indice est un indice de ligne
    // Les indices de ligne indiquent sur quelle ligne se trouve la piece
    // Retourne false si c'est un indice de colonne ou pas un indice
    public boolean isLigne() {
        return this == INDICE_LIGNE_0 || this == INDICE_LIGNE_1
            || this == INDICE_LIGNE_2 || this == INDICE_LIGNE_3;
    }
}
