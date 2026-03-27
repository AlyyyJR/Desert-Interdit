// ==================================================================
// FICHIER : Zone.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est une case du plateau 5x5. Chaque zone a une position
// (ligne, colonne), un type, un niveau de sable, et peut etre
// occupee par un ou plusieurs joueurs
//
// Ce que fait Zone :
//   - On stocke la position, le type et le niveau de sable
//   - On gere la liste des joueurs presents (occupants)
//   - On gere l'exploration et le retrait de sable
//   - Une zone est bloquee si le sable atteint 2 ou plus
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

import java.util.ArrayList;

// ==================================================================
// Classe Zone
// ==================================================================
public class Zone {
    // ==================================================================
    // Attributs
    // ==================================================================
    // Position en ligne sur la grille (0 a 4)
    private int ligne;
    // Position en colonne sur la grille (0 a 4)
    private int colonne;
    // Niveau de sable sur cette zone (commence a 0)
    private int sable;
    // Indique si la zone a ete exploree (retournee)
    private boolean exploree;
    // Le type de cette zone
    private TypeZone type;
    // Indique si le type de la zone est visible pour les joueurs
    private boolean visible;
    // Liste des joueurs presents sur cette zone
    private ArrayList<Joueur> occupants;

    // ==================================================================
    // Constructeur
    // ==================================================================
    // Constructeur d'une zone a une position donnee avec un type
    // La zone commence sans sable, non exploree, non visible
    public Zone(int ligne, int colonne, TypeZone type) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.sable = 0;
        this.exploree = false;
        this.type = type;
        this.visible = false;
        this.occupants = new ArrayList<>();
    }

    // ==================================================================
    // Gestion du sable
    // ==================================================================
    // Ajoute une tonne de sable sur cette zone
    // Le sable ne peut pas etre ajoute sur l'oeil de la tempete
    public void addSable() {
        if (type != TypeZone.OEIL) {
            sable++;
        }
    }
    // Retire une tonne de sable de cette zone
    // Le sable ne peut pas descendre en dessous de 0
    // Retourne true si du sable a ete retire, false sinon
    public boolean retirerSable() {
        if (sable > 0) {
            sable--;
            return true;
        }
        return false;
    }

    // ==================================================================
    // Exploration
    // ==================================================================
    // Explore cette zone : la retourne et la rend visible
    // Une zone ne peut etre exploree que si elle n'est pas bloquee et pas deja exploree
    // Retourne true si l'exploration a reussi, false sinon
    public boolean explorer() {
        if (!exploree && !isBloquee()) {
            exploree = true;
            visible = true;
            return true;
        }
        return false;
    }

    // Verifie si la zone est bloquee par le sable
    // Une zone est bloquee si elle a 2 tonnes de sable ou plus
    public boolean isBloquee() {
        return sable >= 2;
    }

    // ==================================================================
    // Gestion des occupants
    // ==================================================================
    // Ajoute un joueur comme occupant de cette zone
    public void addOccupant(Joueur joueur) {
        if (!occupants.contains(joueur)) {
            occupants.add(joueur);
        }
    }
    // Retire un joueur des occupants de cette zone
    public void removeOccupant(Joueur joueur) {
        occupants.remove(joueur);
    }
    // Verifie si la zone a au moins un occupant
    public boolean aDesOccupants() {
        return !occupants.isEmpty();
    }

    // ==================================================================
    // Getters et Setters
    // ==================================================================
    // Retourne la ligne de la zone (0 a 4)
    public int getLigne() {
        return ligne;
    }
    // Modifie la ligne de la zone
    public void setLigne(int ligne) {
        this.ligne = ligne;
    }
    // Retourne la colonne de la zone (0 a 4)
    public int getColonne() {
        return colonne;
    }
    // Modifie la colonne de la zone
    public void setColonne(int colonne) {
        this.colonne = colonne;
    }
    // Retourne le niveau de sable sur cette zone
    public int getSable() {
        return sable;
    }
    // Modifie le niveau de sable sur cette zone
    public void setSable(int sable) {
        this.sable = sable;
    }
    // Indique si la zone a ete exploree
    public boolean isExploree() {
        return exploree;
    }
    // Modifie l'etat d'exploration de la zone
    public void setExploree(boolean exploree) {
        this.exploree = exploree;
    }
    // Retourne le type de cette zone
    public TypeZone getType() {
        return type;
    }
    // Modifie le type de cette zone
    public void setType(TypeZone type) {
        this.type = type;
    }
    // Indique si le type de la zone est visible
    public boolean isVisible() {
        return visible;
    }
    // Modifie la visibilite du type de la zone
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    // Retourne la liste des joueurs presents sur cette zone
    public ArrayList<Joueur> getOccupants() {
        return occupants;
    }

    // ==================================================================
    // Methodes ajoutees pour le controleur
    // ==================================================================
    // Retourne la piece disponible sur cette zone, ou null si aucune
    // Delegue la verification au modele Desert via les indices decouverts
    // Ici on retourne null car la logique des pieces est dans Desert
    public Piece getPiece() {
        return null;
    }
    // Alias pour getOccupants() - retourne la liste des joueurs sur cette zone
    public ArrayList<Joueur> getJoueurs() {
        return occupants;
    }

    // ==================================================================
    // Representation textuelle
    // ==================================================================
    // Retourne une representation textuelle de la zone
    @Override
    public String toString() {
        String s = "Zone[" + ligne + "," + colonne + "] ";
        if (visible) {
            s += type.getNom();
        } else {
            s += "???";
        }
        s += " (sable=" + sable + ")";
        if (!occupants.isEmpty()) {
            s += " occupants: " + occupants.size();
        }
        return s;
    }
}
