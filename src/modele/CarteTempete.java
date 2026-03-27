// ==================================================================
// FICHIER : CarteTempete.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Represente une carte de la pioche tempete. On les pioche a la
// fin de chaque tour et elles declenchent differents effets :
// vent (deplace des zones), vague de chaleur, ou la tempete se
// dechaine (augmente le niveau)
//
// Ce que fait CarteTempete :
//   - On definit les types de cartes (VENT, CHALEUR, DECHAINE)
//   - Pour les cartes VENT : on stocke direction (0-3) et force (1-3)
//   - Directions du vent :
//       0 = droite, 1 = gauche, 2 = bas, 3 = haut
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

// ==================================================================
// Classe CarteTempete
// ==================================================================
public class CarteTempete {

    // ==================================================================
    // Enumeration des types de cartes tempete
    // ==================================================================
    public enum TypeCarteTempete {
        // Le vent souffle et deplace des zones
        VENT("Le vent souffle"),
        // Vague de chaleur - tous les joueurs perdent de l'eau
        CHALEUR("Vague de chaleur"),
        // La tempete se dechaine - le niveau de tempete augmente
        DECHAINE("La tempete se dechaine");

        // Le nom en francais
        private String nom;

        // Constructeur du type de carte tempete
        TypeCarteTempete(String nom) {
            this.nom = nom;
        }
        // Retourne le nom en francais du type de carte
        public String getNom() {
            return nom;
        }
    }

    // ==================================================================
    // Attributs
    // ==================================================================
    // Le type de cette carte tempete
    private TypeCarteTempete type;
    // Direction du vent (uniquement pour les cartes VENT)
    // 0 = droite, 1 = gauche, 2 = bas, 3 = haut
    private int direction;
    // Force du vent (uniquement pour les cartes VENT)
    // Valeur de 1 a 3 indiquant combien de cases sont deplacees.
    private int force;

    // ==================================================================
    // Constructeurs
    // ==================================================================
    // Constructeur pour une carte de type VENT avec direction et force
    public CarteTempete(TypeCarteTempete type, int direction, int force) {
        this.type = type;
        this.direction = direction;
        this.force = force;
    }
    // Constructeur pour une carte CHALEUR ou DECHAINE (pas de direction ni force)
    public CarteTempete(TypeCarteTempete type) {
        this.type = type;
        this.direction = -1;
        this.force = 0;
    }

    // ==================================================================
    // Methodes publiques
    // ==================================================================
    // Retourne le type de cette carte tempete
    public TypeCarteTempete getType() {
        return type;
    }
    // Retourne la direction du vent
    // Uniquement pertinent pour les cartes VENT
    // Retourne -1 si non applicable
    public int getDirection() {
        return direction;
    }
    // Retourne la force du vent
    // Uniquement pertinent pour les cartes VENT
    // Retourne 0 si non applicable
    public int getForce() {
        return force;
    }
    // Retourne le nom de la direction en francais
    public String getNomDirection() {
        switch (direction) {
            case 0: return "droite";
            case 1: return "gauche";
            case 2: return "bas";
            case 3: return "haut";
            default: return "aucune";
        }
    }
    // Retourne une representation textuelle de la carte tempete
    @Override
    public String toString() {
        if (type == TypeCarteTempete.VENT) {
            return "CarteTempete[VENT direction=" + getNomDirection()
                + " force=" + force + "]";
        } else {
            return "CarteTempete[" + type.getNom() + "]";
        }
    }
}
