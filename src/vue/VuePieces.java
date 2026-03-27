// ==================================================================
// FICHIER : VuePieces.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est le panneau qui affiche les pieces de la machine recuperees
//
// Ce que fait VuePieces :
//   - On affiche les 4 emplacements (Moteur, Helice, Gouvernail, Capteur)
//   - On met en evidence les pieces deja recuperees
//   - On se met a jour automatiquement quand le modele change (Observer)
//
// Structure : MVC (Vue)
// Utilise par : Controleur, Fenetre
// ==================================================================

package vue;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class VuePieces extends JPanel implements Observer {
    // Noms des 4 pieces de la machine volante
    private static final String[] NOMS_PIECES = {
        "Moteur", "Helice", "Gouvernail", "Capteur"
    };
    // Etat des pieces (true = recuperee)
    private boolean[] piecesRecuperees;
    // Labels pour afficher chaque piece
    private JLabel[] labelsPieces;
    // Constructeur du panneau des pieces
    public VuePieces() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(60, 50, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Initialiser l'etat : aucune piece recuperee
        piecesRecuperees = new boolean[4];
        labelsPieces = new JLabel[4];
        // Titre
        JLabel titre = new JLabel("Pieces de la machine");
        titre.setFont(new Font("SansSerif", Font.BOLD, 14));
        titre.setForeground(new Color(220, 190, 140));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titre);
        add(Box.createVerticalStrut(8));
        // Creer les labels pour chaque piece
        for (int i = 0; i < 4; i++) {
            labelsPieces[i] = new JLabel("  [ ] " + NOMS_PIECES[i]);
            labelsPieces[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            labelsPieces[i].setForeground(new Color(200, 175, 130));
            labelsPieces[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            add(labelsPieces[i]);
            add(Box.createVerticalStrut(3));
        }
    }
    // Met a jour l'affichage des pieces recuperees
    // piecesRecuperees : tableau de 4 booleens (true = piece recuperee)
    public void mettreAJour(boolean[] piecesRecuperees) {
        if (piecesRecuperees == null || piecesRecuperees.length < 4) {
            return;
        }
        this.piecesRecuperees = piecesRecuperees;
        for (int i = 0; i < 4; i++) {
            if (piecesRecuperees[i]) {
                // Piece recuperee : afficher en vert avec coche
                labelsPieces[i].setText("  [X] " + NOMS_PIECES[i]);
                labelsPieces[i].setForeground(new Color(100, 220, 100));
                labelsPieces[i].setFont(new Font("SansSerif", Font.BOLD, 12));
            } else {
                // Piece pas encore recuperee
                labelsPieces[i].setText("  [ ] " + NOMS_PIECES[i]);
                labelsPieces[i].setForeground(new Color(200, 175, 130));
                labelsPieces[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            }
        }
        repaint();
    }
    // Mise a jour automatique quand le modele change
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
