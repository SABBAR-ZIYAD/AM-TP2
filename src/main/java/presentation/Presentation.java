package presentation;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Classe Presentation correspondant au diagramme de classes UML du TP 2.
 */
@Configuration
@ComponentScan(basePackages = {"dao", "metier", "com.example.dao", "com.example.metier"})
public class Presentation {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Presentation.class);
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Résultat = " + metier.calcul());
    }
}
