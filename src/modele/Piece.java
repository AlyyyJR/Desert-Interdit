// ==================================================================
// FICHIER : Piece.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Represente une piece de la machine volante. Les joueurs doivent
// recuperer les 4 pieces pour pouvoir s'echapper du desert
//
// Ce que fait Piece :
//   - On identifie chaque piece par un type et un index (0 a 3)
//   - On fournit le nom en francais de la piece
//   - Les pieces sont localisees grace aux indices de ligne et de
//     colonne dissemines sur le plateau
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

// ==================================================================
// Classe Piece
// ==================================================================
public class Piece {

    // ==================================================================
    // Enumeration des types de pieces
    // ==================================================================
    public enum TypePiece {
        // Le moteur de la machine (index 0)
        MOTEUR("Moteur"),
        // L'helice de la machine (index 1)
        HELICE("Helice"),
        // Le gouvernail de la machine (index 2)
        GOUVERNAIL("Gouvernail"),
        // Le capteur solaire de la machine (index 3)
        CAPTEUR("Capteur solaire");

        // Le nom en francais de la piece
        private String nom;

        // Constructeur du type de piece avec le nom en francais
        TypePiece(String nom) {
            this.nom = nom;
        }
        // Retourne le nom en francais du type de piece
        public String getNom() {
            return nom;
        }
    }

    // ==================================================================
    // Attributs
    // ==================================================================
    // Le type de cette piece
    private TypePiece type;

    // ==================================================================
    // Constructeur
    // ==================================================================
    // Constructeur d'une piece avec son type
    public Piece(TypePiece type) {
        this.type = type;
    }

    // ==================================================================
    // Methodes publiques
    // ==================================================================
    // Retourne le type de cette piece
    public TypePiece getType() {
        return type;
    }
    // Retourne le nom en francais de cette piece
    public String getNom() {
        return type.getNom();
    }
    // Retourne l'index de cette piece (0 a 3)
    // 0 = Moteur, 1 = Helice, 2 = Gouvernail, 3 = Capteur
    public int getIndex() {
        return type.ordinal();
    }
    // Retourne une representation textuelle de la piece
    @Override
    public String toString() {
        return "Piece[" + type.getNom() + " (index=" + getIndex() + ")]";
    }
}
