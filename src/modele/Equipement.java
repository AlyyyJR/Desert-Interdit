// ==================================================================
// FICHIER : Equipement.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Represente une carte equipement. Ce sont des objets speciaux
// que les joueurs peuvent utiliser pour faciliter leur survie
//
// Ce que fait Equipement :
//   - On definit chaque type d'equipement avec son effet
//   - On fournit le nom et la description de chaque equipement
//   - Chaque equipement a un type, un nom et une description
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

// ==================================================================
// Classe Equipement
// ==================================================================
public class Equipement {

    // ==================================================================
    // Enumeration des types d'equipement
    // ==================================================================
    public enum TypeEquipement {
        // Jetpack - permet de se deplacer a n'importe quelle case
        JETPACK("Jetpack",
            "Permet a un joueur de se deplacer sur n'importe quelle case du plateau"),
        // Bouclier solaire - protege contre la vague de chaleur
        BOUCLIER_SOLAIRE("Bouclier solaire",
            "Protege un joueur contre la prochaine vague de chaleur"),
        // Blaster - retire tout le sable d'une case
        BLASTER("Blaster",
            "Retire toutes les tonnes de sable d'une case adjacente ou de la case actuelle"),
        // Detecteur de terrain - permet de regarder les zones adjacentes
        DETECTEUR("Detecteur de terrain",
            "Permet de regarder les types des zones dans une zone de 3x3 autour du joueur");
        // Le nom en francais de l'equipement
        private String nom;
        // La description de l'effet de l'equipement
        private String description;
        // Constructeur du type d'equipement avec le nom et la description
        TypeEquipement(String nom, String description) {
            this.nom = nom;
            this.description = description;
        }
        // Retourne le nom en francais de l'equipement
        public String getNom() {
            return nom;
        }
        // Retourne la description de l'equipement
        public String getDescription() {
            return description;
        }
    }

    // ==================================================================
    // Attributs
    // ==================================================================
    // Le type de cet equipement
    private TypeEquipement type;

    // ==================================================================
    // Constructeur
    // ==================================================================
    // Constructeur d'un equipement avec son type
    public Equipement(TypeEquipement type) {
        this.type = type;
    }

    // ==================================================================
    // Methodes publiques
    // ==================================================================
    // Retourne le type de cet equipement
    public TypeEquipement getType() {
        return type;
    }
    // Retourne le nom en francais de cet equipement
    public String getNom() {
        return type.getNom();
    }
    // Retourne la description de l'effet de cet equipement
    public String getDescription() {
        return type.getDescription();
    }
    // Retourne une representation textuelle de l'equipement
    @Override
    public String toString() {
        return "Equipement[" + type.getNom() + "]";
    }
}
