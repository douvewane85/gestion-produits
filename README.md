# Gestion de produits — étape 1

Projet pédagogique en Java 17, sans framework ni dépendance externe.
Une seule classe regroupe le menu console, les règles métier et le stockage
en mémoire dans une `ArrayList`. Les données disparaissent à la fermeture.

## Compiler et lancer

Depuis le dossier `gestion-produits` :

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/GestionProduits.java
java -cp out GestionProduits
```

## Fonctionnalités

- Ajouter un produit avec un identifiant automatique.
- Afficher les produits et rechercher par identifiant.
- Modifier une quantité et supprimer un produit.
- Calculer un prix unitaire après une remise, sans modifier le prix enregistré.

## Règles métier

- Nom obligatoire et unique, sans distinction entre majuscules et minuscules.
- Prix strictement positif avec au maximum deux décimales.
- Quantité entière supérieure ou égale à zéro.
- Remise comprise entre 0 et 100 % inclus.
- Arrondi du prix remisé à deux décimales avec `HALF_UP`.

Les prix utilisent `BigDecimal` pour éviter les approximations de `double`.
Les saisies incorrectes affichent un message et reviennent au menu.
La virgule et le point sont acceptés pour les nombres décimaux.

## Observer avant de restructurer

Dans `GestionProduits.java`, repère les instructions d'affichage, les validations
et les accès à la liste : ces responsabilités sont mélangées dans la même classe.
Chaque produit est provisoirement un tableau de chaînes : les indices 0, 1, 2 et 3
représentent son identifiant, son nom, son prix et sa quantité. Cette représentation
est fragile et sera remplacée par un modèle `Produit` lors de la restructuration.

Les méthodes permettent de suivre le programme, mais il n'y a encore aucune
séparation View / DTO / Service / Repository ni pattern de conception.

Exercice : ajoute un produit « Clavier » à 100 avec une quantité de 5, recherche-le
avec l'identifiant 1, puis applique une remise de 10 %. Le résultat attendu est 90.00.
Essaie ensuite un nom vide, un doublon, un prix négatif et une remise de 101 %.

La prochaine étape consistera à extraire le modèle et les couches, puis à introduire
SOLID et les patterns à partir de problèmes concrets dans cette première version.
