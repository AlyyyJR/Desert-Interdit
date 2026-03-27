// ==================================================================
// FICHIER : VueJoueurs.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est le panneau qui affiche les infos de chaque joueur
//
// Ce que fait VueJoueurs :
//   - On affiche le nom, le niveau d'eau, les equipements et le role
//   - On met en evidence le joueur actif
//   - On se met a jour automatiquement quand le modele change (Observer)
//
// Structure : MVC (Vue)
// Utilise par : Controleur, Fenetre
// ==================================================================

package vue;

import modele.Joueur;
import modele.Role;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class VueJoueurs extends JPanel implements Observer {
    // Panneau interne qui contient les cartes des joueurs
    private JPanel panneauJoueurs;
    // Cache des images de roles
    private HashMap<String, ImageIcon> cacheImagesRoles;
    // Couleurs pour les joueurs (meme ordre que VuePlateau)
    private static final Color[] COULEURS_JOUEURS = {
        new Color(220, 50, 50),   // rouge
        new Color(50, 100, 220),  // bleu
        new Color(50, 180, 50),   // vert
        new Color(220, 180, 30),  // jaune
        new Color(180, 50, 180)   // violet
    };
    // Constructeur du panneau des joueurs
    public VueJoueurs() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 40, 30));
        setPreferredSize(new Dimension(0, 110));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        // Charger les images de roles
        cacheImagesRoles = new HashMap<>();
        String[] nomsRoles = {"ALPIN", "ARCHEO", "EXPLO", "METEO", "NAVIG", "PORTEUR"};
        for (String nom : nomsRoles) {
            try {
                ImageIcon icone = new ImageIcon(
                    getClass().getResource("/resources/images/" + nom + ".png"));
                if (icone.getIconWidth() > 0) {
                    Image img = icone.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    cacheImagesRoles.put(nom, new ImageIcon(img));
                }
            } catch (Exception e) {
                // Image non trouvee, on continue
            }
        }
        // Titre
        JLabel titre = new JLabel("Joueurs");
        titre.setFont(new Font("SansSerif", Font.BOLD, 14));
        titre.setForeground(new Color(220, 190, 140));
        add(titre, BorderLayout.NORTH);
        // Panneau interne pour les infos joueurs
        panneauJoueurs = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panneauJoueurs.setBackground(new Color(50, 40, 30));
        add(panneauJoueurs, BorderLayout.CENTER);
    }
    // Met a jour l'affichage des joueurs
    // joueurs     : la liste de tous les joueurs
    // joueurActif : le joueur dont c'est le tour
    public void mettreAJour(ArrayList<Joueur> joueurs, Joueur joueurActif) {
        panneauJoueurs.removeAll();
        if (joueurs == null) {
            panneauJoueurs.revalidate();
            panneauJoueurs.repaint();
            return;
        }
        for (int i = 0; i < joueurs.size(); i++) {
            Joueur joueur = joueurs.get(i);
            boolean estActif = (joueur == joueurActif);

            JPanel carteJoueur = creerCarteJoueur(joueur, i, estActif);
            panneauJoueurs.add(carteJoueur);
        }
        panneauJoueurs.revalidate();
        panneauJoueurs.repaint();
    }
    // Cree un petit panneau pour afficher les infos d'un joueur
    private JPanel creerCarteJoueur(Joueur joueur, int index, boolean estActif) {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setPreferredSize(new Dimension(160, 85));
        // Fond selon si le joueur est actif ou non
        if (estActif) {
            carte.setBackground(new Color(80, 70, 50));
            carte.setBorder(BorderFactory.createLineBorder(
                COULEURS_JOUEURS[index % COULEURS_JOUEURS.length], 2));
        } else {
            carte.setBackground(new Color(60, 50, 40));
            carte.setBorder(BorderFactory.createLineBorder(new Color(100, 90, 70), 1));
        }
        // Nom du joueur
        JLabel labelNom = new JLabel(" " + joueur.getNom());
        labelNom.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelNom.setForeground(COULEURS_JOUEURS[index % COULEURS_JOUEURS.length]);
        carte.add(labelNom);
        // Niveau d'eau
        String texteEau = " Eau : " + joueur.getEau() + "/" + Joueur.EAU_MAX;
        JLabel labelEau = new JLabel(texteEau);
        labelEau.setFont(new Font("SansSerif", Font.PLAIN, 11));
        // Couleur rouge si eau basse
        if (joueur.getEau() <= 1) {
            labelEau.setForeground(new Color(255, 80, 80));
        } else {
            labelEau.setForeground(Color.WHITE);
        }
        carte.add(labelEau);
        // Nombre d'equipements
        int nbEquip = joueur.getEquipements().size();
        JLabel labelEquip = new JLabel(" Equipements : " + nbEquip);
        labelEquip.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelEquip.setForeground(Color.WHITE);
        carte.add(labelEquip);
        // Role du joueur avec icone
        Role role = joueur.getRole();
        if (role != null) {
            JPanel panneauRole = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            panneauRole.setOpaque(false);
            // Trouver l'image du role
            String cle = nomImageRole(role);
            ImageIcon iconeRole = cacheImagesRoles.get(cle);
            if (iconeRole != null) {
                JLabel labelIcone = new JLabel(iconeRole);
                panneauRole.add(labelIcone);
            }
            JLabel labelRole = new JLabel(role.getNom());
            labelRole.setFont(new Font("SansSerif", Font.ITALIC, 10));
            labelRole.setForeground(new Color(200, 200, 150));
            panneauRole.add(labelRole);
            carte.add(panneauRole);
        }
        return carte;
    }
    // Retourne le nom d'image correspondant au role
    private String nomImageRole(Role role) {
        switch (role) {
            case ALPINISTE: return "ALPIN";
            case ARCHEOLOGUE: return "ARCHEO";
            case EXPLORATEUR: return "EXPLO";
            case METEOROLOGUE: return "METEO";
            case NAVIGATRICE: return "NAVIG";
            case PORTEUSE_EAU: return "PORTEUR";
            default: return "";
        }
    }
    // Mise a jour automatique quand le modele change
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
