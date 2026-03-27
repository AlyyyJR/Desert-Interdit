// ==================================================================
// FICHIER : PlateauListener.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Interface d'ecoute pour les clics sur le plateau
//
// Ce que fait PlateauListener :
//   - Ca permet au controleur de recevoir les clics sur les zones
//   - Ca decouple la vue du controleur
//
// Structure : MVC (Interface Vue -> Controleur)
// Utilise par : VuePlateau, Controleur
// ==================================================================

package vue;

public interface PlateauListener {
    // Appelee quand un joueur clique sur une zone du plateau
    // ligne   : la ligne de la zone cliquee (0 a 4)
    // colonne : la colonne de la zone cliquee (0 a 4)
    void zoneCliquee(int ligne, int colonne);
}
