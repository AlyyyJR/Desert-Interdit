// ==================================================================
// FICHIER : JoueurTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour la classe Joueur.
// Verifie le constructeur, la gestion de l'eau (boire, remplir,
// mort), la gestion des equipements (ajout, retrait), l'index
// du joueur et la representation textuelle.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.awt.Color;
import modele.Equipement.TypeEquipement;

// ==================================================================
// Classe de tests pour Joueur
// ==================================================================
public class JoueurTest {
    // ==================================================================
    // Attributs de test
    // ==================================================================
    // Joueur utilise dans les tests
    private Joueur joueur;

    // ==================================================================
    // Initialisation avant chaque test
    // ==================================================================
    @Before
    public void setUp() {
        joueur = new Joueur("Alice", Color.BLUE);
    }

    // ==================================================================
    //                       TESTS DU CONSTRUCTEUR
    // ==================================================================
    // Verifie que le nom est correctement initialise
    @Test
    public void testConstructeurNom() {
        assertEquals("Alice", joueur.getNom());
    }
    // Verifie que la couleur est correctement initialisee
    @Test
    public void testConstructeurCouleur() {
        assertEquals(Color.BLUE, joueur.getCouleur());
    }
    // Verifie que l'eau commence a EAU_MAX (5)
    @Test
    public void testConstructeurEau() {
        assertEquals(Joueur.EAU_MAX, joueur.getEau());
        assertEquals(5, joueur.getEau());
    }
    // Verifie que la liste d'equipements est vide au depart
    @Test
    public void testConstructeurEquipementsVides() {
        assertNotNull(joueur.getEquipements());
        assertTrue(joueur.getEquipements().isEmpty());
    }
    // Verifie que le role est null par defaut
    @Test
    public void testConstructeurRoleNull() {
        assertNull(joueur.getRole());
    }
    // Verifie que l'index est -1 par defaut
    @Test
    public void testConstructeurIndex() {
        assertEquals(-1, joueur.getIndex());
    }
    // Verifie le constructeur avec role
    @Test
    public void testConstructeurAvecRole() {
        Joueur joueurRole = new Joueur("Bob", Color.RED, Role.EXPLORATEUR);
        assertEquals("Bob", joueurRole.getNom());
        assertEquals(Color.RED, joueurRole.getCouleur());
        assertEquals(Role.EXPLORATEUR, joueurRole.getRole());
        assertEquals(5, joueurRole.getEau());
    }

    // ==================================================================
    //                      TESTS DE GESTION DE L'EAU
    // ==================================================================
    // Verifie que boireEau diminue l'eau de 1
    @Test
    public void testBoireEauDiminue() {
        joueur.boireEau();
        assertEquals(4, joueur.getEau());
    }
    // Verifie que boireEau retourne true quand eau >= 0
    @Test
    public void testBoireEauRetourneTrueQuandPositif() {
        assertTrue(joueur.boireEau()); // eau = 4
        assertTrue(joueur.boireEau()); // eau = 3
        assertTrue(joueur.boireEau()); // eau = 2
        assertTrue(joueur.boireEau()); // eau = 1
        assertTrue(joueur.boireEau()); // eau = 0
    }
    // Verifie que boireEau retourne false quand eau < 0
    @Test
    public void testBoireEauRetourneFalseQuandNegatif() {
        // Vider toute l'eau (5 -> 0)
        for (int i = 0; i < 5; i++) {
            joueur.boireEau();
        }
        assertEquals(0, joueur.getEau());

        // La prochaine fois, l'eau passe a -1 et retourne false
        assertFalse(joueur.boireEau());
        assertEquals(-1, joueur.getEau());
    }
    // Verifie que remplirEau augmente l'eau
    @Test
    public void testRemplirEauAugmente() {
        joueur.boireEau(); // eau = 4
        joueur.boireEau(); // eau = 3
        joueur.remplirEau(2);
        assertEquals(5, joueur.getEau());
    }
    // Verifie que remplirEau ne depasse pas EAU_MAX
    @Test
    public void testRemplirEauPlafondMax() {
        joueur.boireEau(); // eau = 4
        joueur.remplirEau(10); // essayer de depasser
        assertEquals(Joueur.EAU_MAX, joueur.getEau());
    }
    // Verifie que remplirEau reste a EAU_MAX si deja plein
    @Test
    public void testRemplirEauDejaPleins() {
        joueur.remplirEau(5);
        assertEquals(Joueur.EAU_MAX, joueur.getEau());
    }

    // ==================================================================
    //                      TESTS DE MORT
    // ==================================================================
    // Verifie que estMort retourne false quand eau >= 0
    @Test
    public void testEstMortFalseQuandEauPositive() {
        assertFalse(joueur.estMort());
    }
    // Verifie que estMort retourne false quand eau = 0
    @Test
    public void testEstMortFalseQuandEauZero() {
        joueur.setEau(0);
        assertFalse(joueur.estMort());
    }
    // Verifie que estMort retourne true quand eau < 0
    @Test
    public void testEstMortTrueQuandEauNegative() {
        joueur.setEau(-1);
        assertTrue(joueur.estMort());
    }
    // Verifie la coherence entre boireEau et estMort
    @Test
    public void testMortApresDeshydratation() {
        // Vider toute l'eau puis continuer
        for (int i = 0; i < 6; i++) {
            joueur.boireEau();
        }
        assertTrue(joueur.estMort());
    }

    // ==================================================================
    //                    TESTS DES EQUIPEMENTS
    // ==================================================================
    // Verifie l'ajout d'un equipement
    @Test
    public void testAddEquipement() {
        Equipement eq = new Equipement(TypeEquipement.JETPACK);
        joueur.addEquipement(eq);
        assertEquals(1, joueur.getEquipements().size());
        assertEquals(eq, joueur.getEquipements().get(0));
    }
    // Verifie l'ajout de plusieurs equipements
    @Test
    public void testAddPlusieursEquipements() {
        joueur.addEquipement(new Equipement(TypeEquipement.JETPACK));
        joueur.addEquipement(new Equipement(TypeEquipement.BLASTER));
        joueur.addEquipement(new Equipement(TypeEquipement.DETECTEUR));
        assertEquals(3, joueur.getEquipements().size());
    }
    // Verifie le retrait d'un equipement existant
    @Test
    public void testRemoveEquipement() {
        Equipement eq = new Equipement(TypeEquipement.BOUCLIER_SOLAIRE);
        joueur.addEquipement(eq);
        assertTrue(joueur.removeEquipement(eq));
        assertTrue(joueur.getEquipements().isEmpty());
    }
    // Verifie le retrait d'un equipement inexistant
    @Test
    public void testRemoveEquipementInexistant() {
        Equipement eq = new Equipement(TypeEquipement.BLASTER);
        assertFalse(joueur.removeEquipement(eq));
    }

    // ==================================================================
    //                    TESTS D'INDEX
    // ==================================================================
    // Verifie setIndex et getIndex
    @Test
    public void testSetGetIndex() {
        joueur.setIndex(2);
        assertEquals(2, joueur.getIndex());
    }
    // Verifie que getNumero est un alias de getIndex
    @Test
    public void testGetNumeroAliasIndex() {
        joueur.setIndex(3);
        assertEquals(joueur.getIndex(), joueur.getNumero());
    }

    // ==================================================================
    //                    TESTS DE TOSTRING
    // ==================================================================
    // Verifie que toString contient le nom du joueur
    @Test
    public void testToStringContientNom() {
        assertTrue(joueur.toString().contains("Alice"));
    }
    // Verifie que toString contient la position
    @Test
    public void testToStringContientPosition() {
        joueur.setLigne(1);
        joueur.setColonne(3);
        String s = joueur.toString();
        assertTrue(s.contains("1"));
        assertTrue(s.contains("3"));
    }
    // Verifie que toString contient l'eau
    @Test
    public void testToStringContientEau() {
        assertTrue(joueur.toString().contains("eau=5"));
    }
}
