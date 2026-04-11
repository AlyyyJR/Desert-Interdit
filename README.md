# Le Desert Interdit

> Projet POGL — Aly KONATE & Youssef ABOU HASHISH | L2 Informatique, Universite Paris-Saclay

---

## Presentation

On a developpe une adaptation du jeu de societe cooperatif **Le Desert Interdit** (Forbidden Desert) en Java avec une interface graphique Swing. Le projet suit une architecture **MVC** (Modele-Vue-Controleur) avec le pattern **Observable/Observer** pour synchroniser le modele et les vues.

Le jeu se joue de **2 a 5 joueurs** avec 6 roles differents et 4 niveaux de difficulte. On a aussi ajoute un **mode demo** pour tester sans les penalites de la tempete.

---

## Regles du jeu

### But

On doit cooperer pour retrouver les **4 pieces de la machine volante** (Moteur, Helice, Gouvernail, Capteur solaire) cachees dans le desert, puis se retrouver tous sur la **piste d'atterrissage** pour s'enfuir avant que la tempete nous ensevelisse.

### Le plateau

Le desert est une **grille 5x5**. Au centre il y a l'oeil de la tempete qui se deplace quand on pioche des cartes vent. Le sable s'accumule sur les zones au fil des tours.

```
         Col 0     Col 1     Col 2     Col 3     Col 4
Ligne 0  [Zone]    [Zone]    [Zone]    [Zone]    [Zone]
Ligne 1  [Zone]    [Zone]    [Zone]    [Zone]    [Zone]
Ligne 2  [Zone]    [Zone]    [OEIL]    [Zone]    [Zone]
Ligne 3  [Zone]    [Zone]    [Zone]    [Zone]    [Zone]
Ligne 4  [Zone]    [Zone]    [Zone]    [Zone]    [Zone]

[OEIL] = Oeil de la tempete (case vide, se deplace)
[Zone] = Zone avec un type (Normal, Oasis, Tunnel, Indice, etc.)
```

### Types de zones

| Type               | Effet                                                         |
|--------------------|---------------------------------------------------------------|
| Normal             | Aucun effet                                                   |
| Oeil de la tempete | Case vide qui se deplace, ne recoit pas de sable              |
| Site du crash      | Point de depart de tous les joueurs                           |
| Piste atterrissage | C'est ici qu'on doit tous se retrouver pour gagner            |
| Oasis              | Donne +2 eau a tous les joueurs presents (une seule fois)     |
| Mirage             | Ressemble a une oasis mais ne donne rien                      |
| Tunnel             | Protege de la chaleur + permet de se deplacer entre tunnels   |
| Indice (x8)        | Revele la ligne ou la colonne d'une piece                     |
| Equipement         | Donne une carte equipement                                    |

### Deroulement d'un tour

```
1. ACTIONS (4 par defaut)
       |-- Deplacer       -> vers une zone adjacente non bloquee
       |-- Creuser        -> retirer 1 tonne de sable d'une zone adjacente
       |-- Explorer       -> retourner la zone (reveler son type)
       |-- Ramasser       -> recuperer une piece si les 2 indices sont decouverts
       |-- Donner eau     -> partager de l'eau avec un joueur sur la meme zone
       |-- Utiliser equip -> activer un equipement

2. FIN DE TOUR
       -> Piocher N cartes tempete (N = niveau de tempete arrondi)
       -> Appliquer les effets : vent, vague de chaleur, tempete se dechaine
```

### Conditions de victoire et de defaite

```
VICTOIRE
  OK  4 pieces de la machine recuperees
  OK  Tous les joueurs reunis sur la piste d'atterrissage
  OK  La piste n'est pas bloquee par le sable

DEFAITE — l'une de ces conditions suffit
  X   Le sable total depasse 48 tonnes (plus de marqueurs disponibles)
  X   Un joueur meurt de soif (eau tombe en dessous de 0)
  X   Le niveau de tempete atteint 7 ou plus
```

### Niveaux de difficulte

| Niveau      | Valeur | Comportement                        |
|-------------|--------|-------------------------------------|
| Facile      | 1.0    | Tempete lente, progression chill    |
| Normal      | 2.0    | Equilibre standard (defaut)         |
| Difficile   | 3.0    | Tempete soutenue                    |
| Legendaire  | 4.0    | Tres agressive, pour les plus vaillants |

### Roles

Chaque joueur peut avoir un role avec une capacite speciale :

| Role             | Capacite                                                       |
|------------------|----------------------------------------------------------------|
| Archeologue      | Retire 2 tonnes de sable en une seule action                   |
| Alpiniste        | Peut se deplacer sur les zones bloquees et emmener un joueur   |
| Explorateur      | Se deplace et creuse en diagonale (8 directions)               |
| Meteorologue     | Depense des actions pour reduire les cartes tempete piochees   |
| Navigatrice      | Deplace un autre joueur jusqu'a 3 cases pour 1 action          |
| Porteuse d'eau   | Recupere 2 eaux dans les oasis et peut donner de l'eau a distance |

### Cartes tempete (31 cartes)

| Type                     | Quantite | Effet                                        |
|--------------------------|----------|----------------------------------------------|
| Vent (4 directions)      | 12       | Deplace des zones et ajoute du sable         |
| Vague de chaleur         | 4        | Les joueurs hors tunnel perdent de l'eau     |
| La tempete se dechaine   | 4        | Le niveau de tempete augmente de 0.5         |

### Equipements (12 cartes, 3 de chaque)

| Equipement            | Effet                                                |
|-----------------------|------------------------------------------------------|
| Jetpack               | Se teleporter sur n'importe quelle case              |
| Bouclier solaire      | Protege contre la prochaine vague de chaleur         |
| Blaster               | Retire tout le sable d'une case                      |
| Detecteur de terrain  | Revele les types des zones dans un rayon de 3x3      |

### Les 4 pieces de la machine

| Piece | Nom              |
|-------|------------------|
| 0     | Moteur           |
| 1     | Helice           |
| 2     | Gouvernail       |
| 3     | Capteur solaire  |

Chaque piece a 2 indices (un pour la ligne, un pour la colonne) places sur le plateau. Quand on explore les 2 indices d'une meme piece, elle apparait a l'intersection de la ligne et de la colonne revelees. Il suffit alors d'aller sur cette case et de la ramasser.

---

## Architecture MVC

On a organise le projet en 3 packages principaux qui suivent le pattern MVC :

- **modele** : contient toute la logique du jeu (Desert, Zone, Joueur, etc.)
- **vue** : contient l'affichage graphique (Fenetre, VuePlateau, VueActions, etc.)
- **controlleur** : fait le lien entre les interactions de l'utilisateur et le modele

### Comment ça communique

```
+-------------------------------+                          +----------------------------+
|             VUE               |    ActionListener        |        CONTROLEUR          |
|                               |    PlateauListener       |                            |
|  Fenetre (JFrame+CardLayout)  | ---------------------->  |  Controleur.java           |
|  VuePlateau (Graphics2D)      |                          |  (implements               |
|  VueActions (8 boutons)       |                          |   ActionListener,          |
|  VueJoueurs (infos joueurs)   |                          |   PlateauListener)         |
|  VuePieces (suivi pieces)     |                          |                            |
|  VueMenu (demarrage)          |                          +-------------+--------------+
|  VueFinDePartie               |                                        |
+-------------------------------+                                        | lit / modifie
        ^                                                                v
        |   Observer.update()                             +--------------------------+
        |   (notifyObservers)                             |         MODELE           |
        +------------------------------------------------ |                          |
                                                          |  Desert (Observable)     |
                                                          |  Zone    Joueur   Role   |
                                                          |  Equipement  Piece       |
                                                          |  CarteTempete  TypeZone  |
                                                          +--------------------------+
```

Le modele `Desert` herite de `java.util.Observable`. Quand il change d'etat il appelle `setChanged()` puis `notifyObservers()`. Les vues qui implementent `java.util.Observer` recoivent l'appel `update()` et se mettent a jour automatiquement.

Le controleur implemente `ActionListener` (pour les boutons) et `PlateauListener` (pour les clics sur le plateau). Il reçoit les evenements de la vue, modifie le modele en consequence, et le modele notifie les vues.

### Le pattern Observable/Observer en detail

On utilise le pattern **Observable/Observer** pour que le modele et les vues restent synchronises sans se connaitre directement. Concretement ca fonctionne comme ca :

- Notre modele `Desert` herite de `java.util.Observable`
- Nos 4 vues (VueActions, VueJoueurs, VuePieces, VuePlateau) implementent `java.util.Observer`
- Quand le modele change, il appelle `setChanged()` puis `notifyObservers()`
- Toutes les vues enregistrees recoivent automatiquement l'appel `update(Observable o, Object arg)`

```
                     addObserver(vue)
     Desert ---------------------------------> VueActions  (Observer)
   (Observable)                                VueJoueurs  (Observer)
                                               VuePieces   (Observer)
                                               VuePlateau  (Observer)

     Quand le modele change :

     Desert.notifierObservateurs() {
         setChanged();           // signale que l'etat a change
         notifyObservers();      // previent tous les observers enregistres
     }

                     update(observable, arg)
     Desert ---------------------------------> VueActions.update()  -> repaint()
                                               VueJoueurs.update() -> repaint()
                                               VuePieces.update()  -> repaint()
                                               VuePlateau.update() -> repaint()
```

Ca nous donne plusieurs avantages :
1. **Decouplage** : le modele ne connait pas les vues (il sait meme pas combien il y en a)
2. **Extensibilite** : on peut ajouter de nouvelles vues sans toucher au modele
3. **Coherence** : toutes les vues sont toujours synchronisees avec l'etat du modele
4. **Reutilisabilite** : le modele peut etre reutilise dans un autre contexte (console, reseau)

### Exemple : quand on creuse du sable

```
1. On clique sur "Creuser" dans VueActions
2. Le Controleur recoit l'ActionEvent via actionPerformed()
3. Le Controleur appelle desert.retirerSable(joueur, ligne, colonne)
4. Desert modifie le sable de la zone
5. Desert appelle notifierObservateurs()
   -> setChanged() + notifyObservers()
6. VueActions.update()   -> met a jour les actions restantes
7. VueJoueurs.update()   -> rafraichit les infos joueurs
8. VuePlateau.repaint()  -> redessine le plateau avec le sable modifie
```

### Pipeline d'un tour complet

```
Controleur.actionPerformed()
  |
  +-- Mode DEPLACEMENT
  |     |   Le joueur choisit une zone adjacente non bloquee
  |     |   desert.deplacerJoueur(joueur, ligne, colonne)
  |     |   desert.decrementerActions()
  |     |   notifierObservateurs() -> toutes les vues se mettent a jour
  |
  +-- Mode CREUSER
  |     |   Le joueur choisit une zone avec du sable
  |     |   desert.retirerSable(joueur, ligne, colonne)
  |     |   desert.decrementerActions()
  |     |   Verification : sable total > 48 ? -> defaite
  |
  +-- Mode EXPLORER
  |     |   desert.explorerZone(joueur)
  |     |   Si oasis -> eau +2, si indice -> decouvrir piece
  |     |   desert.decrementerActions()
  |
  +-- Mode RAMASSER
  |     |   Verifier : les 2 indices decouverts + joueur sur la bonne case
  |     |   desert.ramasserPiece(joueur)
  |     |   desert.decrementerActions()
  |     |   Verification : 4 pieces + tous sur piste ? -> victoire
  |
  +-- FIN DE TOUR
  |     desert.actionsDuDesert()
  |       |-- Carte VENT -> deplacer zones + ajouter sable
  |       |-- Carte CHALEUR -> tous les joueurs hors tunnel perdent eau
  |       |-- Carte DECHAINE -> niveauTempete += 0.5
  |     desert.joueurSuivant()
  |     desert.reinitialiserActions()
  |
  +-- Verification fin de partie a chaque action
        |-- sableTotal > 48        -> defaite (plus de sable)
        |-- joueur.estMort()       -> defaite (deshydratation)
        |-- niveauTempete >= 7     -> defaite (tempete maximale)
        |-- 4 pieces + piste libre -> victoire
```

---

## Structure du projet

```
Desert-Interdit/
|-- src/
|   |-- Main.java                      # Point d'entree
|   |-- modele/
|   |   |-- Desert.java                # Modele principal (grille 5x5, extends Observable)
|   |   |-- Zone.java                  # Case du plateau (position, sable, type, occupants)
|   |   |-- Joueur.java                # Joueur (nom, eau, equipements, role)
|   |   |-- TypeZone.java              # Enum des 16 types de zones
|   |   |-- CarteTempete.java          # Cartes tempete (vent, chaleur, dechaine)
|   |   |-- Equipement.java            # Equipements (jetpack, blaster, etc.)
|   |   |-- Piece.java                 # Pieces de la machine volante (4 types)
|   |   |-- Role.java                  # Enum des 6 roles
|   |
|   |-- vue/
|   |   |-- Fenetre.java               # Fenetre principale (JFrame + CardLayout)
|   |   |-- VuePlateau.java            # Affichage du plateau 5x5 (Graphics2D)
|   |   |-- VueActions.java            # Boutons d'actions (implements Observer)
|   |   |-- VueJoueurs.java            # Informations des joueurs (implements Observer)
|   |   |-- VuePieces.java             # Suivi des 4 pieces (implements Observer)
|   |   |-- VueMenu.java               # Menu de configuration
|   |   |-- VueFinDePartie.java        # Ecran de victoire / defaite
|   |   |-- PlateauListener.java       # Interface pour les clics sur le plateau
|   |
|   |-- controlleur/
|   |   |-- Controleur.java            # Controleur (ActionListener + PlateauListener)
|   |
|   |-- resources/
|       |-- images/                    # 34 images PNG
|
|-- test/
|   |-- modele/
|       |-- JoueurTest.java            # 23 tests
|       |-- ZoneTest.java              # 22 tests
|       |-- DesertTest.java            # 25 tests
|       |-- EquipementTest.java        # 13 tests
|       |-- PieceTest.java             # 12 tests
|       |-- CarteTempeteTest.java      # 13 tests
|       |-- TypeZoneTest.java          # 16 tests
|       |-- RoleTest.java              # 14 tests
|
|-- lib/
|   |-- junit-4.13.2.jar
|   |-- hamcrest-core-1.3.jar
|
|-- out/                               # Fichiers compiles
|-- diagramme_classes.pdf              # Diagramme de classes UML
|-- diagramme_classes.puml             # Source PlantUML du diagramme
|-- README.md
```

---

## Interface graphique

On a 3 ecrans geres par un `CardLayout` dans la fenetre principale :

```
+------------------------------------------------------------------------+
|                        Le Desert Interdit                              |
|                    Configuration de la partie                          |
|------------------------------------------------------------------------|
|                                                                        |
|   Nombre de joueurs : [ 2 ]                                            |
|                                                                        |
|   Joueur 1 : [ Nom _________ ]                                         |
|   Joueur 2 : [ Nom _________ ]                                         |
|                                                                        |
|   Roles :                                                              |
|     - Role J1 : [ Aleatoire v ]                                        |
|     - Role J2 : [ Aleatoire v ]                                        |
|                                                                        |
|   Difficulte : [ Normal v ]                                            |
|                                                                        |
|        [ Regles du jeu ]   [ Jouer ]   [ Demo ]                        |
|                             [ Quitter ]                                |
+------------------------------------------------------------------------+
                                    | clic JOUER
                                    v
+----------------------------------------------------+--------------------+
|                                                    |      Actions       |
|                                                    |--------------------|
|                  VuePlateau                        | Joueur : Joueur 1  |
|                                                    | Role : Porteuse    |
|  +------+------+------+------+------+              | Actions : 4        |
|  |      |      |      |      |      |              | Tempete : 2.0      |
|  +------+------+------+------+------+              | Sable total : 8    |
|  |      |      |      |      |      |              |--------------------|
|  +------+------+------+------+------+              | [ Deplacer ]       |
|  |      |      |[OEIL]|      |      |              | [ Creuser  ]       |
|  +------+------+------+------+------+              | [ Explorer ]       |
|  |      |      |      |      |      |              | [ Ramasser ]       |
|  +------+------+------+------+------+              | [ Donner eau ]     |
|  |      |      |      |      |      |              | [ Prendre eau ]    |
|  +------+------+------+------+------+              | [ Utiliser eq ]    |
|                                                    |--------------------|
|                                                    | [ Fin de tour ]    |
|                                                    | [ Retour menu ]    |
|                                                    | [ Quitter ]        |
+----------------------------------------------------+--------------------+
|                      Joueurs                       |   Pieces machine   |
|----------------------------------------------------+--------------------|
| Joueur 1 : Eau 5/5 | Equip : 0 | Role              | [ ] Moteur         |
| Joueur 2 : Eau 5/5 | Equip : 0 | Role              | [ ] Helice         |
|                                                    | [ ] Gouvernail     |
|                                                    | [ ] Capteur        |
+----------------------------------------------------+--------------------+
                      | Fin de game
                      v
+----------------------------------------+
|            Fin de partie               |
|----------------------------------------|
|                                        |
|           VICTOIRE / DEFAITE           |
|                                        |
|       Message explicatif (raison)      |
|                                        |
|             [ Retour menu ]            |
+----------------------------------------+
```

| Ecran   | Panneau           | Affiche                                      |
|---------|-------------------|----------------------------------------------|
| "MENU"  | VueMenu           | Formulaire de configuration de la partie      |
| "JEU"   | Plateau + Panels  | Le jeu complet (plateau, actions, joueurs)    |
| "FIN"   | VueFinDePartie    | Ecran de victoire ou de defaite               |

---

## Mode Demo

On a ajoute un mode demo accessible depuis le menu. Dans ce mode la tempete est desactivee : pas de vent, pas de vague de chaleur, pas d'augmentation du niveau de tempete. Ca permet de tester toutes les mecaniques du jeu (explorer, ramasser les pieces, aller sur la piste) sans risquer de perdre.

---

## Images utilisees

### Roles (6 fichiers)
ALPIN.png, ARCHEO.png, EXPLO.png, METEO.png, NAVIG.png, PORTEUR.png

### Indices (8 fichiers)
INDICE00.png a INDICE31.png — chaque piece a 2 indices (ligne + colonne)

### Pieces de la machine (4 fichiers)
PIECE0.png (Moteur), PIECE1.png (Helice), PIECE2.png (Gouvernail), PIECE3.png (Capteur)

### Terrain (9 fichiers)
OEIL.png, CRASH.png, PISTE.png, OASIS.png, MIRAGE.png, TUNNEL.png, ENGRENAGE.png, CASEVIDE.png, DOS_CARTE.png

### Sable (3 fichiers)
SABLE0.png, SABLE1.png, SABLE2.png

### Joueurs (2 fichiers)
JOUEUR.png (pion normal), JOUEUR_COURANT.png (pion du joueur actif)

### Autres (2 fichiers)
EAU.png (icone d'eau), FOND_MENU.png (fond du menu)

---

## Diagramme de classes

Le diagramme de classes UML complet est dans `diagramme_classes.pdf`. On l'a genere a partir du fichier source `diagramme_classes.puml` (PlantUML).

Le projet contient 18 classes/enums repartis en 3 packages :
- **modele** : Desert, Zone, Joueur, TypeZone, CarteTempete, Equipement, Piece, Role
- **vue** : Fenetre, VuePlateau, VueActions, VueJoueurs, VuePieces, VueMenu, VueFinDePartie, PlateauListener
- **controlleur** : Controleur (avec l'enum interne ModeAction)
- + la classe **Main** a la racine

---

## Tests unitaires

On a ecrit **138 tests unitaires** avec JUnit 4 qui couvrent toutes les classes du modele :

| Classe           | Fichier                  | Tests | Ce qu'on teste                        |
|------------------|--------------------------|-------|---------------------------------------|
| Joueur           | JoueurTest.java          | 23    | Constructeurs, eau, equipements, role |
| Zone             | ZoneTest.java            | 22    | Sable, exploration, occupants, type   |
| Desert           | DesertTest.java          | 25    | Grille, joueurs, actions, tempete     |
| Equipement       | EquipementTest.java      | 13    | Enum, constructeur, noms, descriptions|
| Piece            | PieceTest.java           | 12    | Enum, constructeur, index, noms       |
| CarteTempete     | CarteTempeteTest.java    | 13    | Constructeurs, directions, toString   |
| TypeZone         | TypeZoneTest.java        | 16    | Enum, indices, isLigne                |
| Role             | RoleTest.java            | 14    | Enum, noms, descriptions              |
| **Total**        |                          |**138**|                                       |

---

## Installation et lancement

### Prerequis

```bash
java -version      # Java 17 minimum
```

### Compilation et lancement

```bash
cd Desert-Interdit/

# Compiler les sources
javac -encoding UTF-8 -d out -sourcepath src src/Main.java src/modele/*.java src/vue/*.java src/controlleur/*.java

# Copier les ressources
cp -r src/resources out/

# Lancer le jeu
java -cp out Main
```

### Lancer les tests

```bash
# Compiler les tests
javac -encoding UTF-8 -cp out:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar -d out -sourcepath test test/modele/*.java

# Executer les 138 tests
java -cp out:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore \
    modele.JoueurTest modele.ZoneTest modele.DesertTest modele.EquipementTest \
    modele.PieceTest modele.CarteTempeteTest modele.TypeZoneTest modele.RoleTest
```

---

## Conventions de code

| Convention           | Ce qu'on fait                                               |
|----------------------|-------------------------------------------------------------|
| Documentation        | Commentaires en `//` en francais                            |
| En-tete fichier      | Bloc `==` avec FICHIER, Projet, Auteur                      |
| Sections             | Separateurs `==` pour organiser le code                     |
| Nommage              | camelCase pour les methodes, MAJUSCULES pour les constantes |
| Architecture         | MVC strict avec Observable/Observer                         |

---

## Auteurs

> **Aly KONATE** & **Youssef ABOU HASHISH**
> Projet POGL — L2 Informatique, Universite Paris-Saclay
