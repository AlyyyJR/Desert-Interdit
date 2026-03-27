// ==================================================================
// FICHIER : VueActions.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est le panneau des boutons d'actions du joueur
//
// Ce que fait VueActions :
//   - On affiche les boutons d'actions (deplacer, creuser, explorer, etc.)
//   - On affiche les infos du tour (actions restantes, joueur actif)
//   - On affiche le niveau de tempete et le sable total
//   - On fournit des boutons pour revenir au menu ou quitter
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

public class VueActions extends JPanel implements Observer {
    // Boutons d'actions de jeu
    private JButton boutonDeplacer;
    private JButton boutonCreuser;
    private JButton boutonExplorer;
    private JButton boutonRamasser;
    private JButton boutonDonnerEau;
    private JButton boutonPrendreEau;
    private JButton boutonFinTour;
    private JButton boutonUtiliserEquipement;
    // Boutons de navigation
    private JButton boutonRetourMenu;
    private JButton boutonQuitter;
    // Labels d'informations
    private JLabel labelJoueurActif;
    private JLabel labelRoleActif;
    private JLabel labelActionsRestantes;
    private JLabel labelNiveauTempete;
    private JLabel labelSableTotal;
    private JLabel labelMessage;
    // Constructeur du panneau d'actions
    public VueActions() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(60, 50, 40));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Titre du panneau
        JLabel titre = new JLabel("Actions");
        titre.setFont(new Font("SansSerif", Font.BOLD, 16));
        titre.setForeground(new Color(220, 190, 140));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titre);
        add(Box.createVerticalStrut(8));
        // Labels d'informations
        labelJoueurActif = creerLabel("Joueur : -");
        add(labelJoueurActif);
        add(Box.createVerticalStrut(3));
        labelRoleActif = creerLabel("Role : -");
        labelRoleActif.setForeground(new Color(255, 200, 100));
        labelRoleActif.setFont(new Font("SansSerif", Font.ITALIC, 11));
        add(labelRoleActif);
        add(Box.createVerticalStrut(3));
        labelActionsRestantes = creerLabel("Actions : -");
        add(labelActionsRestantes);
        add(Box.createVerticalStrut(3));
        labelNiveauTempete = creerLabel("Tempete : -");
        add(labelNiveauTempete);
        add(Box.createVerticalStrut(3));
        labelSableTotal = creerLabel("Sable total : -");
        add(labelSableTotal);
        add(Box.createVerticalStrut(8));
        // Separator
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        add(sep);
        add(Box.createVerticalStrut(6));
        // Creer les boutons d'actions
        boutonDeplacer = creerBouton("Deplacer", "DEPLACER");
        add(boutonDeplacer);
        add(Box.createVerticalStrut(3));
        boutonCreuser = creerBouton("Creuser", "CREUSER");
        add(boutonCreuser);
        add(Box.createVerticalStrut(3));
        boutonExplorer = creerBouton("Explorer", "EXPLORER");
        add(boutonExplorer);
        add(Box.createVerticalStrut(3));
        boutonRamasser = creerBouton("Ramasser", "RAMASSER");
        add(boutonRamasser);
        add(Box.createVerticalStrut(3));
        boutonDonnerEau = creerBouton("Donner eau", "DONNER_EAU");
        add(boutonDonnerEau);
        add(Box.createVerticalStrut(3));
        boutonPrendreEau = creerBouton("Prendre eau", "PRENDRE_EAU");
        add(boutonPrendreEau);
        add(Box.createVerticalStrut(3));
        boutonUtiliserEquipement = creerBouton("Utiliser equipement", "UTILISER_EQUIPEMENT");
        add(boutonUtiliserEquipement);
        add(Box.createVerticalStrut(6));
        // Separator avant fin de tour
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        add(sep2);
        add(Box.createVerticalStrut(8));
        boutonFinTour = creerBouton("Fin de tour", "FIN_TOUR");
        add(boutonFinTour);
        add(Box.createVerticalStrut(8));
        // Separator avant navigation
        JSeparator sep3 = new JSeparator();
        sep3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        add(sep3);
        add(Box.createVerticalStrut(8));
        // Boutons de navigation (retour menu + quitter)
        boutonRetourMenu = creerBouton("Retour menu", "RETOUR_MENU");
        add(boutonRetourMenu);
        add(Box.createVerticalStrut(3));
        boutonQuitter = creerBouton("Quitter", "QUITTER_JEU");
        boutonQuitter.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment quitter la partie ?",
                "Quitter", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        add(boutonQuitter);
        // Label de message (invisible mais accessible par le controleur)
        labelMessage = creerLabel(" ");
        labelMessage.setForeground(new Color(255, 200, 100));
    }
    // Cree un label avec le style du panneau (couleur sable)
    private JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(new Color(210, 185, 140));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    // Cree un bouton avec la commande d'action et le style blanc/creme arrondi
    private JButton creerBouton(String texte, String commande) {
        JButton bouton = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond blanc/creme avec coins arrondis
                g2d.setColor(new Color(255, 252, 245));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // Bordure fine brun clair
                g2d.setColor(new Color(180, 160, 130));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        bouton.setActionCommand(commande);
        bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bouton.setMaximumSize(new Dimension(200, 30));
        bouton.setFont(new Font("SansSerif", Font.BOLD, 12));
        bouton.setForeground(new Color(80, 50, 20));
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setOpaque(false);
        return bouton;
    }

    // ==================== Methodes de mise a jour ====================
    // Met a jour le nombre d'actions restantes
    public void setActionsRestantes(int actions) {
        labelActionsRestantes.setText("Actions : " + actions);
    }
    // Met a jour le nom du joueur actif
    public void setNomJoueurActif(String nom) {
        labelJoueurActif.setText("Joueur : " + nom);
    }
    // Met a jour le role du joueur actif
    public void setRoleJoueurActif(String role) {
        labelRoleActif.setText("Role : " + role);
    }
    // Met a jour le niveau de tempete
    public void setNiveauTempete(double niveau) {
        labelNiveauTempete.setText("Tempete : " + niveau);
    }
    // Met a jour le sable total sur le plateau
    public void setSableTotalInfo(int sable) {
        labelSableTotal.setText("Sable total : " + sable);
    }
    // Affiche un message d'information
    public void setMessageInfo(String message) {
        labelMessage.setText(message);
    }

    // ==================== Accesseurs des boutons ====================
    // Retourne le bouton Deplacer
    public JButton getBoutonDeplacer() {
        return boutonDeplacer;
    }
    // Retourne le bouton Creuser
    public JButton getBoutonCreuser() {
        return boutonCreuser;
    }
    // Retourne le bouton Explorer
    public JButton getBoutonExplorer() {
        return boutonExplorer;
    }
    // Retourne le bouton Ramasser
    public JButton getBoutonRamasser() {
        return boutonRamasser;
    }
    // Retourne le bouton Donner eau
    public JButton getBoutonDonnerEau() {
        return boutonDonnerEau;
    }
    // Retourne le bouton Prendre eau
    public JButton getBoutonPrendreEau() {
        return boutonPrendreEau;
    }
    // Retourne le bouton Fin de tour
    public JButton getBoutonFinTour() {
        return boutonFinTour;
    }
    // Retourne le bouton Utiliser equipement
    public JButton getBoutonUtiliserEquipement() {
        return boutonUtiliserEquipement;
    }
    // Retourne le bouton Retour au menu
    public JButton getBoutonRetourMenu() {
        return boutonRetourMenu;
    }
    // Retourne le bouton Quitter
    public JButton getBoutonQuitter() {
        return boutonQuitter;
    }
    // Mise a jour automatique quand le modele change
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
