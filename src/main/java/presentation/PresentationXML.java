package presentation;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresentationXML {
    public static void main(String[] args) {
        // Chargement du contexte Spring à partir du fichier XML
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Récupération du bean metier
        IMetier metier = (IMetier) context.getBean("metier");

        // Exécution et affichage du résultat
        System.out.println("Résultat (XML) = " + metier.calcul());
    }
}
