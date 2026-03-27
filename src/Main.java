// ==================================================================
// FICHIER : Main.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Point d'entree de notre programme
//
// Ici on cree le modele (Desert), la vue (Fenetre) et le
// controleur, puis on affiche la fenetre principale
//
// On lance tout dans le thread Swing (SwingUtilities) pour
// respecter les bonnes pratiques de la programmation graphique
//
// Structure : MVC (lancement)
// ==================================================================

import modele.Desert;
import vue.Fenetre;
import controlleur.Controleur;

import javax.swing.SwingUtilities;

public class Main {
    // Methode principale : lance l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Creation du modele
            Desert modele = new Desert();
            // Creation de la vue (fenetre principale)
            Fenetre fenetre = new Fenetre(modele);
            // Creation du controleur qui relie modele et vue
            Controleur controleur = new Controleur(modele, fenetre);
            // Affichage de la fenetre
            fenetre.setVisible(true);
        });
    }
}
