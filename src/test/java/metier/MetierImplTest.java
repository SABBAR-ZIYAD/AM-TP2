package metier;

import dao.IDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import presentation.Presentation2;

public class MetierImplTest {

    @Test
    public void testCalcul() {
        // Création d'une implémentation fictive de IDao pour le test
        IDao dao = new IDao() {
            @Override
            public double getValue() {
                return 10.0;
            }
        };

        // Création de l'instance de MetierImpl et injection manuelle de la dépendance
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);

        // Vérification que le calcul donne le résultat attendu
        double result = metier.calcul();
        Assert.assertEquals(20.0, result, 0.001);  // 10.0 * 2 = 20.0
    }

    @Test
    public void testSpringContextProfileDev() {
        // Test d'intégration avec le profil "dev"
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("dev");
        context.register(Presentation2.class);
        context.refresh();

        IMetier metier = context.getBean(IMetier.class);
        Assert.assertNotNull(metier);
        // Profil dev -> DaoImpl2 (150.0) -> calcul() = 300.0
        Assert.assertEquals(300.0, metier.calcul(), 0.001);
        context.close();
    }

    @Test
    public void testSpringContextProfileProd() {
        // Test d'intégration avec le profil "prod"
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("prod");
        context.register(Presentation2.class);
        context.refresh();

        IMetier metier = context.getBean(IMetier.class);
        Assert.assertNotNull(metier);
        // Profil prod -> DaoImpl (100.0) -> calcul() = 200.0
        Assert.assertEquals(200.0, metier.calcul(), 0.001);
        context.close();
    }

    @Test
    public void testXmlConfiguration() {
        // Test d'intégration avec la configuration XML
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IMetier metier = (IMetier) context.getBean("metier");
        Assert.assertNotNull(metier);
        // XML injecte dao2 (150.0) -> calcul() = 300.0
        Assert.assertEquals(300.0, metier.calcul(), 0.001);
    }
}
