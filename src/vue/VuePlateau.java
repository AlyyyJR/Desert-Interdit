// ==================================================================
// FICHIER : VuePlateau.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est la vue graphique du plateau de jeu (grille 5x5)
//
// Ce que fait VuePlateau :
//   - On affiche la grille 5x5 avec un rendu personnalise
//   - On dessine les joueurs, le sable, les indices, les pieces et l'oeil
//   - On surligne les zones valides pour un deplacement ou une action
//   - On transmet les clics de souris au controleur via PlateauListener
//   - On se met a jour automatiquement quand le modele change (Observer)
//
// Structure : MVC (Vue)
// Utilise par : Controleur, Fenetre
// ==================================================================

package vue;

import modele.Desert;
import modele.Zone;
import modele.TypeZone;
import modele.Joueur;
import modele.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class VuePlateau extends JPanel implements Observer {
    // Reference vers le modele du desert
    private Desert desert;
    // Taille en pixels d'une tuile du plateau
    private static final int TAILLE_TUILE = 120;
    // Marge autour du plateau en pixels
    private static final int MARGE = 10;
    // Nombre de lignes et colonnes du plateau
    private static final int TAILLE_GRILLE = 5;
    // Liste des zones a surligner (zones de deplacement valides)
    private ArrayList<int[]> zonesSurlignees;
    // Ecouteur pour les clics sur le plateau
    private PlateauListener plateauListener;
    // Cache des images chargees depuis les ressources
    private HashMap<String, Image> cacheImages;
    // Couleurs associees a chaque joueur
    private static final Color[] COULEURS_JOUEURS = {
        new Color(220, 50, 50),   // rouge
        new Color(50, 100, 220),  // bleu
        new Color(50, 180, 50),   // vert
        new Color(220, 180, 30),  // jaune
        new Color(180, 50, 180)   // violet
    };
    // Couleur de fond sable normal
    private static final Color COULEUR_SABLE = new Color(237, 201, 141);
    // Couleur de fond sable accumule
    private static final Color COULEUR_SABLE_FONCE = new Color(194, 154, 90);
    // Couleur de fond oasis exploree
    private static final Color COULEUR_OASIS = new Color(100, 180, 220);
    // Couleur de fond tunnel explore
    private static final Color COULEUR_TUNNEL = new Color(160, 130, 100);
    // Couleur de fond zone non exploree
    private static final Color COULEUR_INEXPLOREE = new Color(180, 150, 100);
    // Couleur du surlignage des zones valides
    private static final Color COULEUR_SURLIGNAGE = new Color(50, 200, 50, 150);
    // Couleur de la grille
    private static final Color COULEUR_GRILLE = new Color(100, 70, 40);
    // Constructeur avec le modele du desert
    public VuePlateau(Desert desert) {
        this.desert = desert;
        this.zonesSurlignees = new ArrayList<>();
        this.cacheImages = new HashMap<>();
        // Dimensions du panneau
        int largeur = TAILLE_GRILLE * TAILLE_TUILE + 2 * MARGE;
        int hauteur = TAILLE_GRILLE * TAILLE_TUILE + 2 * MARGE;
        setPreferredSize(new Dimension(largeur, hauteur));
        setBackground(new Color(240, 220, 180));
        // Charger toutes les images necessaires
        chargerImages();
        // Ajouter le listener de souris pour detecter les clics sur les zones
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int colonne = (e.getX() - MARGE) / TAILLE_TUILE;
                int ligne = (e.getY() - MARGE) / TAILLE_TUILE;

                // Verifier que le clic est dans les limites de la grille
                if (ligne >= 0 && ligne < TAILLE_GRILLE
                        && colonne >= 0 && colonne < TAILLE_GRILLE) {
                    if (plateauListener != null) {
                        plateauListener.zoneCliquee(ligne, colonne);
                    }
                }
            }
        });
    }
    // Constructeur par defaut sans modele
    public VuePlateau() {
        this(null);
    }
    // Definit le modele du desert a afficher
    public void setDesert(Desert desert) {
        this.desert = desert;
        repaint();
    }
    // Charge toutes les images necessaires depuis le dossier de ressources
    private void chargerImages() {
        String[] nomsImages = {
            "OEIL", "CRASH", "PISTE", "OASIS", "MIRAGE", "TUNNEL",
            "ENGRENAGE", "SABLE0", "SABLE1", "SABLE2", "CASEVIDE", "EAU",
            "DOS_CARTE",
            "INDICE00", "INDICE01", "INDICE10", "INDICE11",
            "INDICE20", "INDICE21", "INDICE30", "INDICE31",
            "PIECE0", "PIECE1", "PIECE2", "PIECE3",
            "JOUEUR", "JOUEUR_COURANT"
        };
        for (String nom : nomsImages) {
            try {
                ImageIcon icone = new ImageIcon(
                    getClass().getResource("/resources/images/" + nom + ".png"));
                if (icone.getIconWidth() > 0) {
                    cacheImages.put(nom, icone.getImage());
                }
            } catch (Exception e) {
                // Image non trouvee, on continue sans elle
                System.err.println("Image non trouvee : " + nom + ".png");
            }
        }
    }
    // Recupere une image depuis le cache (null si non disponible)
    private Image getImage(String nom) {
        return cacheImages.get(nom);
    }
    // Definit l'ecouteur pour les clics sur le plateau
    public void setPlateauListener(PlateauListener listener) {
        this.plateauListener = listener;
    }
    // Surligne les zones valides (version ArrayList)
    public void surlignerZones(ArrayList<int[]> zones) {
        this.zonesSurlignees = new ArrayList<>(zones);
        repaint();
    }
    // Surligne les zones valides (version List, utilise par le Controleur)
    public void surlignerZones(List<int[]> zones) {
        this.zonesSurlignees = new ArrayList<>(zones);
        repaint();
    }
    // Efface tous les surlignages du plateau
    public void effacerSurlignage() {
        this.zonesSurlignees.clear();
        repaint();
    }
    // Alias pour effacerSurlignage, utilise par le Controleur
    public void effacerSurbrillance() {
        effacerSurlignage();
    }
    // Mise a jour automatique quand le modele notifie ses observateurs
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
    // Dessine le plateau complet avec toutes les zones, joueurs et indicateurs
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Activer l'anti-aliasing pour un rendu plus joli
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Si pas de modele, dessiner un plateau vide
        if (desert == null) {
            dessinerPlateauVide(g2d);
            return;
        }
        // Dessiner chaque zone de la grille 5x5
        for (int ligne = 0; ligne < TAILLE_GRILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE_GRILLE; colonne++) {
                int x = MARGE + colonne * TAILLE_TUILE;
                int y = MARGE + ligne * TAILLE_TUILE;
                dessinerZone(g2d, ligne, colonne, x, y);
            }
        }
        // Dessiner les surlignages par-dessus
        dessinerSurlignages(g2d);
    }
    // Dessine un plateau vide quand le modele n'est pas encore initialise
    private void dessinerPlateauVide(Graphics2D g2d) {
        for (int ligne = 0; ligne < TAILLE_GRILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE_GRILLE; colonne++) {
                int x = MARGE + colonne * TAILLE_TUILE;
                int y = MARGE + ligne * TAILLE_TUILE;
                g2d.setColor(COULEUR_INEXPLOREE);
                g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
                g2d.setColor(COULEUR_GRILLE);
                g2d.drawRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
                // Dessiner un point d'interrogation
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 36));
                FontMetrics fm = g2d.getFontMetrics();
                String texte = "?";
                int tx = x + (TAILLE_TUILE - fm.stringWidth(texte)) / 2;
                int ty = y + (TAILLE_TUILE + fm.getAscent()) / 2 - 5;
                g2d.drawString(texte, tx, ty);
            }
        }
    }
    // Dessine une zone individuelle du plateau a la position donnee
    private void dessinerZone(Graphics2D g2d, int ligne, int colonne, int x, int y) {
        Zone zone = null;
        try {
            zone = desert.getZone(ligne, colonne);
        } catch (Exception e) {
            // Zone inaccessible, dessiner une case vide
            g2d.setColor(COULEUR_INEXPLOREE);
            g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
            g2d.setColor(COULEUR_GRILLE);
            g2d.drawRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
            return;
        }
        if (zone == null) {
            g2d.setColor(COULEUR_INEXPLOREE);
            g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
            g2d.setColor(COULEUR_GRILLE);
            g2d.drawRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
            return;
        }
        // Fond de la zone
        dessinerFondZone(g2d, zone, x, y);
        // Icone de la zone si exploree
        dessinerIconeZone(g2d, zone, x, y);
        // Indicateur de sable
        dessinerSable(g2d, zone, x, y);
        // Pieces disponibles
        dessinerPieceSurZone(g2d, zone, x, y);
        // Joueurs presents
        dessinerJoueurs(g2d, zone, x, y);
        // Bordure de la zone
        g2d.setColor(COULEUR_GRILLE);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
    }
    // Dessine le fond colore d'une zone en fonction de son etat et de son type
    private void dessinerFondZone(Graphics2D g2d, Zone zone, int x, int y) {
        Color couleurFond;
        // Determiner la couleur de fond selon l'etat de la zone
        boolean exploree = false;
        try {
            exploree = zone.isExploree();
        } catch (Exception e) {
            // methode peut ne pas exister, on laisse false
        }
        TypeZone typeZone = null;
        try {
            typeZone = zone.getType();
        } catch (Exception e) {
            // type non disponible
        }
        // Verifier si c'est l'oeil de la tempete
        if (typeZone == TypeZone.OEIL) {
            Image imgOeil = getImage("OEIL");
            if (imgOeil != null) {
                g2d.drawImage(imgOeil, x, y, TAILLE_TUILE, TAILLE_TUILE, this);
            } else {
                g2d.setColor(new Color(160, 80, 60));
                g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
                dessinerTexteCentre(g2d, "OEIL", x, y, TAILLE_TUILE, TAILLE_TUILE);
            }
            return;
        }
        // Zone non exploree
        if (!exploree) {
            Image imgDos = getImage("DOS_CARTE");
            if (imgDos != null) {
                g2d.drawImage(imgDos, x, y, TAILLE_TUILE, TAILLE_TUILE, this);
            } else {
                couleurFond = COULEUR_INEXPLOREE;
                g2d.setColor(couleurFond);
                g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
                // Dessiner un point d'interrogation
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setFont(new Font("SansSerif", Font.BOLD, 32));
                dessinerTexteCentre(g2d, "?", x, y, TAILLE_TUILE, TAILLE_TUILE);
            }
            return;
        }
        // Zone exploree - determiner la couleur/image de fond
        if (typeZone != null) {
            switch (typeZone) {
                case OASIS:
                    couleurFond = COULEUR_OASIS;
                    break;
                case TUNNEL:
                    couleurFond = COULEUR_TUNNEL;
                    break;
                case CRASH:
                    couleurFond = new Color(200, 170, 130);
                    break;
                case PISTE:
                    couleurFond = new Color(170, 200, 170);
                    break;
                case MIRAGE:
                    couleurFond = new Color(200, 180, 140);
                    break;
                default:
                    couleurFond = COULEUR_SABLE;
                    break;
            }
        } else {
            couleurFond = COULEUR_SABLE;
        }
        g2d.setColor(couleurFond);
        g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
    }
    // Dessine l'icone ou le texte du type de zone quand elle est exploree
    private void dessinerIconeZone(Graphics2D g2d, Zone zone, int x, int y) {
        boolean exploree = false;
        try {
            exploree = zone.isExploree();
        } catch (Exception e) {
            return;
        }
        if (!exploree) return;
        TypeZone typeZone = null;
        try {
            typeZone = zone.getType();
        } catch (Exception e) {
            return;
        }
        if (typeZone == null || typeZone == TypeZone.OEIL) return;
        // Nom de l'image correspondant au type
        String nomImage = null;
        String texteSecours = "";
        int tailleIcone = 60;
        int xIcone = x + (TAILLE_TUILE - tailleIcone) / 2;
        int yIcone = y + 5;
        switch (typeZone) {
            case CRASH:
                nomImage = "CRASH";
                texteSecours = "CRASH";
                break;
            case PISTE:
                nomImage = "PISTE";
                texteSecours = "PISTE";
                break;
            case OASIS:
                nomImage = "OASIS";
                texteSecours = "OASIS";
                break;
            case MIRAGE:
                nomImage = "MIRAGE";
                texteSecours = "MIRAGE";
                break;
            case TUNNEL:
                nomImage = "TUNNEL";
                texteSecours = "TUNNEL";
                break;
            case EQUIPEMENT:
                nomImage = "ENGRENAGE";
                texteSecours = "EQUIP";
                break;
            case NORMAL:
                nomImage = "CASEVIDE";
                texteSecours = "";
                break;
            default:
                // Pour les indices, utiliser les images INDICE
                if (typeZone.isPieceIndice()) {
                    int pieceIdx = typeZone.getPieceIndex();
                    boolean isLigne = typeZone.isLigne();
                    nomImage = "INDICE" + pieceIdx + (isLigne ? "0" : "1");
                    texteSecours = typeZone.getNom();
                }
                break;
        }
        if (nomImage != null) {
            Image img = getImage(nomImage);
            if (img != null) {
                g2d.drawImage(img, xIcone, yIcone, tailleIcone, tailleIcone, this);
            } else if (!texteSecours.isEmpty()) {
                g2d.setColor(new Color(80, 50, 30));
                g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
                dessinerTexteCentre(g2d, texteSecours, x, y + 5, TAILLE_TUILE, 20);
            }
        }
    }
    // Dessine l'indicateur de sable sur une zone
    private void dessinerSable(Graphics2D g2d, Zone zone, int x, int y) {
        int sable = 0;
        try {
            sable = zone.getSable();
        } catch (Exception e) {
            return;
        }
        if (sable <= 0) return;
        // Assombrir le fond proportionnellement au sable
        g2d.setColor(new Color(160, 130, 70, Math.min(sable * 40, 180)));
        g2d.fillRect(x, y, TAILLE_TUILE, TAILLE_TUILE);
        // Image de sable si disponible
        String nomSable = "SABLE" + Math.min(sable, 2);
        Image imgSable = getImage(nomSable);
        if (imgSable != null) {
            g2d.drawImage(imgSable, x + TAILLE_TUILE - 35, y + TAILLE_TUILE - 35,
                          30, 30, this);
        }
        // Afficher le nombre de tonnes en bas a droite
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        String txtSable = String.valueOf(sable);
        g2d.drawString(txtSable, x + TAILLE_TUILE - 18, y + TAILLE_TUILE - 5);
    }
    // Dessine les icones des pieces disponibles sur la zone
    // Utilise les indices decouverts dans Desert pour localiser les pieces
    private void dessinerPieceSurZone(Graphics2D g2d, Zone zone, int x, int y) {
        if (desert == null) return;
        int ligne = zone.getLigne();
        int colonne = zone.getColonne();
        int nbPiecesAffichees = 0;
        boolean[] piecesRamassees = desert.getPiecesRamassees();
        boolean[][] indices = desert.getIndicesDecouverts();
        int[] pLigne = desert.getPieceLigne();
        int[] pColonne = desert.getPieceColonne();
        for (int i = 0; i < Desert.NB_PIECES; i++) {
            if (!piecesRamassees[i]
                && indices[i][0] && indices[i][1]
                && pLigne[i] == ligne && pColonne[i] == colonne) {
                // Dessiner l'image de la piece
                String nomImage = "PIECE" + i;
                Image imgPiece = getImage(nomImage);
                int offsetX = x + 2 + nbPiecesAffichees * 32;
                int offsetY = y + TAILLE_TUILE - 32;
                if (imgPiece != null) {
                    g2d.drawImage(imgPiece, offsetX, offsetY, 30, 30, this);
                } else {
                    g2d.setColor(new Color(200, 50, 200));
                    int px = offsetX + 15;
                    int py = offsetY + 5;
                    Polygon losange = new Polygon(
                        new int[]{px, px + 10, px, px - 10},
                        new int[]{py - 10, py, py + 10, py},
                        4
                    );
                    g2d.fillPolygon(losange);
                }
                nbPiecesAffichees++;
            }
        }
    }
    // Dessine les pions des joueurs presents sur la zone
    private void dessinerJoueurs(Graphics2D g2d, Zone zone, int x, int y) {
        java.util.List<Joueur> occupants = null;
        try {
            occupants = zone.getOccupants();
        } catch (Exception e) {
            try {
                occupants = zone.getJoueurs();
            } catch (Exception ex) {
                return;
            }
        }
        if (occupants == null || occupants.isEmpty()) return;
        int taillePion = 18;
        int espacement = 4;
        int nbJoueurs = occupants.size();
        int largeurTotale = nbJoueurs * taillePion + (nbJoueurs - 1) * espacement;
        int debutX = x + (TAILLE_TUILE - largeurTotale) / 2;
        int debutY = y + TAILLE_TUILE - taillePion - 8;
        for (int i = 0; i < nbJoueurs; i++) {
            Joueur joueur = occupants.get(i);
            int indexJoueur = 0;
            try {
                indexJoueur = joueur.getIndex();
            } catch (Exception e) {
                try {
                    indexJoueur = joueur.getNumero();
                } catch (Exception ex) {
                    indexJoueur = i;
                }
            }
            Color couleur = COULEURS_JOUEURS[indexJoueur % COULEURS_JOUEURS.length];
            int px = debutX + i * (taillePion + espacement);
            // Verifier si c'est le joueur courant
            boolean estCourant = false;
            try {
                estCourant = (joueur == desert.getJoueurCourant());
            } catch (Exception e) {
                // Pas grave, on ne met pas de bordure speciale
            }
            if (estCourant) {
                // Bordure doree pour le joueur courant
                g2d.setColor(new Color(255, 215, 0));
                g2d.fillOval(px - 2, debutY - 2, taillePion + 4, taillePion + 4);
            }
            // Cercle colore du joueur
            g2d.setColor(couleur);
            g2d.fillOval(px, debutY, taillePion, taillePion);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawOval(px, debutY, taillePion, taillePion);
        }
    }
    // Dessine les surlignages verts sur les zones de destination valides
    private void dessinerSurlignages(Graphics2D g2d) {
        if (zonesSurlignees == null || zonesSurlignees.isEmpty()) return;
        for (int[] coords : zonesSurlignees) {
            if (coords.length >= 2) {
                int ligne = coords[0];
                int colonne = coords[1];
                int x = MARGE + colonne * TAILLE_TUILE;
                int y = MARGE + ligne * TAILLE_TUILE;
                // Fond semi-transparent vert
                g2d.setColor(COULEUR_SURLIGNAGE);
                g2d.fillRect(x + 2, y + 2, TAILLE_TUILE - 4, TAILLE_TUILE - 4);
                // Bordure verte epaisse
                g2d.setColor(new Color(0, 200, 0));
                g2d.setStroke(new BasicStroke(3.0f));
                g2d.drawRect(x + 1, y + 1, TAILLE_TUILE - 2, TAILLE_TUILE - 2);
            }
        }
    }
    // Dessine un texte centre dans un rectangle donne
    private void dessinerTexteCentre(Graphics2D g2d, String texte, int x, int y,
                                     int largeur, int hauteur) {
        FontMetrics fm = g2d.getFontMetrics();
        int tx = x + (largeur - fm.stringWidth(texte)) / 2;
        int ty = y + (hauteur + fm.getAscent()) / 2 - fm.getDescent();
        g2d.drawString(texte, tx, ty);
    }
    // Retourne la taille d'une tuile en pixels
    public int getTailleTuile() {
        return TAILLE_TUILE;
    }
    // Retourne la marge du plateau en pixels
    public int getMarge() {
        return MARGE;
    }
}
