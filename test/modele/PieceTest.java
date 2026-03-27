// ==================================================================
// FICHIER : PieceTest.java
// Projet  : Le Desert Interdit
// Auteur  : Aly KONATE & Youssef ABOU HASHISH - L2 Informatique
// ==================================================================
// Tests unitaires pour la classe Piece et l'enum TypePiece.
// Verifie le constructeur, les getters, l'index et le toString.
//
// Structure : MVC (Tests)
// ==================================================================

package modele;

import org.junit.Test;
import static org.junit.Assert.*;

import modele.Piece.TypePiece;

// ==================================================================
// Classe de tests pour Piece
// ==================================================================
public class PieceTest {
    // ==================================================================
    //                    TESTS DE L'ENUM TYPE PIECE
    // ==================================================================
    // Verifie qu'il y a bien 4 types de pieces
    @Test
    public void testNombreTypesPiece() {
        assertEquals(4, TypePiece.values().length);
    }
    // Verifie les noms des types de pieces
    @Test
    public void testNomsTypesPiece() {
        assertEquals("Moteur", TypePiece.MOTEUR.getNom());
        assertEquals("Helice", TypePiece.HELICE.getNom());
        assertEquals("Gouvernail", TypePiece.GOUVERNAIL.getNom());
        assertEquals("Capteur solaire", TypePiece.CAPTEUR.getNom());
    }

    // ==================================================================
    //                    TESTS DU CONSTRUCTEUR
    // ==================================================================
    // Verifie le constructeur avec le type MOTEUR
    @Test
    public void testConstructeurMoteur() {
        Piece p = new Piece(TypePiece.MOTEUR);
        assertEquals(TypePiece.MOTEUR, p.getType());
    }
    // Verifie le constructeur avec le type CAPTEUR
    @Test
    public void testConstructeurCapteur() {
        Piece p = new Piece(TypePiece.CAPTEUR);
        assertEquals(TypePiece.CAPTEUR, p.getType());
    }

    // ==================================================================
    //                    TESTS DES GETTERS
    // ==================================================================
    // Verifie que getNom retourne le nom francais
    @Test
    public void testGetNom() {
        Piece p = new Piece(TypePiece.HELICE);
        assertEquals("Helice", p.getNom());
    }
    // Verifie que getIndex retourne l'ordinal correct
    @Test
    public void testGetIndexMoteur() {
        Piece p = new Piece(TypePiece.MOTEUR);
        assertEquals(0, p.getIndex());
    }
    @Test
    public void testGetIndexHelice() {
        Piece p = new Piece(TypePiece.HELICE);
        assertEquals(1, p.getIndex());
    }
    @Test
    public void testGetIndexGouvernail() {
        Piece p = new Piece(TypePiece.GOUVERNAIL);
        assertEquals(2, p.getIndex());
    }
    @Test
    public void testGetIndexCapteur() {
        Piece p = new Piece(TypePiece.CAPTEUR);
        assertEquals(3, p.getIndex());
    }

    // ==================================================================
    //                    TESTS DE TOSTRING
    // ==================================================================
    // Verifie que toString contient le nom et l'index
    @Test
    public void testToString() {
        Piece p = new Piece(TypePiece.GOUVERNAIL);
        assertTrue(p.toString().contains("Gouvernail"));
        assertTrue(p.toString().contains("index=2"));
    }
}
