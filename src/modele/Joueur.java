// ==================================================================
// FICHIER : Joueur.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Represente un joueur dans notre jeu. Chaque joueur a un nom,
// une position sur le plateau, un niveau d'eau, une couleur pour
// l'affichage, et peut posseder des equipements
//
// Ce que fait Joueur :
//   - On gere le nom, la position et le niveau d'eau
//   - On gere l'inventaire d'equipements
//   - On detecte la mort par deshydratation (eau < 0)
//   - Le joueur meurt si son eau tombe en dessous de 0
//   - L'eau maximale est de 5
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

import java.awt.Color;
import java.util.ArrayList;

// ==================================================================
// Classe Joueur
// ==================================================================

public class Joueur {

    // ==================================================================
    // Constantes
    // ==================================================================

    // Eau maximale qu'un joueur peut avoir
    public static final int EAU_MAX = 5;

    // ==================================================================
    // Attributs
    // ==================================================================
    // Le nom du joueur
    private String nom;
    // Position en ligne sur la grille (0 a 4)
    private int ligne;
    // Position en colonne sur la grille (0 a 4)
    private int colonne;
    // Niveau d'eau du joueur (commence a 5, max 5)
    private int eau;
    // Couleur du joueur pour l'affichage
    private Color couleur;
    // Liste des equipements possedes par le joueur
    private ArrayList<Equipement> equipements;
    // Le role special du joueur (peut etre null pour le jeu de base)
    private Role role;
    // Index du joueur dans la liste des joueurs
    private int index;
    // Indique si le joueur est protege contre la prochaine vague de chaleur
    private boolean bouclierSolaireActif;

    // ==================================================================
    // Constructeurs
    // ==================================================================
    // Constructeur d'un joueur avec un nom et une couleur.
    // Le joueur commence avec 5 unites d'eau et aucun equipement.
    public Joueur(String nom, Color couleur) {
        this.nom = nom;
        this.eau = EAU_MAX;
        this.couleur = couleur;
        this.equipements = new ArrayList<>();
        this.role = null;
        this.index = -1;
        this.bouclierSolaireActif = false;
        // La position sera definie quand le joueur est place sur le plateau
        this.ligne = -1;
        this.colonne = -1;
    }
    // Constructeur d'un joueur avec un nom, une couleur et un role
    public Joueur(String nom, Color couleur, Role role) {
        this(nom, couleur);
        this.role = role;
    }

    // ==================================================================
    // Gestion de l'eau
    // ==================================================================
    // Le joueur boit de l'eau (perd 1 unite).
    // Si l'eau tombe en dessous de 0, le joueur est en danger de mort.
    // Retourne true si le joueur a encore de l'eau (>= 0), false s'il est deshydrate.
    public boolean boireEau() {
        eau--;
        return eau >= 0;
    }
    // Remplit l'eau du joueur d'une certaine quantite.
    // L'eau ne peut pas depasser EAU_MAX.
    public void remplirEau(int quantite) {
        eau = Math.min(eau + quantite, EAU_MAX);
    }
    // Verifie si le joueur est mort (eau < 0)
    public boolean estMort() {
        return eau < 0;
    }

    // ==================================================================
    // Gestion des equipements
    // ==================================================================
    // Ajoute un equipement a l'inventaire du joueur
    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
    }
    // Retire un equipement de l'inventaire du joueur.
    // Retourne true si l'equipement a ete retire, false sinon.
    public boolean removeEquipement(Equipement equipement) {
        return equipements.remove(equipement);
    }
    // Active la protection contre la prochaine vague de chaleur
    public void activerBouclierSolaire() {
        bouclierSolaireActif = true;
    }
    // Retourne true si le bouclier solaire est actif
    public boolean hasBouclierSolaireActif() {
        return bouclierSolaireActif;
    }
    // Consomme la protection du bouclier solaire
    public void consommerBouclierSolaire() {
        bouclierSolaireActif = false;
    }
    // ==================================================================
    // Getters et Setters
    // ==================================================================
    // Retourne le nom du joueur
    public String getNom() {
        return nom;
    }
    // Modifie le nom du joueur
    public void setNom(String nom) {
        this.nom = nom;
    }
    // Retourne la ligne du joueur sur la grille (0 a 4)
    public int getLigne() {
        return ligne;
    }
    // Modifie la ligne du joueur
    public void setLigne(int ligne) {
        this.ligne = ligne;
    }
    // Retourne la colonne du joueur sur la grille (0 a 4)
    public int getColonne() {
        return colonne;
    }
    // Modifie la colonne du joueur
    public void setColonne(int colonne) {
        this.colonne = colonne;
    }
    // Retourne le niveau d'eau du joueur
    public int getEau() {
        return eau;
    }
    // Modifie le niveau d'eau du joueur
    public void setEau(int eau) {
        this.eau = eau;
    }
    // Retourne la couleur du joueur
    public Color getCouleur() {
        return couleur;
    }
    // Modifie la couleur du joueur
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
    // Retourne la liste des equipements du joueur
    public ArrayList<Equipement> getEquipements() {
        return equipements;
    }
    // Retourne le role du joueur, ou null si pas de role special
    public Role getRole() {
        return role;
    }
    // Modifie le role du joueur
    public void setRole(Role role) {
        this.role = role;
    }

    // ==================================================================
    // Methodes ajoutees pour le controleur
    // ==================================================================
    // Retourne l'index du joueur dans la liste des joueurs
    public int getIndex() {
        return index;
    }
    // Modifie l'index du joueur
    public void setIndex(int index) {
        this.index = index;
    }
    // Alias pour getIndex() - retourne le numero du joueur
    public int getNumero() {
        return index;
    }

    // ==================================================================
    // Representation textuelle
    // ==================================================================
    // Retourne une representation textuelle du joueur
    @Override
    public String toString() {
        String s = "Joueur[" + nom + " pos=(" + ligne + "," + colonne + ")"
            + " eau=" + eau;
        if (role != null) {
            s += " role=" + role.getNom();
        }
        s += " equip=" + equipements.size() + "]";
        return s;
    }
}
