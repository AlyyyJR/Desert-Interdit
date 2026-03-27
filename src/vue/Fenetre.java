// ==================================================================
// FICHIER : Fenetre.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est la fenetre principale de notre application
//
// Ce que fait Fenetre :
//   - On contient toutes les vues du jeu dans un CardLayout
//   - On bascule entre le menu, le jeu et l'ecran de fin
//   - On fournit les accesseurs vers chaque sous-vue pour le controleur
//
// Structure : MVC (Vue)
// Utilise par : Controleur
// ==================================================================

package vue;

import modele.Desert;

import javax.swing.*;
import java.awt.*;

public class Fenetre extends JFrame {
    // Le CardLayout pour basculer entre les ecrans
    private CardLayout cardLayout;
    // Le panneau principal qui contient les cartes
    private JPanel panneauPrincipal;
    // Les differentes vues
    private VuePlateau vuePlateau;
    private VueJoueurs vueJoueurs;
    private VuePieces vuePieces;
    private VueActions vueActions;
    private VueMenu vueMenu;
    private VueFinDePartie vueFinDePartie;
    // Panneau de jeu (contient plateau + panneaux lateraux)
    private JPanel panneauJeu;

    // Constructeur de la fenetre principale
    public Fenetre(Desert desert) {
        super("Le Desert Interdit");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setLocationRelativeTo(null);
        setResizable(false);
        // Initialiser le CardLayout
        cardLayout = new CardLayout();
        panneauPrincipal = new JPanel(cardLayout);
        // Creer les vues
        vuePlateau = new VuePlateau(desert);
        vueJoueurs = new VueJoueurs();
        vuePieces = new VuePieces();
        vueActions = new VueActions();
        vueMenu = new VueMenu();
        vueFinDePartie = new VueFinDePartie();
        // Construire le panneau de jeu
        panneauJeu = construirePanneauJeu();
        // Ajouter les cartes au panneau principal
        panneauPrincipal.add(vueMenu, "MENU");
        panneauPrincipal.add(panneauJeu, "JEU");
        panneauPrincipal.add(vueFinDePartie, "FIN");
        // Ajouter le panneau principal a la fenetre
        setContentPane(panneauPrincipal);
        // Afficher le menu par defaut
        afficherMenu();
    }

    // Construit le panneau de jeu avec le plateau au centre
    // et les panneaux d'info sur les cotes
    private JPanel construirePanneauJeu() {
        JPanel panneau = new JPanel(new BorderLayout(5, 5));
        panneau.setBackground(new Color(50, 40, 30));
        // Le plateau au centre
        JPanel centrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centrePanel.setBackground(new Color(50, 40, 30));
        centrePanel.add(vuePlateau);
        panneau.add(centrePanel, BorderLayout.CENTER);
        // Panneau droit : actions + pieces
        JPanel panneauDroit = new JPanel();
        panneauDroit.setLayout(new BoxLayout(panneauDroit, BoxLayout.Y_AXIS));
        panneauDroit.setPreferredSize(new Dimension(270, 0));
        panneauDroit.setBackground(new Color(60, 50, 40));
        panneauDroit.add(vueActions);
        panneauDroit.add(Box.createVerticalStrut(5));
        panneauDroit.add(vuePieces);
        panneau.add(panneauDroit, BorderLayout.EAST);
        // Panneau bas : infos joueurs
        panneau.add(vueJoueurs, BorderLayout.SOUTH);
        return panneau;
    }

    // Affiche l'ecran de jeu
    public void afficherJeu() {
        cardLayout.show(panneauPrincipal, "JEU");
    }

    // Affiche le menu principal
    public void afficherMenu() {
        cardLayout.show(panneauPrincipal, "MENU");
    }

    // Affiche l'ecran de fin de partie
    public void afficherFinDePartie(boolean victoire, String raison) {
        vueFinDePartie.afficher(victoire, raison);
        cardLayout.show(panneauPrincipal, "FIN");
    }

    // ==================== Accesseurs ====================
    // Retourne la vue du plateau
    public VuePlateau getVuePlateau() {
        return vuePlateau;
    }
    // Retourne la vue des joueurs
    public VueJoueurs getVueJoueurs() {
        return vueJoueurs;
    }
    // Retourne la vue des pieces
    public VuePieces getVuePieces() {
        return vuePieces;
    }
    // Retourne la vue des actions
    public VueActions getVueActions() {
        return vueActions;
    }
    // Retourne la vue du menu
    public VueMenu getVueMenu() {
        return vueMenu;
    }
    // Retourne la vue de fin de partie
    public VueFinDePartie getVueFinDePartie() {
        return vueFinDePartie;
    }
}
