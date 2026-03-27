// ==================================================================
// FICHIER : Controleur.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// C'est le controleur principal de notre jeu
//
// Il fait le lien entre le modele (Desert) et la vue (Fenetre).
// On ecoute les actions de l'utilisateur (clics sur le plateau,
// clics sur les boutons) et on met a jour le modele puis la vue
//
// Le controleur gere aussi la boucle de jeu : il sait a qui c'est
// le tour, combien d'actions il reste, et quel mode d'action est
// selectionne par le joueur
//
// Ce que fait Controleur :
//   - On ecoute les clics sur le plateau et sur les boutons
//   - On interprete les actions du joueur selon le mode actuel
//   - On met a jour le modele (deplacement, creusage, exploration...)
//   - On rafraichit la vue apres chaque modification
//   - On gere la boucle de tour (phase joueur, phase tempete)
//   - On verifie les conditions de fin de partie
//
// Structure : MVC (Controleur)
// ==================================================================

package controlleur;

import modele.Desert;
import modele.Joueur;
import modele.Zone;
import modele.Role;
import modele.Piece;
import modele.Equipement;
import modele.Equipement.TypeEquipement;
import modele.TypeZone;

import vue.Fenetre;
import vue.VuePlateau;
import vue.VueJoueurs;
import vue.VueActions;
import vue.VuePieces;
import vue.VueMenu;
import vue.VueFinDePartie;
import vue.PlateauListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controleur implements PlateauListener, ActionListener {

    // ==================================================================
    // Enumeration interne -- modes d'action
    // ==================================================================
    // Enumeration des differents modes d'action possibles
    // Quand le joueur clique sur un bouton d'action, le controleur passe dans le mode correspondant
    // Le prochain clic sur le plateau sera alors interprete selon ce mode
    public enum ModeAction {
        DEPLACEMENT,         // Le joueur veut se deplacer vers une zone adjacente
        CREUSER,             // Le joueur veut creuser (retirer du sable) d'une zone
        EXPLORER,            // Le joueur veut explorer la zone ou il se trouve
        RAMASSER,            // Le joueur veut ramasser une piece sur sa zone
        DONNER_EAU,          // Le joueur veut donner de l'eau a un autre joueur
        PRENDRE_EAU,         // Le joueur veut prendre de l'eau d'un autre joueur
        ATTENTE,             // Aucune action en cours, on attend un choix
        UTILISER_EQUIPEMENT  // Le joueur veut utiliser un equipement
    }

    // ==================================================================
    // Attributs
    // ==================================================================
    // Reference vers le modele du jeu (le desert et tout son etat)
    private Desert modele;
    // Reference vers la fenetre principale (la vue)
    private Fenetre fenetre;
    // Mode d'action actuellement selectionne par le joueur
    private ModeAction modeActuel;
    // Joueur cible pour les actions qui necessitent de choisir un autre joueur (par exemple donner ou prendre de l'eau)
    private Joueur joueurCible;

    // ==================================================================
    // Constructeur
    // ==================================================================
    // Constructeur du controleur.
    // Initialise les references vers le modele et la vue,
    //  - branche les ecouteurs sur les boutons de la vue d'actions,
    //  - enregistre ce controleur comme ecouteur du plateau,
    //  - et enregistre les vues comme observateurs du modele
    public Controleur(Desert modele, Fenetre fenetre) {
        this.modele = modele;
        this.fenetre = fenetre;
        this.modeActuel = ModeAction.ATTENTE;
        this.joueurCible = null;
        // On branche les ecouteurs sur les boutons d'actions
        initialiserEcouteursBoutons();
        // On s'enregistre comme ecouteur des clics sur le plateau
        fenetre.getVuePlateau().setPlateauListener(this);
        // On enregistre les vues comme observateurs du modele pour qu'elles se mettent a jour automatiquement
        enregistrerObservateurs();
    }

    // ==================================================================
    // Initialisation
    // ==================================================================
    // Branche les ActionListener sur tous les boutons de la vue d'actions
    // Chaque bouton a une actionCommand qui permet de savoir quel bouton a ete clique dans actionPerformed()
    private void initialiserEcouteursBoutons() {
        VueActions vueActions = fenetre.getVueActions();
        // On recupere les boutons et on ajoute ce controleur comme ecouteur
        vueActions.getBoutonDeplacer().addActionListener(this);
        vueActions.getBoutonCreuser().addActionListener(this);
        vueActions.getBoutonExplorer().addActionListener(this);
        vueActions.getBoutonRamasser().addActionListener(this);
        vueActions.getBoutonDonnerEau().addActionListener(this);
        vueActions.getBoutonPrendreEau().addActionListener(this);
        vueActions.getBoutonFinTour().addActionListener(this);
        vueActions.getBoutonUtiliserEquipement().addActionListener(this);
        vueActions.getBoutonRetourMenu().addActionListener(this);
        // Boutons du menu pour lancer la partie
        VueMenu vueMenu = fenetre.getVueMenu();
        vueMenu.getBoutonJouer().addActionListener(this);
        vueMenu.getBoutonDemo().addActionListener(this);
        // Bouton retour au menu sur l'ecran de fin de partie
        fenetre.getVueFinDePartie().getBoutonRetourMenu().addActionListener(this);
    }
    // Enregistre les differentes vues comme observateurs du modele
    // Quand le modele change, les vues seront notifiees et se redessineront
    private void enregistrerObservateurs() {
        modele.addObserver(fenetre.getVuePlateau());
        modele.addObserver(fenetre.getVueJoueurs());
        modele.addObserver(fenetre.getVuePieces());
        modele.addObserver(fenetre.getVueActions());
    }

    // ==================================================================
    // Gestion des clics plateau
    // ==================================================================
    // Methode appelee quand le joueur clique sur une zone du plateau
    // Le comportement depend du mode d'action actuel :
    //   - DEPLACEMENT : on essaie de deplacer le joueur actif vers la zone
    //   - CREUSER     : on essaie de retirer du sable de la zone
    //   - DONNER_EAU / PRENDRE_EAU : on selectionne le joueur cible
    //   - UTILISER_EQUIPEMENT : certains equipements ciblent une zone
    //   - ATTENTE     : on ne fait rien, le joueur doit d'abord choisir
    @Override
    public void zoneCliquee(int ligne, int colonne) {
        // On recupere le joueur dont c'est le tour
        Joueur joueurActif = modele.getJoueurActif();
        // Si la partie est terminee, on ne fait rien
        if (modele.isPartieFinie()) {
            return;
        }
        // Si on n'a plus d'actions, on ne fait rien (faut finir le tour)
        // Exception : donner/prendre de l'eau est GRATUIT (ne coute pas d'action)
        if (modele.getActionsRestantes() <= 0
                && modeActuel != ModeAction.ATTENTE
                && modeActuel != ModeAction.DONNER_EAU
                && modeActuel != ModeAction.PRENDRE_EAU) {
            JOptionPane.showMessageDialog(fenetre,
                "Plus d'actions disponibles ! Cliquez sur 'Fin de tour'.",
                "Attention", JOptionPane.WARNING_MESSAGE);
            modeActuel = ModeAction.ATTENTE;
            fenetre.getVuePlateau().effacerSurbrillance();
            return;
        }
        switch (modeActuel) {
            case DEPLACEMENT:
                effectuerDeplacement(joueurActif, ligne, colonne);
                break;
            case CREUSER:
                effectuerCreusage(joueurActif, ligne, colonne);
                break;
            case DONNER_EAU:
                selectionnerJoueurPourEau(ligne, colonne, true);
                break;
            case PRENDRE_EAU:
                selectionnerJoueurPourEau(ligne, colonne, false);
                break;
            case UTILISER_EQUIPEMENT:
                // Certains equipements ciblent une zone
                effectuerUtilisationEquipementSurZone(ligne, colonne);
                break;
            case ATTENTE:
            default:
                // Rien a faire, le joueur doit d'abord choisir une action
                break;
        }
        // Apres chaque action, on verifie si la partie est terminee
        verifierEtTerminer();
        mettreAJourAffichage();
    }

    // ==================================================================
    // Actions de jeu
    // ==================================================================
    // Tente de deplacer le joueur actif vers la zone (ligne, colonne)
    // Le deplacement est possible seulement si la zone est adjacente et n'est pas bloquee par trop de sable
    private void effectuerDeplacement(Joueur joueur, int ligne, int colonne) {
        Zone zoneCible = modele.getZone(ligne, colonne);
        if (zoneCible == null) {
            return;
        }
        // On verifie que le deplacement est valide
        boolean deplacementOk = modele.deplacerJoueur(joueur, ligne, colonne);
        if (deplacementOk) {
            // Actions deja decrementees dans modele.deplacerJoueur()
            modeActuel = ModeAction.ATTENTE;
            fenetre.getVuePlateau().effacerSurbrillance();
        } else {
            JOptionPane.showMessageDialog(fenetre,
                "Deplacement impossible vers cette zone !",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
        }
        // Si plus d'actions, on le signale
        verifierActionsRestantes();
    }
    // Tente de retirer du sable de la zone (ligne, colonne)
    // Le creusage est possible si la zone a au moins 1 niveau de sable et qu'elle est a portee du joueur
    private void effectuerCreusage(Joueur joueur, int ligne, int colonne) {
        Zone zoneCible = modele.getZone(ligne, colonne);
        if (zoneCible == null) {
            return;
        }
        boolean creusageOk = modele.retirerSable(joueur, ligne, colonne);
        if (creusageOk) {
            // Actions deja decrementees dans modele.creuser()
            modeActuel = ModeAction.ATTENTE;
            fenetre.getVuePlateau().effacerSurbrillance();
        } else {
            JOptionPane.showMessageDialog(fenetre,
                "Impossible de creuser cette zone !",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
        }
        verifierActionsRestantes();
    }
    // Explore la zone ou se trouve le joueur actif
    // Explorer revele le contenu cache de la zone (indice, equipement, etc.)
    private void effectuerExploration(Joueur joueur) {
        boolean explorationOk = modele.explorerZone(joueur);
        if (explorationOk) {
            // Actions deja decrementees dans modele.explorer()
            modeActuel = ModeAction.ATTENTE;
        } else {
            JOptionPane.showMessageDialog(fenetre,
                "Impossible d'explorer cette zone ! (deja exploree ou trop de sable)",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
        }
        verifierActionsRestantes();
    }
    // Le joueur actif ramasse une piece sur la zone ou il se trouve
    // La piece doit etre presente et les conditions doivent etre remplies
    private void effectuerRamassage(Joueur joueur) {
        boolean ramassageOk = modele.ramasserPiece(joueur);
        if (ramassageOk) {
            // Actions deja decrementees dans modele.ramasserPiece()
            modeActuel = ModeAction.ATTENTE;
        } else {
            JOptionPane.showMessageDialog(fenetre,
                "Aucune piece a ramasser ici !",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
        }
        verifierActionsRestantes();
    }
    // Selectionne un joueur cible sur la zone cliquee pour un transfert d'eau
    // Si un seul joueur est sur la zone, il est selectionne directement
    // Si plusieurs joueurs sont presents, on demande au joueur de choisir
    // estDonner : true si on donne de l'eau, false si on prend
    private void selectionnerJoueurPourEau(int ligne, int colonne, boolean estDonner) {
        Joueur joueurActif = modele.getJoueurActif();
        Zone zoneJoueur = modele.getZoneJoueur(joueurActif);
        Zone zoneCliquee = modele.getZone(ligne, colonne);
        // On ne peut echanger de l'eau qu'avec des joueurs sur la meme zone
        if (zoneJoueur == null || zoneCliquee == null
                || zoneJoueur.getLigne() != ligne || zoneJoueur.getColonne() != colonne) {
            JOptionPane.showMessageDialog(fenetre,
                "Vous devez cliquer sur votre propre zone pour choisir un joueur !",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // On cherche les autres joueurs sur la meme zone
        List<Joueur> joueursSurZone = modele.getJoueursSurZone(ligne, colonne);
        joueursSurZone.remove(joueurActif);
        if (joueursSurZone.isEmpty()) {
            JOptionPane.showMessageDialog(fenetre,
                "Aucun autre joueur sur cette zone !",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
            modeActuel = ModeAction.ATTENTE;
            return;
        }
        // Si un seul joueur, on le selectionne directement
        if (joueursSurZone.size() == 1) {
            joueurCible = joueursSurZone.get(0);
        } else {
            // Sinon on demande de choisir
            String[] nomsJoueurs = new String[joueursSurZone.size()];
            for (int i = 0; i < joueursSurZone.size(); i++) {
                nomsJoueurs[i] = joueursSurZone.get(i).getNom();
            }
            String titre;
            if (estDonner) {
                titre = "Donner de l'eau";
            } else {
                titre = "Prendre de l'eau";
            }
            String choix = (String) JOptionPane.showInputDialog(fenetre,
                "Choisissez un joueur :",
                titre,
                JOptionPane.QUESTION_MESSAGE,
                null, nomsJoueurs, nomsJoueurs[0]);

            if (choix == null) {
                modeActuel = ModeAction.ATTENTE;
                return;
            }
            // On retrouve le joueur choisi
            for (Joueur j : joueursSurZone) {
                if (j.getNom().equals(choix)) {
                    joueurCible = j;
                    break;
                }
            }
        }
        // On demande la quantite d'eau
        String titre;
        if (estDonner) {
            titre = "Combien d'eau voulez-vous donner ?";
        } else {
            titre = "Combien d'eau voulez-vous prendre ?";
        }
        String quantiteStr = JOptionPane.showInputDialog(fenetre,
            titre,
            "1");
        if (quantiteStr == null || quantiteStr.isEmpty()) {
            modeActuel = ModeAction.ATTENTE;
            joueurCible = null;
            return;
        }
        try {
            int quantite = Integer.parseInt(quantiteStr);
            boolean transfertOk;
            if (estDonner) {
                transfertOk = modele.donnerEau(joueurActif, joueurCible, quantite);
            } else {
                transfertOk = modele.prendreEau(joueurActif, joueurCible, quantite);
            }
            if (transfertOk) {
                // Le transfert d'eau ne coute pas d'action dans les regles de base,
                // mais on peut adapter selon la variante
            } else {
                JOptionPane.showMessageDialog(fenetre,
                    "Transfert d'eau impossible ! (pas assez d'eau)",
                    "Action invalide", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(fenetre,
                "Veuillez entrer un nombre valide !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        modeActuel = ModeAction.ATTENTE;
        joueurCible = null;
    }
    // Utilise un equipement qui necessite de cibler une zone sur le plateau
    // Par exemple, le jetpack permet de se deplacer n'importe ou
    private void effectuerUtilisationEquipementSurZone(int ligne, int colonne) {
        // L'equipement en cours d'utilisation est stocke temporairement
        // On demande au modele de l'appliquer sur la zone ciblee
        Joueur joueurActif = modele.getJoueurActif();
        boolean utilisationOk = modele.utiliserEquipementSurZone(joueurActif, ligne, colonne);
        if (utilisationOk) {
            fenetre.getVuePlateau().effacerSurbrillance();
        } else {
            JOptionPane.showMessageDialog(fenetre,
                "Impossible d'utiliser l'equipement sur cette zone !",
                "Action invalide", JOptionPane.WARNING_MESSAGE);
        }
        modeActuel = ModeAction.ATTENTE;
    }

    // ==================================================================
    // Gestion des boutons
    // ==================================================================
    // Methode appelee quand un bouton est clique
    // On regarde la commande d'action du bouton pour savoir quelle action le joueur veut effectuer, puis on agit en consequence
    @Override
    public void actionPerformed(ActionEvent e) {
        String commande = e.getActionCommand();
        // Si la partie est finie, on ne fait rien (sauf lancement/navigation)
        if (modele.isPartieFinie()
                && !"JOUER".equals(commande)
                && !"DEMO".equals(commande)
                && !"RETOUR_MENU".equals(commande)
                && !"RETOUR_MENU_FIN".equals(commande)) {
            return;
        }
        Joueur joueurActif = modele.getJoueurActif();
        switch (commande) {
            case "JOUER":
                // Lancer la partie depuis le menu (mode normal)
                modele.setModeDemo(false);
                lancerPartieDepuisMenu();
                break;
            case "DEMO":
                // Lancer la partie en mode demonstration (sans penalites)
                modele.setModeDemo(true);
                lancerPartieDepuisMenu();
                break;
            case "DEPLACER":
                if (!verifierActionsDisponibles()) break;
                modeActuel = ModeAction.DEPLACEMENT;
                // On met en surbrillance les zones adjacentes accessibles
                surlignerZonesAccessibles(joueurActif);
                break;
            case "CREUSER":
                if (!verifierActionsDisponibles()) break;
                modeActuel = ModeAction.CREUSER;
                // On met en surbrillance les zones ou on peut creuser
                surlignerZonesCreusables(joueurActif);
                break;
            case "EXPLORER":
                if (!verifierActionsDisponibles()) break;
                // L'exploration s'execute immediatement (pas besoin de cliquer une zone)
                effectuerExploration(joueurActif);
                break;
            case "RAMASSER":
                if (!verifierActionsDisponibles()) break;
                // Le ramassage s'execute immediatement sur la zone courante
                effectuerRamassage(joueurActif);
                break;
            case "DONNER_EAU":
                modeActuel = ModeAction.DONNER_EAU;
                break;
            case "PRENDRE_EAU":
                modeActuel = ModeAction.PRENDRE_EAU;
                break;
            case "FIN_TOUR":
                finDeTour();
                break;
            case "UTILISER_EQUIPEMENT":
                ouvrirDialogueEquipement(joueurActif);
                break;
            case "RETOUR_MENU":
                // Retour au menu depuis le jeu (avec confirmation)
                retournerAuMenu();
                return; // On ne verifie pas la fin de partie apres un retour menu
            case "RETOUR_MENU_FIN":
                // Retour au menu depuis l'ecran de fin (sans confirmation)
                fenetre.afficherMenu();
                return; // On ne verifie pas la fin de partie apres un retour menu
            default:
                // Commande inconnue, on ignore
                break;
        }
        // Mise a jour apres chaque action
        verifierEtTerminer();
        mettreAJourAffichage();
    }
    // Verifie qu'il reste des actions au joueur actif
    // Retourne true s'il reste des actions, false sinon
    private boolean verifierActionsDisponibles() {
        if (modele.getActionsRestantes() <= 0) {
            JOptionPane.showMessageDialog(fenetre,
                "Plus d'actions disponibles ! Cliquez sur 'Fin de tour'.",
                "Attention", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    // Met en surbrillance les zones vers lesquelles le joueur peut se deplacer
    // Ce sont les zones adjacentes qui ne sont pas bloquees
    private void surlignerZonesAccessibles(Joueur joueur) {
        List<Zone> zonesAccessibles = modele.getZonesAccessibles(joueur);
        List<int[]> positions = new ArrayList<>();
        for (Zone z : zonesAccessibles) {
            positions.add(new int[]{z.getLigne(), z.getColonne()});
        }
        fenetre.getVuePlateau().surlignerZones(positions);
    }
    // Met en surbrillance les zones ou le joueur peut creuser
    // (sa zone et les zones adjacentes qui ont du sable)
    private void surlignerZonesCreusables(Joueur joueur) {
        List<Zone> zonesCreusables = modele.getZonesCreusables(joueur);
        List<int[]> positions = new ArrayList<>();
        for (Zone z : zonesCreusables) {
            positions.add(new int[]{z.getLigne(), z.getColonne()});
        }
        fenetre.getVuePlateau().surlignerZones(positions);
    }
    // Ouvre un dialogue pour choisir et utiliser un equipement
    // Le joueur voit la liste de ses equipements et en choisit un
    // Certains equipements s'appliquent immediatement, d'autres necessitent de cliquer sur une zone du plateau.
    private void ouvrirDialogueEquipement(Joueur joueur) {
        List<Equipement> equipements = joueur.getEquipements();
        if (equipements.isEmpty()) {
            JOptionPane.showMessageDialog(fenetre,
                "Vous n'avez aucun equipement !",
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // On construit la liste des noms d'equipements
        String[] nomsEquipements = new String[equipements.size()];
        for (int i = 0; i < equipements.size(); i++) {
            nomsEquipements[i] = equipements.get(i).getNom();
        }
        String choix = (String) JOptionPane.showInputDialog(fenetre,
            "Choisissez un equipement a utiliser :",
            "Utiliser un equipement",
            JOptionPane.QUESTION_MESSAGE,
            null, nomsEquipements, nomsEquipements[0]);
        if (choix == null) {
            return; // Le joueur a annule
        }
        // On retrouve l'equipement choisi
        Equipement equipementChoisi = null;
        for (Equipement eq : equipements) {
            if (eq.getNom().equals(choix)) {
                equipementChoisi = eq;
                break;
            }
        }
        if (equipementChoisi == null) {
            return;
        }
        // Selon le type d'equipement, on l'utilise directement
        // ou on passe en mode "clic zone"
        TypeEquipement type = equipementChoisi.getType();
        if (type == TypeEquipement.JETPACK || type == TypeEquipement.BLASTER) {
            // Ces equipements necessitent de cliquer sur une zone
            modele.preparerEquipement(joueur, equipementChoisi);
            modeActuel = ModeAction.UTILISER_EQUIPEMENT;
            JOptionPane.showMessageDialog(fenetre,
                "Cliquez sur la zone cible pour utiliser " + equipementChoisi.getNom() + ".",
                "Equipement", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Utilisation immediate (bouclier solaire, reserve d'eau, etc.)
            boolean ok = modele.utiliserEquipement(joueur, equipementChoisi);
            if (ok) {
                JOptionPane.showMessageDialog(fenetre,
                    "Equipement " + equipementChoisi.getNom() + " utilise avec succes !",
                    "Equipement", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(fenetre,
                    "Impossible d'utiliser cet equipement maintenant.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // ==================================================================
    // Gestion des tours
    // ==================================================================
    // Termine le tour du joueur actif
    // Cela declenche la phase de tempete du desert :
    //  - des cartes tempete sont piochees, du sable est ajoute,
    //  - et l'oeil de la tempete se deplace. Ensuite, on passe
    //  - au joueur suivant et on remet le compteur d'actions
    private void finDeTour() {
        // Phase de tempete + passage au joueur suivant (tout est gere dans modele.finDeTour())
        modele.finDeTour();
        // On remet le mode en attente
        modeActuel = ModeAction.ATTENTE;
        fenetre.getVuePlateau().effacerSurbrillance();
        // On verifie si la partie est terminee (tempete, soif, ensevelissement...)
        if (modele.isPartieFinie()) {
            verifierEtTerminer();
        }
        // Mise a jour de l'affichage
        mettreAJourAffichage();
    }

    // Retourne au menu principal depuis le jeu en cours
    // Demande confirmation car la partie en cours sera perdue
    private void retournerAuMenu() {
        int choix = JOptionPane.showConfirmDialog(fenetre,
            "Voulez-vous vraiment quitter la partie ?\nLa partie en cours sera perdue.",
            "Retour au menu", JOptionPane.YES_NO_OPTION);
        if (choix == JOptionPane.YES_OPTION) {
            modeActuel = ModeAction.ATTENTE;
            fenetre.getVuePlateau().effacerSurbrillance();
            fenetre.afficherMenu();
        }
    }

    // Verifie s'il reste des actions au joueur actif
    // Si le compteur est a zero, on affiche un petit rappel
    private void verifierActionsRestantes() {
        if (modele.getActionsRestantes() <= 0) {
            // On ne bloque pas, mais on informe le joueur
            fenetre.getVueActions().setMessageInfo(
                "Plus d'actions ! Cliquez sur 'Fin de tour'.");
        }
    }

    // ==================================================================
    // Lancement de la partie
    // ==================================================================
    // Lance la partie depuis le menu en recuperant les informations saisies
    // Recupere les noms des joueurs, la difficulte et les roles choisis
    // depuis la vue menu, puis appelle lancerPartie()
    private void lancerPartieDepuisMenu() {
        VueMenu menu = fenetre.getVueMenu();
        ArrayList<String> noms = menu.getNomsJoueurs();
        double difficulte = menu.getDifficulte();
        ArrayList<Role> roles = menu.getRolesChoisis();
        // Verification basique
        if (noms == null || noms.isEmpty()) {
            JOptionPane.showMessageDialog(fenetre,
                "Veuillez entrer au moins 2 joueurs !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (noms.size() < 2 || noms.size() > 5) {
            JOptionPane.showMessageDialog(fenetre,
                "Le nombre de joueurs doit etre entre 2 et 5 !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // On verifie que tous les noms sont remplis
        for (String nom : noms) {
            if (nom == null || nom.trim().isEmpty()) {
                JOptionPane.showMessageDialog(fenetre,
                    "Tous les joueurs doivent avoir un nom !",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        lancerPartie(noms, difficulte, roles);
    }
    // Initialise et lance une nouvelle partie
    // Configure le modele avec les joueurs, la difficulte et les roles, puis bascule la vue sur l'ecran de jeu et demarre le premier tour
    public void lancerPartie(ArrayList<String> noms, double difficulte, ArrayList<Role> roles) {
        // Initialisation du modele
        modele.initialiserPartie(noms, difficulte, roles);
        // On bascule la fenetre vers l'ecran de jeu
        fenetre.afficherJeu();
        // On reinitialise le mode
        modeActuel = ModeAction.ATTENTE;
        joueurCible = null;
        // Premiere mise a jour de l'affichage
        mettreAJourAffichage();
    }

    // ==================================================================
    // Verification fin de partie
    // ==================================================================
    // Verifie si la partie est terminee et, si oui, affiche l'ecran de fin
    //
    // Les conditions de defaite sont :
    //   - Un joueur meurt de soif (eau a 0)
    //   - Le niveau de tempete depasse le maximum
    //   - Une zone est ensevelie (trop de sable)
    //
    // La condition de victoire est que tous les joueurs soient sur la piste d'atterrissage avec toutes les pieces assemblees
    public void verifierEtTerminer() {
        if (!modele.isPartieFinie()) {
            return;
        }
        boolean victoire = modele.isVictoire();
        String raison = modele.getRaisonFinPartie();
        // On affiche l'ecran de fin de partie
        fenetre.afficherFinDePartie(victoire, raison);
    }

    // ==================================================================
    // Mise a jour de l'affichage
    // ==================================================================
    // Met a jour tout l'affichage du jeu
    // Rafraichit les labels d'information (actions restantes, joueur actif, niveau de tempete, quantite totale de sable) et repaint toutes les vues
    public void mettreAJourAffichage() {
        if (modele.getJoueurActif() == null) {
            return;
        }
        VueActions vueActions = fenetre.getVueActions();
        // Mise a jour des informations textuelles
        vueActions.setActionsRestantes(modele.getActionsRestantes());
        vueActions.setNomJoueurActif(modele.getJoueurActif().getNom());
        // Afficher le role du joueur actif
        Role roleActif = modele.getJoueurActif().getRole();
        vueActions.setRoleJoueurActif(roleActif != null ? roleActif.getNom() : "Aucun");
        vueActions.setNiveauTempete(modele.getNiveauTempete());
        vueActions.setSableTotalInfo(modele.getSableTotal());
        // Rafraichir les composants visuels
        fenetre.getVuePlateau().repaint();
        fenetre.getVueJoueurs().mettreAJour(modele.getJoueurs(), modele.getJoueurActif());
        fenetre.getVuePieces().mettreAJour(modele.getPiecesRecuperees());
        fenetre.getVueActions().repaint();
    }

    // ==================================================================
    // Accesseurs
    // ==================================================================
    // Retourne le mode d'action actuellement selectionne
    public ModeAction getModeActuel() {
        return modeActuel;
    }
    // Retourne la reference vers le modele du jeu
    public Desert getModele() {
        return modele;
    }
    // Retourne la reference vers la fenetre principale
    public Fenetre getFenetre() {
        return fenetre;
    }
}
