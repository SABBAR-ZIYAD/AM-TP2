# TP 2 — Injection de dépendances avec Spring (IoC, Annotations, XML & Profils)

> **ARCHITECTURE MICROSERVICES LAB**  
> Édition pédagogique : `TP2-SPRING-DI-2026`

---

Ce projet met en pratique le principe d'**inversion de contrôle (IoC)** et l'**injection de dépendances (DI)** en utilisant le framework **Spring 6 (spring-context)** selon 3 approches complémentaires :
1. **Annotations Java** (`@Component`, `@Autowired`, `@Qualifier`)
2. **Configuration XML** (`applicationContext.xml`, `ClassPathXmlApplicationContext`)
3. **Profils d'environnement Spring** (`@Profile("dev")`, `@Profile("prod")`)

---

## 📋 Table des matières

1. [Objectifs du TP](#-objectifs-du-tp)
2. [Structure du projet](#-structure-du-projet)
3. [Détail des étapes réalisées](#-détail-des-étapes-réalisées)
   - [Partie 1 : Création et configuration Maven](#partie-1--création-et-configuration-maven)
   - [Partie 2 : Couches DAO et Métier](#partie-2--couches-dao-et-métier)
   - [Partie 3 : Tests unitaires (Mock DAO)](#partie-3--tests-unitaires-mock-dao)
   - [Partie 4 : Configuration XML alternative](#partie-4--configuration-xml-alternative)
   - [Partie 5 : Profils d'environnement Spring (dev / prod)](#partie-5--profils-denvironnement-spring-dev--prod)
4. [Compilation et exécution](#-compilation-et-exécution)

---

## 📁 Structure du projet

```text
TP 2/
├── pom.xml                                      # Configuration Maven (Spring Context 6.1.2 & JUnit 4.13.2)
├── README.md                                    # Documentation du TP
├── .project / .classpath / .gitignore           # Métadonnées Eclipse & Git
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── dao/
    │   │   │   ├── IDao.java                    # Interface DAO
    │   │   │   ├── DaoImpl.java                 # Bean "dao" (@Profile("prod"), 100.0)
    │   │   │   └── DaoImpl2.java                # Bean "dao2" (@Profile("dev"), 150.0)
    │   │   ├── metier/
    │   │   │   ├── IMetier.java                 # Interface Métier
    │   │   │   └── MetierImpl.java              # Implémentation Métier avec @Autowired et setter
    │   │   └── presentation/
    │   │       ├── Presentation.java            # Présentation classique UML
    │   │       ├── Presentation2.java           # Présentation avec profil "dev"
    │   │       └── PresentationXML.java         # Présentation avec contexte XML
    │   └── resources/
    │       └── applicationContext.xml           # Configuration Spring XML avec injection par setter
    └── test/
        └── java/
            └── metier/
                └── MetierImplTest.java          # Tests unitaires JUnit (Mock DAO + Profils + XML)
```

---

## 🛠️ Détail des étapes réalisées

### Partie 1 : Création et configuration Maven
- **Group Id** : `com.example`
- **Artifact Id** : `springdi`
- **Version** : `1.0-SNAPSHOT`
- **Dépendances** :
  - `org.springframework:spring-context:6.1.2`
  - `junit:junit:4.13.2` (scope test)

### Partie 2 : Couches DAO et Métier
- **`IDao`** : Interface avec la méthode `double getValue()` (et méthode de compatibilité `getData()`).
- **`IMetier`** : Interface avec la méthode `double calcul()`.
- **`MetierImpl`** : Classe métier implémentant `IMetier`. Reçoit une dépendance `IDao` injectée soit par annotation (`@Autowired`), soit par constructeur, soit par setter (`setDao`).

### Partie 3 : Tests unitaires (Mock DAO)
Dans `src/test/java/metier/MetierImplTest.java` :
```java
@Test
public void testCalcul() {
    IDao dao = new IDao() {
        @Override
        public double getValue() {
            return 10.0;
        }
    };

    MetierImpl metier = new MetierImpl();
    metier.setDao(dao);

    double result = metier.calcul();
    Assert.assertEquals(20.0, result, 0.001); // 10.0 * 2 = 20.0
}
```
Ce test valide la logique métier de manière totalement isolée sans dépendre d'une implémentation concrète de base de données.

### Partie 4 : Configuration XML alternative
1. Fichier `src/main/resources/applicationContext.xml` :
```xml
<beans ...>
    <bean id="dao" class="dao.DaoImpl"></bean>
    <bean id="dao2" class="dao.DaoImpl2"></bean>
    <bean id="metier" class="metier.MetierImpl">
        <property name="dao" ref="dao2"></property>
    </bean>
</beans>
```

2. Classe `presentation.PresentationXML` :
```java
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
IMetier metier = (IMetier) context.getBean("metier");
System.out.println("Résultat (XML) = " + metier.calcul());
```
**Sortie :**
```text
Version de développement
Résultat (XML) = 300.0
```

### Partie 5 : Profils d'environnement Spring (dev / prod)
Les implémentations DAO sont annotées avec `@Profile` :
- `DaoImpl` : `@Profile("prod")` -> affiche `"Version de production"`, valeur = `100.0`.
- `DaoImpl2` : `@Profile("dev")` -> affiche `"Version de développement"`, valeur = `150.0`.

La classe `Presentation2` active dynamiquement le profil `dev` :
```java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
context.getEnvironment().setActiveProfiles("dev");
context.register(Presentation2.class);
context.refresh();

IMetier metier = context.getBean(IMetier.class);
System.out.println("Résultat = " + metier.calcul());
```
**Sortie :**
```text
Version de développement
Résultat = 300.0
```

Si le profil `"prod"` est activé :
```text
Version de production
Résultat = 200.0
```

---

## 🚀 Compilation et exécution

Depuis le terminal (PowerShell) dans le répertoire `TP 2` :

1. **Lancer tous les tests unitaires :**
   ```powershell
   mvn clean test
   ```

2. **Exécuter la version Profils Spring (`Presentation2`) :**
   ```powershell
   mvn exec:java "-Dexec.mainClass=presentation.Presentation2"
   ```

3. **Exécuter la version XML (`PresentationXML`) :**
   ```powershell
   mvn exec:java "-Dexec.mainClass=presentation.PresentationXML"
   ```

---

<div align="center">

`ARCHITECTURE MICROSERVICES LAB · TP2-SPRING-DI-2026`

*Maîtriser l'inversion de contrôle et l'injection de dépendances avec Spring Framework.*

</div>
