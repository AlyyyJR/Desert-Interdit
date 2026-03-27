// ==================================================================
// FICHIER : VueFinDePartie.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est l'ecran de fin de partie (victoire ou defaite)
//
// Ce que fait VueFinDePartie :
//   - On affiche un message de victoire ou de defaite
//   - On affiche la raison de la fin de partie
//   - On fournit un bouton "Revenir au menu" pour relancer une partie
//   - On fournit un bouton "Quitter" pour fermer l'application
//
// Structure : MVC (Vue)
// Utilise par : Controleur, Fenetre
// ==================================================================

package vue;

import javax.swing.*;
import java.awt.*;

public class VueFinDePartie extends JPanel {
    // Label principal (victoire ou defaite)
    private JLabel labelResultat;
    // Label de la raison de la fin
    private JLabel labelRaison;
    // Label decoratif
    private JLabel labelDecor;
    // Bouton pour revenir au menu principal
    private JButton boutonRetourMenu;
    // Bouton pour quitter l'application
    private JButton boutonQuitter;
    // Constructeur de l'ecran de fin
    public VueFinDePartie() {
        setLayout(new GridBagLayout());
        setBackground(new Color(40, 30, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        // Label decoratif en haut
        gbc.gridy = 0;
        labelDecor = new JLabel("Fin de la partie");
        labelDecor.setFont(new Font("SansSerif", Font.BOLD, 18));
        labelDecor.setForeground(new Color(200, 180, 140));
        add(labelDecor, gbc);
        // Label du resultat (victoire/defaite)
        gbc.gridy = 1;
        labelResultat = new JLabel(" ");
        labelResultat.setFont(new Font("SansSerif", Font.BOLD, 36));
        labelResultat.setForeground(Color.WHITE);
        add(labelResultat, gbc);
        // Label de la raison
        gbc.gridy = 2;
        labelRaison = new JLabel(" ");
        labelRaison.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelRaison.setForeground(new Color(220, 200, 160));
        add(labelRaison, gbc);
        // Panneau des boutons (Revenir au menu + Quitter)
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 20, 10, 20);
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panneauBoutons.setOpaque(false);
        // Bouton Revenir au menu — retourne a l'ecran de configuration
        boutonRetourMenu = creerBoutonFin("Revenir au menu");
        boutonRetourMenu.setActionCommand("RETOUR_MENU_FIN");
        panneauBoutons.add(boutonRetourMenu);
        // Bouton Quitter — ferme l'application
        boutonQuitter = creerBoutonFin("Quitter");
        boutonQuitter.addActionListener(e -> System.exit(0));
        panneauBoutons.add(boutonQuitter);
        add(panneauBoutons, gbc);
    }
    // Cree un bouton avec le style blanc/creme arrondi pour l'ecran de fin
    private JButton creerBoutonFin(String texte) {
        JButton bouton = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 252, 245));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(180, 160, 130));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        bouton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bouton.setPreferredSize(new Dimension(180, 40));
        bouton.setForeground(new Color(80, 50, 20));
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setOpaque(false);
        return bouton;
    }
    // Retourne le bouton Revenir au menu
    public JButton getBoutonRetourMenu() {
        return boutonRetourMenu;
    }
    // Affiche le resultat de la partie
    // victoire : true si les joueurs ont gagne, false sinon
    // raison   : explication de la fin de partie
    public void afficher(boolean victoire, String raison) {
        if (victoire) {
            labelResultat.setText("VICTOIRE !");
            labelResultat.setForeground(new Color(100, 220, 100));
            setBackground(new Color(20, 50, 20));
            labelDecor.setText("Felicitations !");
        } else {
            labelResultat.setText("DEFAITE...");
            labelResultat.setForeground(new Color(220, 80, 80));
            setBackground(new Color(50, 20, 20));
            labelDecor.setText("Le desert a gagne...");
        }
        if (raison != null && !raison.isEmpty()) {
            labelRaison.setText(raison);
        } else {
            labelRaison.setText(" ");
        }
        revalidate();
        repaint();
    }
}
