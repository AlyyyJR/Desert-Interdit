// ==================================================================
// FICHIER : Desert.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est la classe principale de notre modele, elle represente le
// plateau de jeu (grille 5x5). L'oeil de la tempete se deplace
// et le sable s'accumule sur les zones au fil des tours
//
// Ce que fait Desert :
//   - On gere la grille 5x5 et l'oeil de la tempete
//   - On gere les joueurs, leurs actions et les tours de jeu
//   - On gere les pieces de la machine volante et les indices
//   - On gere les pioches d'equipements et de cartes tempete
//   - On verifie les conditions de victoire et de defaite
//   - On notifie la vue via le pattern Observable
//
// Les joueurs doivent retrouver les 4 pieces de la machine et
// s'echapper avant de manquer d'eau ou que le sable depasse 48
//
// Structure : MVC (Modele)
// ==================================================================

package modele;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import modele.CarteTempete.TypeCarteTempete;
import modele.Equipement.TypeEquipement;

// ==================================================================
// Classe Desert
// ==================================================================

public class Desert extends Observable {
    // ==================================================================
    // Constantes
    // ==================================================================
    // Taille de la grille (5x5)
    public static final int TAILLE = 5;
    // Quantite maximale de sable avant la defaite
    public static final int SABLE_MAX = 48;
    // Nombre maximum d'actions par tour
    public static final int ACTIONS_MAX = 4;
    // Nombre de pieces a recuperer pour gagner
    public static final int NB_PIECES = 4;
    // Mode demonstration : desactive les penalites (tempete, vagues de chaleur, sable)
    // Active par le bouton "Demo" du menu, desactive par defaut pour le jeu normal
    private boolean modeDemo = false;
    // Couleurs disponibles pour les joueurs
    private static final Color[] COULEURS_JOUEURS = {
        new Color(220, 50, 50),   // rouge
        new Color(50, 100, 220),  // bleu
        new Color(50, 180, 50),   // vert
        new Color(220, 180, 30),  // jaune
        new Color(180, 50, 180)   // violet
    };

    // ==================================================================
    // Attributs du plateau
    // ==================================================================
    // La grille 5x5 de zones
    private Zone[][] grille;
    // Position en ligne de l'oeil de la tempete
    private int oeilLigne;
    // Position en colonne de l'oeil de la tempete
    private int oeilColonne;
    // Niveau de la tempete (commence a 2.0, augmente de 0.5)
    private double niveauTempete;
    // Nombre total de tonnes de sable sur le plateau
    private int sableTotal;

    // ==================================================================
    // Attributs des joueurs
    // ==================================================================
    // Liste de tous les joueurs
    private ArrayList<Joueur> joueurs;
    // Le joueur dont c'est le tour
    private Joueur joueurActif;
    // Index du joueur actif dans la liste
    private int indexJoueurActif;
    // Nombre d'actions restantes pour le joueur actif
    private int actionsRestantes;

    // ==================================================================
    // Attributs des pieces
    // ==================================================================
    // Indices decouverts pour chaque piece
    // indicesDecouverts[i][0] = true si l'indice de LIGNE de la piece i est decouvert
    // indicesDecouverts[i][1] = true si l'indice de COLONNE de la piece i est decouvert
    private boolean[][] indicesDecouverts;
    // Ligne de chaque piece (calculee quand les 2 indices sont decouverts)
    private int[] pieceLigne;
    // Colonne de chaque piece (calculee quand les 2 indices sont decouverts)
    private int[] pieceColonne;
    // Indique si chaque piece a ete ramassee
    private boolean[] piecesRamassees;

    // ==================================================================
    // Attributs de fin de partie
    // ==================================================================
    // Indique si la partie est terminee
    private boolean partieTerminee;
    // Indique si la partie est gagnee (true) ou perdue (false)
    private boolean partieGagnee;

    // ==================================================================
    // Attributs des cartes
    // ==================================================================
    // Pioche d'equipements
    private ArrayList<Equipement> piocheEquipement;
    // Pioche de cartes tempete
    private ArrayList<CarteTempete> piocheTempete;
    // Defausse de cartes tempete
    private ArrayList<CarteTempete> defausseTempete;
    // Generateur de nombres aleatoires
    private Random random;

    // ==================================================================
    //                          CONSTRUCTEUR
    // ==================================================================
    // Constructeur du desert. Initialise la grille 5x5, place l'oeil de la
    // tempete au centre, distribue les zones speciales aleatoirement et
    // place le sable initial
    public Desert() {
        this.random = new Random();
        this.joueurs = new ArrayList<>();
        this.indexJoueurActif = 0;
        this.actionsRestantes = ACTIONS_MAX;
        this.niveauTempete = 2.0;
        this.sableTotal = 0;
        this.partieTerminee = false;
        this.partieGagnee = false;
        // Initialisation des pieces
        this.indicesDecouverts = new boolean[NB_PIECES][2];
        this.pieceLigne = new int[NB_PIECES];
        this.pieceColonne = new int[NB_PIECES];
        this.piecesRamassees = new boolean[NB_PIECES];
        for (int i = 0; i < NB_PIECES; i++) {
            pieceLigne[i] = -1;
            pieceColonne[i] = -1;
            piecesRamassees[i] = false;
        }
        // Initialisation du plateau
        initGrille();
        // Creation des pioches
        creerPiocheEquipement();
        creerPiocheTempete();
    }

    // ==================================================================
    //                     INITIALISATION DU PLATEAU
    // ==================================================================
    // Initialise la grille 5x5 avec les zones speciales placees aleatoirement.
    // L'oeil de la tempete est toujours au centre (2,2)
    // Le site du crash et la piste sont places aleatoirement
    // Les indices, oasis, mirages, tunnels et equipements sont distribues
    // sur les cases restantes
    private void initGrille() {
        grille = new Zone[TAILLE][TAILLE];
        // On prepare la liste des types speciaux a distribuer
        // (sans compter l'oeil qui est toujours au centre)
        ArrayList<TypeZone> typesSpeciaux = new ArrayList<>();
        // 8 indices (2 par piece : 1 ligne + 1 colonne)
        typesSpeciaux.add(TypeZone.INDICE_LIGNE_0);
        typesSpeciaux.add(TypeZone.INDICE_COLONNE_0);
        typesSpeciaux.add(TypeZone.INDICE_LIGNE_1);
        typesSpeciaux.add(TypeZone.INDICE_COLONNE_1);
        typesSpeciaux.add(TypeZone.INDICE_LIGNE_2);
        typesSpeciaux.add(TypeZone.INDICE_COLONNE_2);
        typesSpeciaux.add(TypeZone.INDICE_LIGNE_3);
        typesSpeciaux.add(TypeZone.INDICE_COLONNE_3);
        // 3 oasis
        typesSpeciaux.add(TypeZone.OASIS);
        typesSpeciaux.add(TypeZone.OASIS);
        typesSpeciaux.add(TypeZone.OASIS);
        // 1 mirage
        typesSpeciaux.add(TypeZone.MIRAGE);
        // 3 tunnels
        typesSpeciaux.add(TypeZone.TUNNEL);
        typesSpeciaux.add(TypeZone.TUNNEL);
        typesSpeciaux.add(TypeZone.TUNNEL);
        // 1 crash
        typesSpeciaux.add(TypeZone.CRASH);
        // 1 piste d'atterrissage
        typesSpeciaux.add(TypeZone.PISTE);
        // 3 equipements
        typesSpeciaux.add(TypeZone.EQUIPEMENT);
        typesSpeciaux.add(TypeZone.EQUIPEMENT);
        typesSpeciaux.add(TypeZone.EQUIPEMENT);
        // On complete avec des zones normales pour avoir 24 cases (25 - 1 pour l'oeil)
        while (typesSpeciaux.size() < TAILLE * TAILLE - 1) {
            typesSpeciaux.add(TypeZone.NORMAL);
        }
        // On melange les types
        Collections.shuffle(typesSpeciaux, random);
        // On place les zones sur la grille
        int indexType = 0;
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (i == 2 && j == 2) {
                    // L'oeil de la tempete est toujours au centre
                    grille[i][j] = new Zone(i, j, TypeZone.OEIL);
                    grille[i][j].setVisible(true);
                    grille[i][j].setExploree(true);
                } else {
                    grille[i][j] = new Zone(i, j, typesSpeciaux.get(indexType));
                    indexType++;
                    // Le crash et les oasis sont visibles des le depart
                    TypeZone t = grille[i][j].getType();
                    if (t == TypeZone.CRASH) {
                        grille[i][j].setVisible(true);
                        grille[i][j].setExploree(true);
                    }
                }
            }
        }
        // L'oeil commence au centre
        oeilLigne = 2;
        oeilColonne = 2;
        // Placement du sable initial :
        //  - 1 tonne sur chacune des 8 cases autour du centre en croix
        int[][] positionsSable = {
            {0, 2}, {1, 1}, {1, 3}, {2, 0}, {2, 4}, {3, 1}, {3, 3}, {4, 2}
        };
        for (int[] pos : positionsSable) {
            grille[pos[0]][pos[1]].addSable();
            sableTotal++;
        }
    }
    // Cree la pioche d'equipements avec les cartes de base
    // - 3 de chaque type = 12 cartes au total
    private void creerPiocheEquipement() {
        piocheEquipement = new ArrayList<>();
        for (TypeEquipement type : TypeEquipement.values()) {
            for (int i = 0; i < 3; i++) {
                piocheEquipement.add(new Equipement(type));
            }
        }
        Collections.shuffle(piocheEquipement, random);
    }
    // Cree la pioche de cartes tempete avec une distribution standard
    // 12 cartes vent (differentes directions et forces),
    // 4 cartes vague de chaleur, 4 cartes la tempete se dechaine
    private void creerPiocheTempete() {
        piocheTempete = new ArrayList<>();
        defausseTempete = new ArrayList<>();
        // Cartes vent : 3 cartes par direction (droite, gauche, bas, haut)
        // Force 1 x2 par direction, Force 2 x1 par direction = 12 cartes
        for (int dir = 0; dir < 4; dir++) {
            // 2 cartes de force 1
            piocheTempete.add(new CarteTempete(TypeCarteTempete.VENT, dir, 1));
            piocheTempete.add(new CarteTempete(TypeCarteTempete.VENT, dir, 1));
            // 1 carte de force 2
            piocheTempete.add(new CarteTempete(TypeCarteTempete.VENT, dir, 2));
        }
        // 4 cartes vague de chaleur
        for (int i = 0; i < 4; i++) {
            piocheTempete.add(new CarteTempete(TypeCarteTempete.CHALEUR));
        }
        // 4 cartes la tempete se dechaine
        for (int i = 0; i < 4; i++) {
            piocheTempete.add(new CarteTempete(TypeCarteTempete.DECHAINE));
        }
        // On melange la pioche
        Collections.shuffle(piocheTempete, random);
    }

    // ==================================================================
    //                       GESTION DES JOUEURS
    // ==================================================================
    // Ajoute un joueur au jeu. Le joueur est place sur le site du crash
    // La couleur est attribuee automatiquement
    // Retourne le joueur cree, ou null si le nombre max de joueurs est atteint
    public Joueur ajouterJoueur(String nom) {
        if (joueurs.size() >= 5) {
            return null;  // Maximum 5 joueurs
        }
        Color couleur = COULEURS_JOUEURS[joueurs.size()];
        Joueur joueur = new Joueur(nom, couleur);
        joueur.setIndex(joueurs.size());
        // Trouver le site du crash pour y placer le joueur
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (grille[i][j].getType() == TypeZone.CRASH) {
                    joueur.setLigne(i);
                    joueur.setColonne(j);
                    grille[i][j].addOccupant(joueur);
                    break;
                }
            }
        }
        joueurs.add(joueur);
        // Le premier joueur ajoute devient le joueur actif
        if (joueurs.size() == 1) {
            joueurActif = joueur;
            indexJoueurActif = 0;
        }
        notifierObservateurs();
        return joueur;
    }
    // Passe au joueur suivant dans l'ordre du tour
    // Remet les actions a ACTIONS_MAX
    public void joueurSuivant() {
        indexJoueurActif = (indexJoueurActif + 1) % joueurs.size();
        joueurActif = joueurs.get(indexJoueurActif);
        actionsRestantes = ACTIONS_MAX;

        notifierObservateurs();
    }

    // ==================================================================
    //                       ACTIONS DES JOUEURS
    // ==================================================================
    // Deplace un joueur vers une case adjacente si le deplacement est valide
    // Coute 1 action
    // Retourne true si le deplacement a ete effectue, false sinon
    public boolean deplacerJoueur(Joueur joueur, int ligne, int colonne) {
        if (partieTerminee) return false;
        if (actionsRestantes <= 0) return false;
        if (!isDeplacementValide(joueur, ligne, colonne)) return false;
        // Retirer le joueur de sa zone actuelle
        grille[joueur.getLigne()][joueur.getColonne()].removeOccupant(joueur);
        // Placer le joueur sur la nouvelle zone
        joueur.setLigne(ligne);
        joueur.setColonne(colonne);
        grille[ligne][colonne].addOccupant(joueur);
        actionsRestantes--;
        notifierObservateurs();
        return true;
    }
    // Verifie si un deplacement est valide pour un joueur
    // Un deplacement est valide si la destination est dans les limites,
    // adjacente (ou par tunnel), pas l'oeil, et pas bloquee
    public boolean isDeplacementValide(Joueur joueur, int ligne, int colonne) {
        // Verifier les limites
        if (ligne < 0 || ligne >= TAILLE || colonne < 0 || colonne >= TAILLE) {
            return false;
        }
        // Verifier que c'est adjacent (pas en diagonale)
        int dLigne = Math.abs(joueur.getLigne() - ligne);
        int dColonne = Math.abs(joueur.getColonne() - colonne);
        boolean adjacent = (dLigne + dColonne == 1);
        // Cas special : deplacement par tunnel
        boolean parTunnel = false;
        Zone zoneActuelle = grille[joueur.getLigne()][joueur.getColonne()];
        Zone zoneDestination = grille[ligne][colonne];
        if (zoneActuelle.getType() == TypeZone.TUNNEL && zoneActuelle.isExploree()
            && zoneDestination.getType() == TypeZone.TUNNEL && zoneDestination.isExploree()) {
            parTunnel = true;
        }
        if (!adjacent && !parTunnel) {
            return false;
        }
        // On ne peut pas aller sur l'oeil de la tempete
        if (grille[ligne][colonne].getType() == TypeZone.OEIL) {
            return false;
        }
        // Verifier que la destination n'est pas bloquee
        if (grille[ligne][colonne].isBloquee()) {
            return false;
        }
        // Verifier que la case de depart permet de sortir (pas bloquee)
        if (zoneActuelle.isBloquee()) {
            return false;
        }
        return true;
    }
    // Creuse sur une zone : retire 1 tonne de sable
    // Le joueur doit etre sur la zone ou une zone adjacente
    // Coute 1 action
    // Retourne true si le sable a ete retire, false sinon
    public boolean creuser(Joueur joueur, int ligne, int colonne) {
        if (partieTerminee) return false;
        if (actionsRestantes <= 0) return false;
        // Verifier les limites
        if (ligne < 0 || ligne >= TAILLE || colonne < 0 || colonne >= TAILLE) {
            return false;
        }
        // Verifier que le joueur est sur la zone ou adjacent
        int dLigne = Math.abs(joueur.getLigne() - ligne);
        int dColonne = Math.abs(joueur.getColonne() - colonne);
        if (dLigne + dColonne > 1) {
            return false;
        }
        Zone zone = grille[ligne][colonne];
        if (zone.retirerSable()) {
            sableTotal--;
            actionsRestantes--;
            notifierObservateurs();
            return true;
        }
        return false;
    }
    // Explore la zone actuelle du joueur
    // Explorer une zone revele son type et declenche son effet
    // Coute 1 action
    // Retourne true si l'exploration a reussi, false sinon
    public boolean explorer(Joueur joueur) {
        if (partieTerminee) return false;
        if (actionsRestantes <= 0) return false;
        Zone zone = grille[joueur.getLigne()][joueur.getColonne()];
        if (zone.explorer()) {
            actionsRestantes--;
            // Appliquer l'effet de la zone
            appliquerEffetZone(zone, joueur);
            notifierObservateurs();
            return true;
        }
        return false;
    }
    // Applique l'effet d'une zone quand elle est exploree
    private void appliquerEffetZone(Zone zone, Joueur joueur) {
        TypeZone type = zone.getType();
        switch (type) {
            case OASIS:
                // Le joueur et tous les occupants recuperent 2 eaux
                for (Joueur j : zone.getOccupants()) {
                    j.remplirEau(2);
                }
                break;
            case MIRAGE:
                // Pas d'effet, c'est un piege
                break;
            case TUNNEL:
                // Le tunnel est maintenant accessible pour les deplacements
                break;
            case EQUIPEMENT:
                // Piocher un equipement
                if (!piocheEquipement.isEmpty()) {
                    Equipement equip = piocheEquipement.remove(0);
                    joueur.addEquipement(equip);
                }
                break;
            default:
                // Pour les indices de pieces
                if (type.isPieceIndice()) {
                    int pieceIdx = type.getPieceIndex();
                    if (type.isLigne()) {
                        indicesDecouverts[pieceIdx][0] = true;
                        pieceLigne[pieceIdx] = zone.getLigne();
                    } else {
                        indicesDecouverts[pieceIdx][1] = true;
                        pieceColonne[pieceIdx] = zone.getColonne();
                    }
                }
                break;
        }
    }
    // Ramasse une piece de la machine volante si elle est disponible a la position du joueur
    // Coute 1 action
    // Une piece est disponible si les deux indices (ligne et colonne) ont ete decouverts, la piece n'a pas encore ete ramassee, 
    // et le joueur est sur la bonne case
    // Retourne true si une piece a ete ramassee, false sinon
    public boolean ramasserPiece(Joueur joueur) {
        if (partieTerminee) return false;
        if (actionsRestantes <= 0) return false;
        boolean pieceRamassee = false;
        for (int i = 0; i < NB_PIECES; i++) {
            if (!piecesRamassees[i]
                && indicesDecouverts[i][0] && indicesDecouverts[i][1]
                && pieceLigne[i] == joueur.getLigne()
                && pieceColonne[i] == joueur.getColonne()) {
                piecesRamassees[i] = true;
                pieceRamassee = true;
                actionsRestantes--;
                break;
            }
        }
        if (pieceRamassee) {
            verifierFinDePartie();
            notifierObservateurs();
        }
        return pieceRamassee;
    }
    // Permet a un joueur de donner de l'eau a un autre joueur present sur la meme case. 
    // Ne coute pas d'action
    // Retourne true si le transfert a reussi
    public boolean donnerEau(Joueur donneur, Joueur receveur) {
        if (partieTerminee) return false;
        // Verifier qu'ils sont sur la meme case
        if (donneur.getLigne() != receveur.getLigne()
            || donneur.getColonne() != receveur.getColonne()) {
            return false;
        }
        // Verifier que le donneur a de l'eau
        if (donneur.getEau() <= 0) {
            return false;
        }
        // Verifier que le receveur n'est pas au max
        if (receveur.getEau() >= Joueur.EAU_MAX) {
            return false;
        }
        donneur.setEau(donneur.getEau() - 1);
        receveur.remplirEau(1);
        notifierObservateurs();
        return true;
    }
    // Permet a un joueur de prendre de l'eau d'un autre joueur present sur la meme case. Ne coute pas d'action
    // Retourne true si le transfert a reussi
    public boolean prendreEau(Joueur preneur, Joueur source) {
        return donnerEau(source, preneur);
    }

    // ==================================================================
    //                        FIN DE TOUR
    // ==================================================================
    // Termine le tour du joueur actif et declenche les actions du desert (tempete). Puis passe au joueur suivant.
    public void finDeTour() {
        if (partieTerminee) return;
        // Piocher et appliquer les cartes tempete
        actionsDuDesert();
        // Verifier la fin de partie
        verifierFinDePartie();
        // Passer au joueur suivant
        if (!partieTerminee) {
            joueurSuivant();
        }
    }
    // Effectue les actions du desert : on pioche un nombre de cartes tempete egal au niveau de tempete (arrondi a l'entier
    // inferieur) et on applique leurs effets.
    // En mode DEMO, les actions du desert sont desactivees (pas de tempete, pas de sable, pas de chaleur)
    public void actionsDuDesert() {
        // Mode demonstration : on ne pioche aucune carte tempete
        if (modeDemo) return;
        int nbCartes = (int) niveauTempete;
        for (int i = 0; i < nbCartes; i++) {
            if (partieTerminee) return;
            // Si la pioche est vide, on remelange la defausse
            if (piocheTempete.isEmpty()) {
                piocheTempete.addAll(defausseTempete);
                defausseTempete.clear();
                Collections.shuffle(piocheTempete, random);
            }
            if (!piocheTempete.isEmpty()) {
                CarteTempete carte = piocheTempete.remove(0);
                appliquerCarteTempete(carte);
                defausseTempete.add(carte);
            }
        }
    }
    // Applique l'effet d'une carte tempete
    private void appliquerCarteTempete(CarteTempete carte) {
        switch (carte.getType()) {
            case VENT:
                soufflerVent(carte.getDirection(), carte.getForce());
                break;
            case CHALEUR:
                vagueDeChaleur();
                break;
            case DECHAINE:
                tempeteSeDechaine();
                break;
        }
    }

    // ==================================================================
    //                     MECANIQUES DE LA TEMPETE
    // ==================================================================
    // Fait souffler le vent dans une direction donnee avec une certaine force
    // L'oeil de la tempete se deplace, et les zones se decalent
    // Du sable est ajoute sur les zones deplacees
    // Directions : 0=droite, 1=gauche, 2=bas, 3=haut
    public void soufflerVent(int direction, int force) {
        for (int f = 0; f < force; f++) {
            // Calculer la nouvelle position de l'oeil
            int nouvLigne = oeilLigne;
            int nouvColonne = oeilColonne;
            switch (direction) {
                case 0: nouvColonne++; break;  // droite
                case 1: nouvColonne--; break;  // gauche
                case 2: nouvLigne++; break;    // bas
                case 3: nouvLigne--; break;    // haut
            }
            // Verifier que l'oeil reste dans les limites
            if (nouvLigne < 0 || nouvLigne >= TAILLE
                || nouvColonne < 0 || nouvColonne >= TAILLE) {
                // L'oeil sort du plateau : pas de deplacement
                continue;
            }
            // Echanger la zone de l'oeil avec la zone de destination
            // La zone adjacente a l'oeil glisse vers la position de l'oeil
            Zone zoneDeplacee = grille[nouvLigne][nouvColonne];
            Zone zoneOeil = grille[oeilLigne][oeilColonne];
            // Echanger les positions dans la grille
            grille[oeilLigne][oeilColonne] = zoneDeplacee;
            grille[nouvLigne][nouvColonne] = zoneOeil;
            // Mettre a jour les coordonnees des zones
            zoneDeplacee.setLigne(oeilLigne);
            zoneDeplacee.setColonne(oeilColonne);
            zoneOeil.setLigne(nouvLigne);
            zoneOeil.setColonne(nouvColonne);
            // Mettre a jour les positions des joueurs sur la zone deplacee
            for (Joueur j : zoneDeplacee.getOccupants()) {
                j.setLigne(oeilLigne);
                j.setColonne(oeilColonne);
            }
            // Ajouter du sable sur la zone qui a ete deplacee
            zoneDeplacee.addSable();
            sableTotal++;
            // Mettre a jour la position de l'oeil
            oeilLigne = nouvLigne;
            oeilColonne = nouvColonne;
            // Mettre a jour les indices de pieces si necessaire
            mettreAJourIndicesPieces();
        }
        notifierObservateurs();
    }
    // Met a jour les positions des pieces en fonction des indices decouverts
    // Quand les zones bougent, les indices suivent les zones
    private void mettreAJourIndicesPieces() {
        for (int i = 0; i < NB_PIECES; i++) {
            // Recalculer la position de la piece a partir des indices
            pieceLigne[i] = -1;
            pieceColonne[i] = -1;
            // Chercher les zones d'indice correspondantes
            for (int l = 0; l < TAILLE; l++) {
                for (int c = 0; c < TAILLE; c++) {
                    TypeZone type = grille[l][c].getType();
                    if (type.isPieceIndice() && type.getPieceIndex() == i) {
                        if (type.isLigne() && indicesDecouverts[i][0]) {
                            pieceLigne[i] = l;
                        } else if (!type.isLigne() && indicesDecouverts[i][1]) {
                            pieceColonne[i] = c;
                        }
                    }
                }
            }
        }
    }
    // Declenche une vague de chaleur. Tous les joueurs perdent 1 eau,
    // sauf ceux qui sont dans un tunnel explore.
    public void vagueDeChaleur() {
        for (Joueur joueur : joueurs) {
            Zone zone = grille[joueur.getLigne()][joueur.getColonne()];
            // Les joueurs dans un tunnel explore sont proteges
            if (zone.getType() == TypeZone.TUNNEL && zone.isExploree()) {
                continue;
            }
            joueur.boireEau();
        }
        verifierFinDePartie();
        notifierObservateurs();
    }
    // La tempete se dechaine : le niveau de tempete augmente de 0.5
    public void tempeteSeDechaine() {
        niveauTempete += 0.5;
        notifierObservateurs();
    }

    // ==================================================================
    //                   VERIFICATION FIN DE PARTIE
    // ==================================================================
    // Verifie les conditions de fin de partie
    // Defaite : joueur deshydrate, sable depasse SABLE_MAX, tempete >= 7
    // Victoire : 4 pieces ramassees ET tous les joueurs sur la piste
    public void verifierFinDePartie() {
        // Verifier la defaite par deshydratation
        for (Joueur joueur : joueurs) {
            if (joueur.estMort()) {
                partieTerminee = true;
                partieGagnee = false;
                notifierObservateurs();
                return;
            }
        }
        // Verifier la defaite par sable
        if (sableTotal >= SABLE_MAX) {
            partieTerminee = true;
            partieGagnee = false;
            notifierObservateurs();
            return;
        }
        // Verifier la defaite par tempete
        if (niveauTempete >= 7.0) {
            partieTerminee = true;
            partieGagnee = false;
            notifierObservateurs();
            return;
        }
        // Verifier la victoire : toutes les pieces ramassees + tous sur la piste
        if (getPiecesRestantes() == 0) {
            boolean tousSurPiste = true;
            for (Joueur joueur : joueurs) {
                Zone zone = grille[joueur.getLigne()][joueur.getColonne()];
                if (zone.getType() != TypeZone.PISTE) {
                    tousSurPiste = false;
                    break;
                }
            }
            if (tousSurPiste) {
                partieTerminee = true;
                partieGagnee = true;
                notifierObservateurs();
                return;
            }
        }
    }

    // ==================================================================
    //                         UTILITAIRES
    // ==================================================================
    // Retourne la liste des zones adjacentes a une position donnee
    // (haut, bas, gauche, droite, en restant dans les limites)
    public ArrayList<Zone> getZonesAdjacentes(int l, int c) {
        ArrayList<Zone> adjacentes = new ArrayList<>();
        // Haut
        if (l > 0) adjacentes.add(grille[l - 1][c]);
        // Bas
        if (l < TAILLE - 1) adjacentes.add(grille[l + 1][c]);
        // Gauche
        if (c > 0) adjacentes.add(grille[l][c - 1]);
        // Droite
        if (c < TAILLE - 1) adjacentes.add(grille[l][c + 1]);
        return adjacentes;
    }
    // Retourne le nombre de pieces qu'il reste a ramasser (0 a 4)
    public int getPiecesRestantes() {
        int restantes = 0;
        for (int i = 0; i < NB_PIECES; i++) {
            if (!piecesRamassees[i]) {
                restantes++;
            }
        }
        return restantes;
    }
    // Verifie si une piece est disponible a ramasser a une position donnee
    // Retourne true si une piece est disponible ici
    public boolean pieceDisponible(int ligne, int colonne) {
        for (int i = 0; i < NB_PIECES; i++) {
            if (!piecesRamassees[i]
                && indicesDecouverts[i][0] && indicesDecouverts[i][1]
                && pieceLigne[i] == ligne && pieceColonne[i] == colonne) {
                return true;
            }
        }
        return false;
    }
    // Notifie les observateurs d'un changement dans le modele.
    // Utilise le pattern Observable
    private void notifierObservateurs() {
        setChanged();
        notifyObservers();
    }

    // ==================================================================
    //                      GETTERS ET SETTERS
    // ==================================================================
    // Retourne la grille de zones du plateau (5x5)
    public Zone[][] getGrille() {
        return grille;
    }
    // Retourne une zone specifique de la grille a la position (ligne, colonne)
    public Zone getZone(int ligne, int colonne) {
        return grille[ligne][colonne];
    }
    // Retourne la ligne de l'oeil de la tempete
    public int getOeilLigne() {
        return oeilLigne;
    }
    // Retourne la colonne de l'oeil de la tempete
    public int getOeilColonne() {
        return oeilColonne;
    }
    // Retourne le niveau actuel de la tempete
    public double getNiveauTempete() {
        return niveauTempete;
    }
    // Modifie le niveau de la tempete
    public void setNiveauTempete(double niveauTempete) {
        this.niveauTempete = niveauTempete;
    }
    // Retourne true si le mode demonstration est actif
    public boolean isModeDemo() {
        return modeDemo;
    }
    // Active ou desactive le mode demonstration
    // En mode demo, les penalites du desert sont desactivees (pas de tempete, sable, chaleur)
    public void setModeDemo(boolean modeDemo) {
        this.modeDemo = modeDemo;
    }
    // Retourne le nombre total de tonnes de sable sur le plateau
    public int getSableTotal() {
        return sableTotal;
    }
    // Retourne la liste des joueurs
    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }
    // Retourne le joueur actif (celui dont c'est le tour)
    public Joueur getJoueurActif() {
        return joueurActif;
    }
    // Retourne le nombre d'actions restantes pour le tour actuel
    public int getActionsRestantes() {
        return actionsRestantes;
    }
    // Modifie le nombre d'actions restantes
    public void setActionsRestantes(int actionsRestantes) {
        this.actionsRestantes = actionsRestantes;
    }
    // Retourne le tableau des indices decouverts
    // indicesDecouverts[piece][0=ligne,1=colonne]
    public boolean[][] getIndicesDecouverts() {
        return indicesDecouverts;
    }
    // Retourne le tableau des lignes des pieces
    public int[] getPieceLigne() {
        return pieceLigne;
    }
    // Retourne le tableau des colonnes des pieces
    public int[] getPieceColonne() {
        return pieceColonne;
    }
    // Retourne le tableau indiquant quelles pieces ont ete ramassees
    public boolean[] getPiecesRamassees() {
        return piecesRamassees;
    }
    // Indique si la partie est terminee
    public boolean isPartieTerminee() {
        return partieTerminee;
    }
    // Indique si la partie est gagnee.
    // N'a de sens que si partieTerminee est true
    public boolean isPartieGagnee() {
        return partieGagnee;
    }
    // Retourne la pioche d'equipements
    public ArrayList<Equipement> getPiocheEquipement() {
        return piocheEquipement;
    }
    // Retourne la pioche de cartes tempete
    public ArrayList<CarteTempete> getPiocheTempete() {
        return piocheTempete;
    }
    // Retourne la defausse de cartes tempete
    public ArrayList<CarteTempete> getDefausseTempete() {
        return defausseTempete;
    }
    // Retourne le generateur de nombres aleatoires
    public Random getRandom() {
        return random;
    }

    // ==================================================================
    //            METHODES AJOUTEES POUR LE CONTROLEUR
    // ==================================================================
    // Alias pour isPartieTerminee() - indique si la partie est finie
    public boolean isPartieFinie() {
        return partieTerminee;
    }
    // Alias pour isPartieGagnee() - indique si c'est une victoire
    public boolean isVictoire() {
        return partieGagnee;
    }
    // Retourne la raison de la fin de partie sous forme de texte
    public String getRaisonFinPartie() {
        if (!partieTerminee) {
            return "La partie n'est pas terminee";
        }
        if (partieGagnee) {
            return "Victoire ! Tous les joueurs se sont echappes du desert !";
        }
        // Chercher la raison de la defaite
        for (Joueur joueur : joueurs) {
            if (joueur.estMort()) {
                return "Defaite : " + joueur.getNom() + " est mort de soif";
            }
        }
        if (sableTotal >= SABLE_MAX) {
            return "Defaite : le sable a enseveli le desert (" + sableTotal + "/" + SABLE_MAX + ")";
        }
        if (niveauTempete >= 7.0) {
            return "Defaite : la tempete est devenue trop forte (niveau " + niveauTempete + ")";
        }
        return "Defaite";
    }
    // Alias pour getJoueurActif() - retourne le joueur dont c'est le tour
    public Joueur getJoueurCourant() {
        return joueurActif;
    }
    // Decremente le nombre d'actions restantes de 1
    public void decrementerActions() {
        if (actionsRestantes > 0) {
            actionsRestantes--;
        }
        notifierObservateurs();
    }
    // Reinitialise le nombre d'actions restantes a ACTIONS_MAX
    public void reinitialiserActions() {
        actionsRestantes = ACTIONS_MAX;
        notifierObservateurs();
    }
    // Alias pour creuser() - retire du sable sur la zone (l, c) pour le joueur j
    // Retourne true si le sable a ete retire, false sinon.
    public boolean retirerSable(Joueur j, int l, int c) {
        return creuser(j, l, c);
    }
    // Alias pour explorer() - explore la zone du joueur j
    // Retourne true si l'exploration a reussi, false sinon.
    public boolean explorerZone(Joueur j) {
        return explorer(j);
    }
    // Retourne la liste des zones accessibles pour le joueur j
    // (zones vers lesquelles il peut se deplacer)
    public List<Zone> getZonesAccessibles(Joueur j) {
        List<Zone> accessibles = new ArrayList<>();
        for (int l = 0; l < TAILLE; l++) {
            for (int c = 0; c < TAILLE; c++) {
                if (isDeplacementValide(j, l, c)) {
                    accessibles.add(grille[l][c]);
                }
            }
        }
        return accessibles;
    }
    // Retourne la liste des zones creusables pour le joueur j
    // (zones adjacentes ou sur place qui ont du sable)
    public List<Zone> getZonesCreusables(Joueur j) {
        List<Zone> creusables = new ArrayList<>();
        int jl = j.getLigne();
        int jc = j.getColonne();
        for (int l = 0; l < TAILLE; l++) {
            for (int c = 0; c < TAILLE; c++) {
                int dLigne = Math.abs(jl - l);
                int dColonne = Math.abs(jc - c);
                if (dLigne + dColonne <= 1 && grille[l][c].getSable() > 0) {
                    creusables.add(grille[l][c]);
                }
            }
        }
        return creusables;
    }
    // Retourne la zone sur laquelle se trouve le joueur j
    public Zone getZoneJoueur(Joueur j) {
        return grille[j.getLigne()][j.getColonne()];
    }
    // Retourne la liste des joueurs presents sur la zone (l, c)
    public List<Joueur> getJoueursSurZone(int l, int c) {
        if (l < 0 || l >= TAILLE || c < 0 || c >= TAILLE) {
            return new ArrayList<>();
        }
        return new ArrayList<>(grille[l][c].getOccupants());
    }
    // Donne q unites d'eau du joueur d au joueur r
    // Les joueurs doivent etre sur la meme case
    // Retourne true si le transfert a reussi
    public boolean donnerEau(Joueur d, Joueur r, int q) {
        if (partieTerminee) return false;
        if (d.getLigne() != r.getLigne() || d.getColonne() != r.getColonne()) {
            return false;
        }
        boolean succes = false;
        for (int i = 0; i < q; i++) {
            if (d.getEau() > 0 && r.getEau() < Joueur.EAU_MAX) {
                d.setEau(d.getEau() - 1);
                r.remplirEau(1);
                succes = true;
            } else {
                break;
            }
        }
        if (succes) {
            notifierObservateurs();
        }
        return succes;
    }
    // Prend q unites d'eau du joueur s pour le joueur p
    // Les joueurs doivent etre sur la meme case
    // Retourne true si le transfert a reussi
    public boolean prendreEau(Joueur p, Joueur s, int q) {
        return donnerEau(s, p, q);
    }
    // Utilise un equipement sur la zone (l, c) pour le joueur j
    // Retourne true si l'utilisation a reussi
    // Utilise le premier equipement disponible du joueur
    public boolean utiliserEquipementSurZone(Joueur j, int l, int c) {
        if (partieTerminee) return false;
        if (j.getEquipements().isEmpty()) return false;
        if (l < 0 || l >= TAILLE || c < 0 || c >= TAILLE) return false;
        Equipement equip = j.getEquipements().get(0);
        return utiliserEquipement(j, equip);
    }
    // Prepare un equipement pour utilisation (pas d'effet pour l'instant,
    // reserve pour une logique future)
    public void preparerEquipement(Joueur j, Equipement e) {
        // Preparation de l'equipement - disponible pour le controleur
    }
    // Utilise un equipement du joueur j
    // L'equipement est retire de l'inventaire apres utilisation
    // Retourne true si l'utilisation a reussi
    public boolean utiliserEquipement(Joueur j, Equipement e) {
        if (partieTerminee) return false;
        if (!j.getEquipements().contains(e)) return false;
        switch (e.getType()) {
            case BLASTER:
                // Retire tout le sable de la zone du joueur
                Zone zoneBlaster = grille[j.getLigne()][j.getColonne()];
                int sableRetire = zoneBlaster.getSable();
                zoneBlaster.setSable(0);
                sableTotal -= sableRetire;
                break;
            case JETPACK:
                // Le jetpack sera utilise via deplacerJoueur avec des coordonnees
                // Le controleur gerera la destination
                break;
            case BOUCLIER_SOLAIRE:
                // Protection contre la prochaine vague de chaleur
                // Le controleur gerera le bouclier
                break;
            case DETECTEUR:
                // Revele les zones dans un rayon de 3x3
                int jl = j.getLigne();
                int jc = j.getColonne();
                for (int dl = -1; dl <= 1; dl++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nl = jl + dl;
                        int nc = jc + dc;
                        if (nl >= 0 && nl < TAILLE && nc >= 0 && nc < TAILLE) {
                            grille[nl][nc].setVisible(true);
                        }
                    }
                }
                break;
        }
        j.removeEquipement(e);
        notifierObservateurs();
        return true;
    }
    // Initialise une nouvelle partie avec les noms des joueurs, la difficulte (niveau de tempete initial) et les roles
    public void initialiserPartie(ArrayList<String> noms, double diff, ArrayList<Role> roles) {
        // Reinitialiser le plateau
        this.niveauTempete = diff;
        this.sableTotal = 0;
        this.partieTerminee = false;
        this.partieGagnee = false;
        this.indexJoueurActif = 0;
        this.actionsRestantes = ACTIONS_MAX;
        // Reinitialiser les pieces
        for (int i = 0; i < NB_PIECES; i++) {
            indicesDecouverts[i][0] = false;
            indicesDecouverts[i][1] = false;
            pieceLigne[i] = -1;
            pieceColonne[i] = -1;
            piecesRamassees[i] = false;
        }
        // Reinitialiser la grille
        initGrille();
        // Recreer les pioches
        creerPiocheEquipement();
        creerPiocheTempete();
        // Reinitialiser les joueurs
        joueurs.clear();
        // Preparer les roles aleatoires pour les joueurs sans role choisi
        ArrayList<Role> rolesDisponibles = new ArrayList<>();
        for (Role r : Role.values()) {
            rolesDisponibles.add(r);
        }
        Collections.shuffle(rolesDisponibles, random);
        int indexRoleAleatoire = 0;
        for (int i = 0; i < noms.size(); i++) {
            Color couleur = COULEURS_JOUEURS[i % COULEURS_JOUEURS.length];
            Role role;
            if (roles != null && i < roles.size() && roles.get(i) != null) {
                role = roles.get(i);
            } else {
                // Attribution aleatoire d'un role unique
                role = rolesDisponibles.get(indexRoleAleatoire % rolesDisponibles.size());
                indexRoleAleatoire++;
            }
            Joueur joueur = new Joueur(noms.get(i), couleur, role);
            joueur.setIndex(i);
            // Placer le joueur sur le site du crash
            for (int l = 0; l < TAILLE; l++) {
                for (int c = 0; c < TAILLE; c++) {
                    if (grille[l][c].getType() == TypeZone.CRASH) {
                        joueur.setLigne(l);
                        joueur.setColonne(c);
                        grille[l][c].addOccupant(joueur);
                        break;
                    }
                }
            }
            joueurs.add(joueur);
        }
        // Definir le premier joueur actif
        if (!joueurs.isEmpty()) {
            joueurActif = joueurs.get(0);
            indexJoueurActif = 0;
        }
        notifierObservateurs();
    }
    // Alias pour getPiecesRamassees() - retourne le tableau des pieces recuperees
    public boolean[] getPiecesRecuperees() {
        return piecesRamassees;
    }

    // ==================================================================
    //                   REPRESENTATION TEXTUELLE
    // ==================================================================
    // Retourne une representation textuelle du plateau de jeu
    // Affiche la grille avec les types de zones, le sable et les joueurs
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Desert Interdit ===\n");
        sb.append("Tempete: ").append(niveauTempete);
        sb.append(" | Sable: ").append(sableTotal).append("/").append(SABLE_MAX);
        sb.append(" | Pieces restantes: ").append(getPiecesRestantes()).append("\n");
        if (joueurActif != null) {
            sb.append("Tour de: ").append(joueurActif.getNom());
            sb.append(" (actions: ").append(actionsRestantes).append(")\n");
        }
        sb.append("\n");
        // Afficher la grille
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                Zone zone = grille[i][j];
                sb.append("[");

                if (zone.getType() == TypeZone.OEIL) {
                    sb.append("OEIL");
                } else if (zone.isVisible()) {
                    // Afficher les 4 premiers caracteres du type
                    String nomType = zone.getType().getNom();
                    if (nomType.length() > 6) {
                        sb.append(nomType.substring(0, 6));
                    } else {
                        sb.append(String.format("%-6s", nomType));
                    }
                } else {
                    sb.append("??????");
                }
                sb.append(" s").append(zone.getSable());
                // Afficher les joueurs
                if (zone.aDesOccupants()) {
                    sb.append(" J").append(zone.getOccupants().size());
                }
                sb.append("] ");
            }
            sb.append("\n");
        }
        // Afficher les joueurs
        sb.append("\nJoueurs:\n");
        for (Joueur j : joueurs) {
            sb.append("  ").append(j.toString()).append("\n");
        }
        if (partieTerminee) {
            sb.append("\n*** PARTIE TERMINEE - ");
            if (partieGagnee) {
                sb.append("VICTOIRE !");
            } else {
                sb.append("DEFAITE...");
            }
            sb.append(" ***\n");
        }
        return sb.toString();
    }
}
