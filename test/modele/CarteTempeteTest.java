// ==================================================================
// FICHIER : CarteTempeteTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour la classe CarteTempete et l'enum
// TypeCarteTempete. Verifie les constructeurs, les getters,
// les noms de direction et le toString.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import static org.junit.Assert.*;
import modele.CarteTempete.TypeCarteTempete;

// ==================================================================
// Classe de tests pour CarteTempete
// ==================================================================
public class CarteTempeteTest {
    // ==================================================================
    //                    TESTS DE L'ENUM TYPE CARTE TEMPETE
    // ==================================================================
    // Verifie qu'il y a bien 3 types de cartes tempete
    @Test
    public void testNombreTypesCarteTempete() {
        assertEquals(3, TypeCarteTempete.values().length);
    }
    // Verifie les noms des types
    @Test
    public void testNomsTypes() {
        assertEquals("Le vent souffle", TypeCarteTempete.VENT.getNom());
        assertEquals("Vague de chaleur", TypeCarteTempete.CHALEUR.getNom());
        assertEquals("La tempete se dechaine", TypeCarteTempete.DECHAINE.getNom());
    }

    // ==================================================================
    //                    TESTS DU CONSTRUCTEUR VENT
    // ==================================================================
    // Verifie le constructeur d'une carte VENT
    @Test
    public void testConstructeurVent() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, 0, 2);
        assertEquals(TypeCarteTempete.VENT, carte.getType());
        assertEquals(0, carte.getDirection());
        assertEquals(2, carte.getForce());
    }
    // Verifie le constructeur VENT avec differentes directions
    @Test
    public void testConstructeurVentDirections() {
        for (int dir = 0; dir < 4; dir++) {
            CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, dir, 1);
            assertEquals(dir, carte.getDirection());
        }
    }

    // ==================================================================
    //                    TESTS DU CONSTRUCTEUR CHALEUR/DECHAINE
    // ==================================================================
    // Verifie le constructeur d'une carte CHALEUR
    @Test
    public void testConstructeurChaleur() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.CHALEUR);
        assertEquals(TypeCarteTempete.CHALEUR, carte.getType());
        assertEquals(-1, carte.getDirection());
        assertEquals(0, carte.getForce());
    }
    // Verifie le constructeur d'une carte DECHAINE
    @Test
    public void testConstructeurDechaine() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.DECHAINE);
        assertEquals(TypeCarteTempete.DECHAINE, carte.getType());
        assertEquals(-1, carte.getDirection());
        assertEquals(0, carte.getForce());
    }

    // ==================================================================
    //                    TESTS DES NOMS DE DIRECTION
    // ==================================================================
    // Verifie les noms de direction en francais
    @Test
    public void testNomDirectionDroite() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, 0, 1);
        assertEquals("droite", carte.getNomDirection());
    }
    @Test
    public void testNomDirectionGauche() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, 1, 1);
        assertEquals("gauche", carte.getNomDirection());
    }
    @Test
    public void testNomDirectionBas() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, 2, 1);
        assertEquals("bas", carte.getNomDirection());
    }
    @Test
    public void testNomDirectionHaut() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, 3, 1);
        assertEquals("haut", carte.getNomDirection());
    }
    // Verifie le nom de direction pour une carte sans direction
    @Test
    public void testNomDirectionAucune() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.CHALEUR);
        assertEquals("aucune", carte.getNomDirection());
    }

    // ==================================================================
    //                    TESTS DE TOSTRING
    // ==================================================================
    // Verifie toString pour une carte VENT
    @Test
    public void testToStringVent() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.VENT, 0, 2);
        String s = carte.toString();
        assertTrue(s.contains("VENT"));
        assertTrue(s.contains("droite"));
        assertTrue(s.contains("2"));
    }
    // Verifie toString pour une carte CHALEUR
    @Test
    public void testToStringChaleur() {
        CarteTempete carte = new CarteTempete(TypeCarteTempete.CHALEUR);
        assertTrue(carte.toString().contains("Vague de chaleur"));
    }
}
