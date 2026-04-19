// ==================================================================
// FICHIER : VueMenu.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est le menu de configuration avant de lancer une partie
//
// Ce que fait VueMenu :
//   - On permet de saisir les noms des joueurs (2 a 5)
//   - On permet de choisir la difficulte et les roles
//   - On fournit un bouton "Jouer" pour lancer la partie
//   - On fournit un bouton "Demo" pour lancer en mode demonstration
//   - On fournit un bouton "Quitter" pour fermer l'application
//
// Structure : MVC (Vue)
// Utilise par : Controleur, Fenetre
// ==================================================================

package vue;

import modele.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class VueMenu extends JPanel {
    // Image de fond du menu (fond_menu.png)
    private Image imageFond;
    // Spinner pour le nombre de joueurs
    private JSpinner spinnerNbJoueurs;
    // Champs de texte pour les noms des joueurs (max 5)
    private JTextField[] champsNoms;
    // Labels pour les noms (pour pouvoir les cacher/afficher)
    private JLabel[] labelsNoms;
    // Panneau contenant les champs de noms
    private JPanel panneauNoms;
    // Combo box pour la difficulte
    private JComboBox<String> comboDifficulte;
    // Combo box pour les roles (une par joueur)
    private JComboBox<String>[] combosRoles;
    // Labels pour les roles (pour pouvoir les cacher/afficher)
    private JLabel[] labelsRoles;
    // Panneau contenant les combos de roles
    private JPanel panneauCombosRoles;
    // Bouton pour lancer la partie
    private JButton boutonJouer;
    // Bouton pour afficher les regles du jeu
    private JButton boutonRegles;
    // Bouton pour lancer une partie en mode demonstration (sans penalites)
    private JButton boutonDemo;
    // Bouton pour quitter l'application
    private JButton boutonQuitter;
    // Nombre maximal de joueurs
    private static final int MAX_JOUEURS = 5;

    // Constructeur du menu
    // @SuppressWarnings("unchecked")
    public VueMenu() {
        setLayout(new BorderLayout());
        setBackground(new Color(237, 201, 141));
        // Charger l'image de fond
        try {
            ImageIcon icone = new ImageIcon(getClass().getResource("/resources/images/FOND_MENU.png"));
            if (icone.getIconWidth() > 0) {
                imageFond = icone.getImage();
            }
        } catch (Exception e) {
            imageFond = null;
        }
        // Panneau central avec GridBagLayout pour un bon alignement
        JPanel panneauCentral = new JPanel(new GridBagLayout());
        panneauCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 10, 4, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 2, 10);
        JLabel titre = new JLabel("Le Desert Interdit", JLabel.CENTER);
        titre.setFont(new Font("Serif", Font.BOLD, 36));
        titre.setForeground(new Color(60, 35, 10));
        panneauCentral.add(titre, gbc);
        // Sous-titre
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 10, 10);
        JLabel sousTitre = new JLabel("Configuration de la partie", JLabel.CENTER);
        sousTitre.setFont(new Font("SansSerif", Font.ITALIC, 13));
        sousTitre.setForeground(new Color(80, 50, 25));
        panneauCentral.add(sousTitre, gbc);
        // Separateur decoratif
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 60, 8, 60);
        JSeparator sepHaut = new JSeparator();
        sepHaut.setForeground(new Color(180, 150, 110));
        panneauCentral.add(sepHaut, gbc);
        // Nombre de joueurs
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(4, 10, 4, 10);
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelNb = creerLabelMenu("Nombre de joueurs :");
        panneauCentral.add(labelNb, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        spinnerNbJoueurs = new JSpinner(new SpinnerNumberModel(2, 2, MAX_JOUEURS, 1));
        spinnerNbJoueurs.setPreferredSize(new Dimension(60, 28));
        panneauCentral.add(spinnerNbJoueurs, gbc);
        // Panneau des noms des joueurs
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panneauNoms = new JPanel(new GridLayout(MAX_JOUEURS, 2, 5, 4));
        panneauNoms.setOpaque(false);
        champsNoms = new JTextField[MAX_JOUEURS];
        labelsNoms = new JLabel[MAX_JOUEURS];
        combosRoles = new JComboBox[MAX_JOUEURS];
        labelsRoles = new JLabel[MAX_JOUEURS];
        for (int i = 0; i < MAX_JOUEURS; i++) {
            labelsNoms[i] = creerLabelMenu("Joueur " + (i + 1) + " :");
            champsNoms[i] = new JTextField("Joueur " + (i + 1), 12);
            champsNoms[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            panneauNoms.add(labelsNoms[i]);
            panneauNoms.add(champsNoms[i]);
        }
        panneauCentral.add(panneauNoms, gbc);
        // Titre des roles
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 10, 2, 10);
        JLabel labelRoleTitre = new JLabel("Roles :", JLabel.CENTER);
        labelRoleTitre.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelRoleTitre.setForeground(new Color(60, 35, 10));
        panneauCentral.add(labelRoleTitre, gbc);
        // Panneau des combos de roles
        gbc.gridy = 6;
        gbc.insets = new Insets(2, 10, 4, 10);
        // Noms des roles pour les combo box
        String[] nomsRoles = new String[Role.values().length + 1];
        nomsRoles[0] = "Aleatoire";
        for (int i = 0; i < Role.values().length; i++) {
            nomsRoles[i + 1] = Role.values()[i].getNom();
        }
        panneauCombosRoles = new JPanel(new GridLayout(MAX_JOUEURS, 2, 5, 4));
        panneauCombosRoles.setOpaque(false);
        for (int i = 0; i < MAX_JOUEURS; i++) {
            labelsRoles[i] = creerLabelMenu("Role J" + (i + 1) + " :");
            combosRoles[i] = new JComboBox<>(nomsRoles);
            panneauCombosRoles.add(labelsRoles[i]);
            panneauCombosRoles.add(combosRoles[i]);
        }
        panneauCentral.add(panneauCombosRoles, gbc);
        // Difficulte
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 10, 4, 10);
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelDiff = creerLabelMenu("Difficulte :");
        panneauCentral.add(labelDiff, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] niveaux = {"Facile", "Normal", "Difficile", "Legendaire"};
        comboDifficulte = new JComboBox<>(niveaux);
        comboDifficulte.setSelectedIndex(1); // Normal par defaut
        panneauCentral.add(comboDifficulte, gbc);
        // Separateur avant boutons
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 60, 4, 60);
        JSeparator sepBas = new JSeparator();
        sepBas.setForeground(new Color(180, 150, 110));
        panneauCentral.add(sepBas, gbc);
        // Panneau des boutons en bas (2 lignes pour eviter l'encombrement)
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(8, 10, 5, 10);
        // Premiere ligne : Regles + Jouer + Demo
        JPanel ligneBoutons1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        ligneBoutons1.setOpaque(false);
        boutonRegles = creerBoutonMenu("Regles du jeu");
        boutonRegles.addActionListener(e -> ouvrirRegles());
        ligneBoutons1.add(boutonRegles);
        boutonJouer = creerBoutonMenu("Jouer");
        boutonJouer.setActionCommand("JOUER");
        ligneBoutons1.add(boutonJouer);
        boutonDemo = creerBoutonMenu("Demo");
        boutonDemo.setActionCommand("DEMO");
        boutonDemo.setToolTipText("Lancer une partie sans penalites (pas de tempete, pas de perte d'eau)");
        ligneBoutons1.add(boutonDemo);
        panneauCentral.add(ligneBoutons1, gbc);
        // Deuxieme ligne : Quitter
        gbc.gridy = 10;
        gbc.insets = new Insets(2, 10, 15, 10);
        JPanel ligneBoutons2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        ligneBoutons2.setOpaque(false);
        boutonQuitter = creerBoutonMenu("Quitter");
        boutonQuitter.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment quitter ?",
                "Quitter", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        ligneBoutons2.add(boutonQuitter);
        panneauCentral.add(ligneBoutons2, gbc);
        add(panneauCentral, BorderLayout.CENTER);
        // Mettre a jour l'affichage des champs selon le nombre de joueurs
        mettreAJourChamps();
        // Ecouter les changements du spinner pour afficher/cacher les champs
        spinnerNbJoueurs.addChangeListener(e -> mettreAJourChamps());
    }
    // Met a jour l'affichage des champs et roles selon le nombre de joueurs selectionne
    private void mettreAJourChamps() {
        int nbJoueurs = (int) spinnerNbJoueurs.getValue();
        for (int i = 0; i < MAX_JOUEURS; i++) {
            boolean visible = (i < nbJoueurs);
            labelsNoms[i].setVisible(visible);
            champsNoms[i].setVisible(visible);
            labelsRoles[i].setVisible(visible);
            combosRoles[i].setVisible(visible);
        }
        panneauNoms.revalidate();
        panneauNoms.repaint();
        panneauCombosRoles.revalidate();
        panneauCombosRoles.repaint();
    }

    // Cree un label avec le style du menu : texte marron fonce sable, police SansSerif
    private JLabel creerLabelMenu(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(60, 35, 10));
        return label;
    }

    // Cree un bouton avec le style du menu : fond blanc/creme, texte brun fonce, coins arrondis
    // Ce style est uniforme pour tous les boutons du menu (Regles, Jouer, Demo, Quitter)
    private JButton creerBoutonMenu(String texte) {
        JButton bouton = new JButton(texte) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond blanc/creme avec coins arrondis
                g2d.setColor(new Color(255, 252, 245));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Bordure fine brun clair
                g2d.setColor(new Color(180, 160, 130));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
                // Dessiner le texte par-dessus
                super.paintComponent(g);
            }
        };
        bouton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bouton.setPreferredSize(new Dimension(160, 40));
        bouton.setForeground(new Color(80, 50, 20));
        bouton.setContentAreaFilled(false);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setOpaque(false);
        return bouton;
    }

    // Dessine l'image de fond si disponible
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageFond != null) {
            g.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // ==================== Accesseurs pour le Controleur ====================
    // Retourne le bouton Jouer
    public JButton getBoutonJouer() {
        return boutonJouer;
    }
    // Retourne le bouton Demo
    public JButton getBoutonDemo() {
        return boutonDemo;
    }
    // Retourne la liste des noms des joueurs saisis
    public ArrayList<String> getNomsJoueurs() {
        int nbJoueurs = (int) spinnerNbJoueurs.getValue();
        ArrayList<String> noms = new ArrayList<>();

        for (int i = 0; i < nbJoueurs; i++) {
            String nom = champsNoms[i].getText().trim();
            if (!nom.isEmpty()) {
                noms.add(nom);
            }
        }
        return noms;
    }
    // Retourne la difficulte choisie sous forme de double
    // Facile = 1.0, Normal = 2.0, Difficile = 3.0, Legendaire = 4.0
    public double getDifficulte() {
        int index = comboDifficulte.getSelectedIndex();
        switch (index) {
            case 0: return 1.0;   // Facile
            case 1: return 2.0;   // Normal
            case 2: return 3.0;   // Difficile
            case 3: return 4.0;   // Legendaire
            default: return 2.0;
        }
    }
    // Retourne le bouton Regles
    public JButton getBoutonRegles() {
        return boutonRegles;
    }

    // ==================================================================
    // OUVERTURE DES REGLES
    // ==================================================================
    // Ouvre le fichier regles.pdf avec le lecteur PDF du systeme
    private void ouvrirRegles() {
        try {
            // Chercher le fichier regles.pdf dans plusieurs emplacements possibles
            File fichierRegles = new File("regles.pdf");
            if (!fichierRegles.exists()) {
                fichierRegles = new File("src/resources/regles.pdf");
            }
            if (!fichierRegles.exists()) {
                fichierRegles = new File("resources/regles.pdf");
            }
            if (fichierRegles.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fichierRegles);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Le fichier regles.pdf est introuvable.\n"
                    + "Placez-le a la racine du projet.",
                    "Fichier introuvable",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Impossible d'ouvrir le fichier regles.pdf :\n" + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    // Retourne les roles choisis par les joueurs
    // Si "Aleatoire" est selectionne, on met null dans la liste
    public ArrayList<Role> getRolesChoisis() {
        int nbJoueurs = (int) spinnerNbJoueurs.getValue();
        ArrayList<Role> roles = new ArrayList<>();
        for (int i = 0; i < nbJoueurs; i++) {
            String selection = (String) combosRoles[i].getSelectedItem();
            if (selection == null || selection.equals("Aleatoire")) {
                roles.add(null);
            } else {
                // Retrouver le Role correspondant au nom
                Role roleChoisi = null;
                for (Role r : Role.values()) {
                    if (r.getNom().equals(selection)) {
                        roleChoisi = r;
                        break;
                    }
                }
                roles.add(roleChoisi);
            }
        }
        return roles;
    }
}
