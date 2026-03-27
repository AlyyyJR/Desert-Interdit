// ==================================================================
// FICHIER : ZoneTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour la classe Zone.
// Verifie le constructeur, la gestion du sable (ajout, retrait),
// l'exploration, le blocage par le sable, la gestion des occupants,
// et les methodes utilitaires (getPiece, getJoueurs).
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.awt.Color;

// ==================================================================
// Classe de tests pour Zone
// ==================================================================
public class ZoneTest {
    // ==================================================================
    // Attributs de test
    // ==================================================================
    // Zone normale utilisee dans les tests
    private Zone zoneNormale;
    // Zone de type OEIL utilisee dans les tests
    private Zone zoneOeil;

    // ==================================================================
    // Initialisation avant chaque test
    // ==================================================================
    @Before
    public void setUp() {
        zoneNormale = new Zone(1, 3, TypeZone.NORMAL);
        zoneOeil = new Zone(2, 2, TypeZone.OEIL);
    }

    // ==================================================================
    //                       TESTS DU CONSTRUCTEUR
    // ==================================================================
    // Verifie que la ligne est correctement initialisee
    @Test
    public void testConstructeurLigne() {
        assertEquals(1, zoneNormale.getLigne());
    }
    // Verifie que la colonne est correctement initialisee
    @Test
    public void testConstructeurColonne() {
        assertEquals(3, zoneNormale.getColonne());
    }
    // Verifie que le type est correctement initialise
    @Test
    public void testConstructeurType() {
        assertEquals(TypeZone.NORMAL, zoneNormale.getType());
        assertEquals(TypeZone.OEIL, zoneOeil.getType());
    }
    // Verifie que le sable commence a 0
    @Test
    public void testConstructeurSable() {
        assertEquals(0, zoneNormale.getSable());
    }
    // Verifie que la zone n'est pas exploree au depart
    @Test
    public void testConstructeurNonExploree() {
        assertFalse(zoneNormale.isExploree());
    }
    // Verifie que la zone n'est pas visible au depart
    @Test
    public void testConstructeurNonVisible() {
        assertFalse(zoneNormale.isVisible());
    }
    // Verifie que la liste d'occupants est vide au depart
    @Test
    public void testConstructeurOccupantsVides() {
        assertNotNull(zoneNormale.getOccupants());
        assertTrue(zoneNormale.getOccupants().isEmpty());
    }

    // ==================================================================
    //                      TESTS DU SABLE
    // ==================================================================
    // Verifie que addSable incremente le sable
    @Test
    public void testAddSable() {
        zoneNormale.addSable();
        assertEquals(1, zoneNormale.getSable());
    }
    // Verifie que addSable incremente plusieurs fois
    @Test
    public void testAddSablePlusieurs() {
        zoneNormale.addSable();
        zoneNormale.addSable();
        zoneNormale.addSable();
        assertEquals(3, zoneNormale.getSable());
    }
    // Verifie que addSable ne fonctionne pas sur l'oeil de la tempete
    @Test
    public void testAddSableSurOeil() {
        zoneOeil.addSable();
        assertEquals(0, zoneOeil.getSable());
    }
    // Verifie que retirerSable decremente le sable
    @Test
    public void testRetirerSable() {
        zoneNormale.addSable();
        zoneNormale.addSable();
        assertTrue(zoneNormale.retirerSable());
        assertEquals(1, zoneNormale.getSable());
    }
    // Verifie que retirerSable ne descend pas en dessous de 0
    @Test
    public void testRetirerSableMinZero() {
        assertFalse(zoneNormale.retirerSable());
        assertEquals(0, zoneNormale.getSable());
    }
    // Verifie que retirerSable retourne true quand du sable est retire
    @Test
    public void testRetirerSableRetourneTrue() {
        zoneNormale.addSable();
        assertTrue(zoneNormale.retirerSable());
    }
    // Verifie que retirerSable retourne false quand pas de sable
    @Test
    public void testRetirerSableRetourneFalse() {
        assertFalse(zoneNormale.retirerSable());
    }

    // ==================================================================
    //                      TESTS D'EXPLORATION
    // ==================================================================
    // Verifie que explorer marque la zone comme exploree et visible
    @Test
    public void testExplorerReussi() {
        assertTrue(zoneNormale.explorer());
        assertTrue(zoneNormale.isExploree());
        assertTrue(zoneNormale.isVisible());
    }
    // Verifie qu'on ne peut pas explorer une zone deja exploree
    @Test
    public void testExplorerDejaExploree() {
        zoneNormale.explorer();
        assertFalse(zoneNormale.explorer());
    }
    // Verifie qu'on ne peut pas explorer une zone bloquee (sable >= 2)
    @Test
    public void testExplorerZoneBloquee() {
        zoneNormale.addSable();
        zoneNormale.addSable();
        assertFalse(zoneNormale.explorer());
        assertFalse(zoneNormale.isExploree());
    }
    // Verifie qu'on peut explorer avec 1 sable (pas encore bloquee)
    @Test
    public void testExplorerAvecUnSable() {
        zoneNormale.addSable();
        assertTrue(zoneNormale.explorer());
    }

    // ==================================================================
    //                      TESTS DE BLOCAGE
    // ==================================================================
    // Verifie qu'une zone sans sable n'est pas bloquee
    @Test
    public void testNonBloquee() {
        assertFalse(zoneNormale.isBloquee());
    }
    // Verifie qu'une zone avec 1 sable n'est pas bloquee
    @Test
    public void testNonBloqueAvecUnSable() {
        zoneNormale.addSable();
        assertFalse(zoneNormale.isBloquee());
    }
    // Verifie qu'une zone avec 2 sable est bloquee
    @Test
    public void testBloqueAvecDeuxSable() {
        zoneNormale.addSable();
        zoneNormale.addSable();
        assertTrue(zoneNormale.isBloquee());
    }
    // Verifie qu'une zone avec 3 sable est bloquee
    @Test
    public void testBloqueAvecTroisSable() {
        zoneNormale.addSable();
        zoneNormale.addSable();
        zoneNormale.addSable();
        assertTrue(zoneNormale.isBloquee());
    }

    // ==================================================================
    //                    TESTS DES OCCUPANTS
    // ==================================================================
    // Verifie l'ajout d'un occupant
    @Test
    public void testAddOccupant() {
        Joueur j = new Joueur("Alice", Color.BLUE);
        zoneNormale.addOccupant(j);
        assertEquals(1, zoneNormale.getOccupants().size());
        assertTrue(zoneNormale.aDesOccupants());
    }
    // Verifie qu'un meme joueur n'est pas ajoute deux fois
    @Test
    public void testAddOccupantDoublon() {
        Joueur j = new Joueur("Alice", Color.BLUE);
        zoneNormale.addOccupant(j);
        zoneNormale.addOccupant(j);
        assertEquals(1, zoneNormale.getOccupants().size());
    }
    // Verifie l'ajout de plusieurs occupants
    @Test
    public void testAddPlusieursOccupants() {
        Joueur j1 = new Joueur("Alice", Color.BLUE);
        Joueur j2 = new Joueur("Bob", Color.RED);
        zoneNormale.addOccupant(j1);
        zoneNormale.addOccupant(j2);
        assertEquals(2, zoneNormale.getOccupants().size());
    }
    // Verifie le retrait d'un occupant
    @Test
    public void testRemoveOccupant() {
        Joueur j = new Joueur("Alice", Color.BLUE);
        zoneNormale.addOccupant(j);
        zoneNormale.removeOccupant(j);
        assertEquals(0, zoneNormale.getOccupants().size());
        assertFalse(zoneNormale.aDesOccupants());
    }
    // Verifie que retirer un joueur absent ne provoque pas d'erreur
    @Test
    public void testRemoveOccupantAbsent() {
        Joueur j = new Joueur("Alice", Color.BLUE);
        zoneNormale.removeOccupant(j);
        assertEquals(0, zoneNormale.getOccupants().size());
    }

    // ==================================================================
    //                   TESTS DES METHODES UTILITAIRES
    // ==================================================================
    // Verifie que getPiece retourne null (logique dans Desert)
    @Test
    public void testGetPieceRetourneNull() {
        assertNull(zoneNormale.getPiece());
    }
    // Verifie que getJoueurs est un alias de getOccupants
    @Test
    public void testGetJoueursAliasOccupants() {
        Joueur j = new Joueur("Alice", Color.BLUE);
        zoneNormale.addOccupant(j);
        assertEquals(zoneNormale.getOccupants(), zoneNormale.getJoueurs());
        assertSame(zoneNormale.getOccupants(), zoneNormale.getJoueurs());
    }
}
