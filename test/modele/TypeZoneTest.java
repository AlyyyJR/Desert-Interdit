// ==================================================================
// FICHIER : TypeZoneTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour l'enum TypeZone.
// Verifie les valeurs, les noms, les methodes isPieceIndice,
// getPieceIndex et isLigne.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import static org.junit.Assert.*;

// ==================================================================
// Classe de tests pour TypeZone
// ==================================================================
public class TypeZoneTest {
    // ==================================================================
    //                    TESTS DES VALEURS DE L'ENUM
    // ==================================================================
    // Verifie qu'il y a 16 types de zones
    @Test
    public void testNombreTypes() {
        assertEquals(16, TypeZone.values().length);
    }
    // Verifie les noms en francais de quelques types
    @Test
    public void testNomNormal() {
        assertEquals("Normal", TypeZone.NORMAL.getNom());
    }
    @Test
    public void testNomOeil() {
        assertEquals("Oeil de la tempete", TypeZone.OEIL.getNom());
    }
    @Test
    public void testNomOasis() {
        assertEquals("Oasis", TypeZone.OASIS.getNom());
    }
    @Test
    public void testNomTunnel() {
        assertEquals("Tunnel", TypeZone.TUNNEL.getNom());
    }
    @Test
    public void testNomPiste() {
        assertEquals("Piste d'atterrissage", TypeZone.PISTE.getNom());
    }

    // ==================================================================
    //                    TESTS DE isPieceIndice
    // ==================================================================
    // Verifie que les types INDICE sont des indices de pieces
    @Test
    public void testIsPieceIndiceTrue() {
        assertTrue(TypeZone.INDICE_LIGNE_0.isPieceIndice());
        assertTrue(TypeZone.INDICE_COLONNE_0.isPieceIndice());
        assertTrue(TypeZone.INDICE_LIGNE_1.isPieceIndice());
        assertTrue(TypeZone.INDICE_COLONNE_1.isPieceIndice());
        assertTrue(TypeZone.INDICE_LIGNE_2.isPieceIndice());
        assertTrue(TypeZone.INDICE_COLONNE_2.isPieceIndice());
        assertTrue(TypeZone.INDICE_LIGNE_3.isPieceIndice());
        assertTrue(TypeZone.INDICE_COLONNE_3.isPieceIndice());
    }
    // Verifie que les types non-INDICE ne sont pas des indices
    @Test
    public void testIsPieceIndiceFalse() {
        assertFalse(TypeZone.NORMAL.isPieceIndice());
        assertFalse(TypeZone.OEIL.isPieceIndice());
        assertFalse(TypeZone.CRASH.isPieceIndice());
        assertFalse(TypeZone.PISTE.isPieceIndice());
        assertFalse(TypeZone.OASIS.isPieceIndice());
        assertFalse(TypeZone.MIRAGE.isPieceIndice());
        assertFalse(TypeZone.TUNNEL.isPieceIndice());
        assertFalse(TypeZone.EQUIPEMENT.isPieceIndice());
    }

    // ==================================================================
    //                    TESTS DE getPieceIndex
    // ==================================================================
    // Verifie les index des pieces (0 = Moteur, 1 = Helice, etc.)
    @Test
    public void testGetPieceIndex0() {
        assertEquals(0, TypeZone.INDICE_LIGNE_0.getPieceIndex());
        assertEquals(0, TypeZone.INDICE_COLONNE_0.getPieceIndex());
    }
    @Test
    public void testGetPieceIndex1() {
        assertEquals(1, TypeZone.INDICE_LIGNE_1.getPieceIndex());
        assertEquals(1, TypeZone.INDICE_COLONNE_1.getPieceIndex());
    }
    @Test
    public void testGetPieceIndex2() {
        assertEquals(2, TypeZone.INDICE_LIGNE_2.getPieceIndex());
        assertEquals(2, TypeZone.INDICE_COLONNE_2.getPieceIndex());
    }
    @Test
    public void testGetPieceIndex3() {
        assertEquals(3, TypeZone.INDICE_LIGNE_3.getPieceIndex());
        assertEquals(3, TypeZone.INDICE_COLONNE_3.getPieceIndex());
    }
    // Verifie que getPieceIndex retourne -1 pour les non-indices
    @Test
    public void testGetPieceIndexNonIndice() {
        assertEquals(-1, TypeZone.NORMAL.getPieceIndex());
        assertEquals(-1, TypeZone.OEIL.getPieceIndex());
        assertEquals(-1, TypeZone.CRASH.getPieceIndex());
    }

    // ==================================================================
    //                    TESTS DE isLigne
    // ==================================================================
    // Verifie que les indices LIGNE retournent true
    @Test
    public void testIsLigneTrue() {
        assertTrue(TypeZone.INDICE_LIGNE_0.isLigne());
        assertTrue(TypeZone.INDICE_LIGNE_1.isLigne());
        assertTrue(TypeZone.INDICE_LIGNE_2.isLigne());
        assertTrue(TypeZone.INDICE_LIGNE_3.isLigne());
    }
    // Verifie que les indices COLONNE retournent false
    @Test
    public void testIsLigneFalseColonne() {
        assertFalse(TypeZone.INDICE_COLONNE_0.isLigne());
        assertFalse(TypeZone.INDICE_COLONNE_1.isLigne());
        assertFalse(TypeZone.INDICE_COLONNE_2.isLigne());
        assertFalse(TypeZone.INDICE_COLONNE_3.isLigne());
    }
    // Verifie que les types non-indice retournent false
    @Test
    public void testIsLigneFalseNonIndice() {
        assertFalse(TypeZone.NORMAL.isLigne());
        assertFalse(TypeZone.OEIL.isLigne());
    }
}
