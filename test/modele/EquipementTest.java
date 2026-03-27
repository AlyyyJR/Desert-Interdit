// ==================================================================
// FICHIER : EquipementTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour la classe Equipement et l'enum TypeEquipement.
// Verifie le constructeur, les getters, l'enum et le toString.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import static org.junit.Assert.*;

import modele.Equipement.TypeEquipement;

// ==================================================================
// Classe de tests pour Equipement
// ==================================================================
public class EquipementTest {
    // ==================================================================
    //                    TESTS DE L'ENUM TYPE EQUIPEMENT
    // ==================================================================
    // Verifie qu'il y a bien 4 types d'equipement
    @Test
    public void testNombreTypesEquipement() {
        assertEquals(4, TypeEquipement.values().length);
    }
    // Verifie les noms des types d'equipement
    @Test
    public void testNomsTypesEquipement() {
        assertEquals("Jetpack", TypeEquipement.JETPACK.getNom());
        assertEquals("Bouclier solaire", TypeEquipement.BOUCLIER_SOLAIRE.getNom());
        assertEquals("Blaster", TypeEquipement.BLASTER.getNom());
        assertEquals("Detecteur de terrain", TypeEquipement.DETECTEUR.getNom());
    }
    // Verifie que les descriptions ne sont pas vides
    @Test
    public void testDescriptionsNonVides() {
        for (TypeEquipement type : TypeEquipement.values()) {
            assertNotNull(type.getDescription());
            assertFalse(type.getDescription().isEmpty());
        }
    }

    // ==================================================================
    //                    TESTS DU CONSTRUCTEUR
    // ==================================================================
    // Verifie le constructeur avec chaque type
    @Test
    public void testConstructeurJetpack() {
        Equipement eq = new Equipement(TypeEquipement.JETPACK);
        assertEquals(TypeEquipement.JETPACK, eq.getType());
    }
    @Test
    public void testConstructeurBlaster() {
        Equipement eq = new Equipement(TypeEquipement.BLASTER);
        assertEquals(TypeEquipement.BLASTER, eq.getType());
    }

    // ==================================================================
    //                    TESTS DES GETTERS
    // ==================================================================
    // Verifie que getNom retourne le nom francais du type
    @Test
    public void testGetNom() {
        Equipement eq = new Equipement(TypeEquipement.BOUCLIER_SOLAIRE);
        assertEquals("Bouclier solaire", eq.getNom());
    }
    // Verifie que getDescription retourne la description
    @Test
    public void testGetDescription() {
        Equipement eq = new Equipement(TypeEquipement.DETECTEUR);
        assertNotNull(eq.getDescription());
        assertTrue(eq.getDescription().length() > 0);
    }

    // ==================================================================
    //                    TESTS DE TOSTRING
    // ==================================================================
    // Verifie que toString contient le nom de l'equipement
    @Test
    public void testToStringContientNom() {
        Equipement eq = new Equipement(TypeEquipement.JETPACK);
        assertTrue(eq.toString().contains("Jetpack"));
    }
}
