// ==================================================================
// FICHIER : RoleTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour l'enum Role.
// Verifie les valeurs, les noms et les descriptions des 6 roles.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import static org.junit.Assert.*;

// ==================================================================
// Classe de tests pour Role
// ==================================================================
public class RoleTest {
    // ==================================================================
    //                    TESTS DES VALEURS DE L'ENUM
    // ==================================================================
    // Verifie qu'il y a bien 6 roles
    @Test
    public void testNombreRoles() {
        assertEquals(6, Role.values().length);
    }
    // Verifie que chaque role a un nom non null et non vide
    @Test
    public void testNomsNonVides() {
        for (Role role : Role.values()) {
            assertNotNull(role.getNom());
            assertFalse(role.getNom().isEmpty());
        }
    }
    // Verifie que chaque role a une description non null et non vide
    @Test
    public void testDescriptionsNonVides() {
        for (Role role : Role.values()) {
            assertNotNull(role.getDescription());
            assertFalse(role.getDescription().isEmpty());
        }
    }

    // ==================================================================
    //                    TESTS DES NOMS SPECIFIQUES
    // ==================================================================
    @Test
    public void testNomArcheologue() {
        assertEquals("Archeologue", Role.ARCHEOLOGUE.getNom());
    }
    @Test
    public void testNomAlpiniste() {
        assertEquals("Alpiniste", Role.ALPINISTE.getNom());
    }
    @Test
    public void testNomExplorateur() {
        assertEquals("Explorateur", Role.EXPLORATEUR.getNom());
    }
    @Test
    public void testNomMeteorologue() {
        assertEquals("Meteorologue", Role.METEOROLOGUE.getNom());
    }
    @Test
    public void testNomNavigatrice() {
        assertEquals("Navigatrice", Role.NAVIGATRICE.getNom());
    }
    @Test
    public void testNomPorteuseEau() {
        assertEquals("Porteuse d'eau", Role.PORTEUSE_EAU.getNom());
    }

    // ==================================================================
    //                    TESTS DES DESCRIPTIONS
    // ==================================================================
    // Verifie que la description de l'archeologue mentionne le sable
    @Test
    public void testDescriptionArcheologue() {
        assertTrue(Role.ARCHEOLOGUE.getDescription().contains("sable"));
    }
    // Verifie que la description de l'alpiniste mentionne le deplacement
    @Test
    public void testDescriptionAlpiniste() {
        assertTrue(Role.ALPINISTE.getDescription().contains("bloquee"));
    }
    // Verifie que la description de l'explorateur mentionne la diagonale
    @Test
    public void testDescriptionExplorateur() {
        assertTrue(Role.EXPLORATEUR.getDescription().contains("diagonale"));
    }
}
