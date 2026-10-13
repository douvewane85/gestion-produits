import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Première version volontairement non structurée : tout est dans une classe. */
public class GestionProduits {
    // Chaque tableau contient : identifiant, nom, prix unitaire, quantité.
    // Ce choix provisoire permet de commencer sans créer une classe Produit.
    private static final List<String[]> produits = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static int prochainId = 1;

    public static void main(String[] args) {
        boolean continuer = true;
        System.out.println("Bienvenue dans la gestion de produits !");
        while (continuer) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Afficher les produits");
            System.out.println("3. Rechercher un produit par identifiant");
            System.out.println("4. Modifier la quantité d'un produit");
            System.out.println("5. Supprimer un produit");
            System.out.println("6. Calculer un prix avec remise");
            System.out.println("0. Quitter");
            String choix = lireTexte("Votre choix : ");
            if (choix == null) break;
            switch (choix) {
                case "1": ajouterProduit(); break;
                case "2": afficherProduits(); break;
                case "3": rechercherProduit(); break;
                case "4": modifierQuantite(); break;
                case "5": supprimerProduit(); break;
                case "6": calculerRemise(); break;
                case "0": continuer = false; break;
                default: System.out.println("Choix invalide.");
            }
        }
        scanner.close();
        System.out.println("Au revoir ! Les données en mémoire ne sont pas conservées.");
    }

    private static void ajouterProduit() {
        String nom = lireTexte("Nom : ");
        if (nom == null) return;
        if (nom.isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return;
        }
        // Règle métier : deux produits ne peuvent pas avoir le même nom.
        for (String[] produit : produits) {
            if (produit[1].equalsIgnoreCase(nom)) {
                System.out.println("Un produit avec ce nom existe déjà.");
                return;
            }
        }
        BigDecimal prix = lireDecimal("Prix unitaire (maximum 2 décimales) : ");
        if (prix == null) return;
        if (prix.signum() <= 0 || prix.stripTrailingZeros().scale() > 2) {
            System.out.println("Le prix doit être positif et avoir au maximum 2 décimales.");
            return;
        }
        Integer quantite = lireEntier("Quantité : ");
        if (quantite == null) return;
        if (quantite < 0) {
            System.out.println("La quantité ne peut pas être négative.");
            return;
        }
        produits.add(new String[] {String.valueOf(prochainId++), nom,
                prix.setScale(2).toPlainString(), String.valueOf(quantite)});
        System.out.println("Produit ajouté.");
    }

    private static void afficherProduits() {
        if (produits.isEmpty()) {
            System.out.println("Aucun produit enregistré.");
            return;
        }
        for (String[] produit : produits) afficherProduit(produit);
    }

    private static void afficherProduit(String[] produit) {
        System.out.printf("ID : %s | Nom : %s | Prix : %s | Quantité : %s%n",
                produit[0], produit[1], produit[2], produit[3]);
    }

    private static String[] trouverProduit(int id) {
        for (String[] produit : produits) {
            if (Integer.parseInt(produit[0]) == id) return produit;
        }
        return null;
    }

    private static String[] demanderProduit() {
        Integer id = lireEntier("Identifiant du produit : ");
        if (id == null) return null;
        String[] produit = trouverProduit(id);
        if (produit == null) System.out.println("Produit introuvable.");
        return produit;
    }

    private static void rechercherProduit() {
        String[] produit = demanderProduit();
        if (produit != null) afficherProduit(produit);
    }

    private static void modifierQuantite() {
        String[] produit = demanderProduit();
        if (produit == null) return;
        Integer quantite = lireEntier("Nouvelle quantité : ");
        if (quantite == null) return;
        if (quantite < 0) {
            System.out.println("La quantité ne peut pas être négative.");
            return;
        }
        produit[3] = String.valueOf(quantite);
        System.out.println("Quantité modifiée.");
    }

    private static void supprimerProduit() {
        String[] produit = demanderProduit();
        if (produit == null) return;
        produits.remove(produit);
        System.out.println("Produit supprimé.");
    }

    private static void calculerRemise() {
        String[] produit = demanderProduit();
        if (produit == null) return;
        BigDecimal remise = lireDecimal("Remise en pourcentage (0 à 100) : ");
        if (remise == null) return;
        if (remise.signum() < 0 || remise.compareTo(new BigDecimal("100")) > 0) {
            System.out.println("La remise doit être comprise entre 0 et 100.");
            return;
        }
        BigDecimal prix = new BigDecimal(produit[2]);
        BigDecimal prixFinal = prix.multiply(BigDecimal.ONE.subtract(remise.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Prix unitaire après remise : " + prixFinal.toPlainString());
        // Le calcul ne modifie pas le prix enregistré.
    }

    private static String lireTexte(String message) {
        System.out.print(message);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    private static Integer lireEntier(String message) {
        String saisie = lireTexte(message);
        if (saisie == null) return null;
        try {
            return Integer.parseInt(saisie);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez saisir un nombre entier valide.");
            return null;
        }
    }

    private static BigDecimal lireDecimal(String message) {
        String saisie = lireTexte(message);
        if (saisie == null) return null;
        try {
            return new BigDecimal(saisie.replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("Veuillez saisir un nombre décimal valide (exemple : 12,50).");
            return null;
        }
    }
}
