// ==================================================================
// FICHIER : DesertTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour la classe Desert.
// Verifie le constructeur (grille 5x5, oeil, tempete, actions),
// l'ajout de joueurs, l'acces aux zones, le sable initial,
// l'etat de fin de partie et la gestion des actions.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

// ==================================================================
// Classe de tests pour Desert
// ==================================================================
public class DesertTest {
    // ==================================================================
    // Attributs de test
    // ==================================================================
    // Le desert utilise dans les tests
    private Desert desert;

    // ==================================================================
    // Initialisation avant chaque test
    // ==================================================================
    @Before
    public void setUp() {
        desert = new Desert();
    }

    // ==================================================================
    //                       TESTS DU CONSTRUCTEUR
    // ==================================================================
    // Verifie que la grille 5x5 est initialisee et non nulle
    @Test
    public void testGrilleNonNulle() {
        assertNotNull(desert.getGrille());
        assertEquals(Desert.TAILLE, desert.getGrille().length);
        assertEquals(Desert.TAILLE, desert.getGrille()[0].length);
    }
    // Verifie que chaque zone de la grille est initialisee
    @Test
    public void testToutesZonesNonNulles() {
        for (int i = 0; i < Desert.TAILLE; i++) {
            for (int j = 0; j < Desert.TAILLE; j++) {
                assertNotNull(desert.getZone(i, j));
            }
        }
    }
    // Verifie que l'oeil de la tempete est au centre (2,2)
    @Test
    public void testOeilAuCentre() {
        assertEquals(2, desert.getOeilLigne());
        assertEquals(2, desert.getOeilColonne());
    }
    // Verifie que la zone (2,2) est de type OEIL
    @Test
    public void testZoneCentreEstOeil() {
        assertEquals(TypeZone.OEIL, desert.getZone(2, 2).getType());
    }
    // Verifie que le niveau de tempete commence a 2.0
    @Test
    public void testNiveauTempeteInitial() {
        assertEquals(2.0, desert.getNiveauTempete(), 0.001);
    }
    // Verifie que le nombre d'actions restantes commence a ACTIONS_MAX (4)
    @Test
    public void testActionsRestantesInitiales() {
        assertEquals(Desert.ACTIONS_MAX, desert.getActionsRestantes());
        assertEquals(4, desert.getActionsRestantes());
    }

    // ==================================================================
    //                    TESTS D'AJOUT DE JOUEURS
    // ==================================================================
    // Verifie que ajouterJoueur ajoute un joueur a la liste
    @Test
    public void testAjouterJoueur() {
        Joueur j = desert.ajouterJoueur("Alice");
        assertNotNull(j);
        assertEquals("Alice", j.getNom());
        assertEquals(1, desert.getJoueurs().size());
    }
    // Verifie que le joueur est place sur le site du crash
    @Test
    public void testJoueurPlaceSurCrash() {
        Joueur j = desert.ajouterJoueur("Alice");

        // Le joueur doit etre sur une zone de type CRASH
        Zone zoneJoueur = desert.getZone(j.getLigne(), j.getColonne());
        assertEquals(TypeZone.CRASH, zoneJoueur.getType());
    }
    // Verifie que le premier joueur ajoute est le joueur courant
    @Test
    public void testPremierJoueurEstCourant() {
        Joueur j = desert.ajouterJoueur("Alice");
        assertEquals(j, desert.getJoueurCourant());
    }
    // Verifie l'ajout de plusieurs joueurs
    @Test
    public void testAjouterPlusieursJoueurs() {
        desert.ajouterJoueur("Alice");
        desert.ajouterJoueur("Bob");
        desert.ajouterJoueur("Charlie");
        assertEquals(3, desert.getJoueurs().size());
    }
    // Verifie que l'index du joueur est correctement affecte
    @Test
    public void testIndexJoueur() {
        Joueur j1 = desert.ajouterJoueur("Alice");
        Joueur j2 = desert.ajouterJoueur("Bob");
        assertEquals(0, j1.getIndex());
        assertEquals(1, j2.getIndex());
    }

    // ==================================================================
    //                    TESTS D'ACCES AUX ZONES
    // ==================================================================
    // Verifie que getZone retourne la zone correcte
    @Test
    public void testGetZone() {
        Zone z = desert.getZone(0, 0);
        assertNotNull(z);
        assertEquals(0, z.getLigne());
        assertEquals(0, z.getColonne());
    }
    // Verifie que getZone hors limites lance une exception
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetZoneHorsLimites() {
        desert.getZone(5, 5);
    }
    // Verifie que getZone avec indices negatifs lance une exception
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetZoneIndicesNegatifs() {
        desert.getZone(-1, 0);
    }

    // ==================================================================
    //                    TESTS DU SABLE INITIAL
    // ==================================================================
    // Verifie que 8 positions ont 1 sable chacune au depart
    @Test
    public void testSableInitial() {
        int[][] positionsSable = {
            {0, 2}, {1, 1}, {1, 3}, {2, 0}, {2, 4}, {3, 1}, {3, 3}, {4, 2}
        };

        for (int[] pos : positionsSable) {
            assertEquals("Sable attendu en (" + pos[0] + "," + pos[1] + ")",
                1, desert.getZone(pos[0], pos[1]).getSable());
        }
    }
    // Verifie que l'oeil (2,2) n'a pas de sable
    @Test
    public void testOeilPasDeSable() {
        assertEquals(0, desert.getZone(2, 2).getSable());
    }

    // ==================================================================
    //                    TESTS DE FIN DE PARTIE
    // ==================================================================
    // Verifie que la partie n'est pas finie au depart
    @Test
    public void testPartiePasFiniAuDebut() {
        assertFalse(desert.isPartieFinie());
    }

    // ==================================================================
    //                   TESTS DU JOUEUR COURANT
    // ==================================================================
    // Verifie que getJoueurCourant retourne le joueur actif
    @Test
    public void testGetJoueurCourant() {
        Joueur j = desert.ajouterJoueur("Alice");
        assertNotNull(desert.getJoueurCourant());
        assertEquals(j, desert.getJoueurCourant());
    }

    // ==================================================================
    //                   TESTS DES ACTIONS
    // ==================================================================
    // Verifie que decrementerActions diminue les actions de 1
    @Test
    public void testDecrementerActions() {
        desert.decrementerActions();
        assertEquals(Desert.ACTIONS_MAX - 1, desert.getActionsRestantes());
    }
    // Verifie que decrementerActions ne descend pas en dessous de 0
    @Test
    public void testDecrementerActionsMinZero() {
        for (int i = 0; i < 10; i++) {
            desert.decrementerActions();
        }
        assertEquals(0, desert.getActionsRestantes());
    }
    // Verifie que reinitialiserActions remet a ACTIONS_MAX
    @Test
    public void testReinitialiserActions() {
        desert.decrementerActions();
        desert.decrementerActions();
        desert.reinitialiserActions();
        assertEquals(Desert.ACTIONS_MAX, desert.getActionsRestantes());
    }

    // ==================================================================
    //                   TESTS DES CONSTANTES
    // ==================================================================
    // Verifie la valeur de TAILLE
    @Test
    public void testConstanteTaille() {
        assertEquals(5, Desert.TAILLE);
    }
    // Verifie la valeur de ACTIONS_MAX
    @Test
    public void testConstanteActionsMax() {
        assertEquals(4, Desert.ACTIONS_MAX);
    }
    // Verifie la valeur de NB_PIECES
    @Test
    public void testConstanteNbPieces() {
        assertEquals(4, Desert.NB_PIECES);
    }
}
