// ==================================================================
// FICHIER : Role.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Enumeration des differents roles que les joueurs peuvent avoir
//
// Ce que fait Role :
//   - On definit les capacites uniques de chaque personnage
//   - On fournit le nom et la description de chaque role
//   - Ca enrichit la strategie du jeu cooperatif
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

// ==================================================================
// Enumeration Role
// ==================================================================
public enum Role {
    // L'archeologue peut retirer 2 sable pour 1 action
    ARCHEOLOGUE("Archeologue",
        "Peut retirer 2 tonnes de sable en une seule action au lieu d'une"),
    // L'alpiniste peut se deplacer sur les zones bloquees et emmener un joueur
    ALPINISTE("Alpiniste",
        "Peut se deplacer sur les zones bloquees par le sable et emmener un autre joueur"),
    // L'explorateur peut se deplacer et creuser en diagonale
    EXPLORATEUR("Explorateur",
        "Peut se deplacer et creuser en diagonale en plus des directions normales"),
    // Le meteorologue peut piocher moins de cartes tempete
    METEOROLOGUE("Meteorologue",
        "Peut depenser des actions pour reduire le nombre de cartes tempete piochees"),
    // La navigatrice peut deplacer les autres joueurs
    NAVIGATRICE("Navigatrice",
        "Peut deplacer un autre joueur jusqu'a 3 cases pour 1 action"),
    // La porteuse d'eau peut prendre 2 eaux d'une oasis et donner a distance
    PORTEUSE_EAU("Porteuse d'eau",
        "Recupere 2 eaux au lieu d'une dans les oasis et peut donner de l'eau a distance");

    // Le nom en francais du role
    private String nom;
    // La description des capacites du role
    private String description;

    // ==================================================================
    // Constructeur
    // ==================================================================
    // Constructeur de l'enum Role avec le nom et la description
    Role(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // ==================================================================
    // Methodes publiques
    // ==================================================================
    // Retourne le nom en francais du role
    public String getNom() {
        return nom;
    }
    // Retourne la description de la capacite speciale du role
    public String getDescription() {
        return description;
    }
}
