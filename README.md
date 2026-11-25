# Suivi des candidatures

[![CI](https://github.com/TekiStone/suivi-candidatures/actions/workflows/ci.yml/badge.svg)](https://github.com/TekiStone/suivi-candidatures/actions/workflows/ci.yml)

Application web pour suivre ses candidatures : offres, contacts, dates limites et relances.
Développée pour mon propre usage pendant ma recherche d'emploi, et déployée sur mon serveur personnel (Proxmox, conteneur LXC).

## Fonctionnalités
- Suivi des candidatures : entreprise, poste, lieu, source, lien de l'offre, statut, notes
- Contacts associés à chaque candidature
- **Relances automatiques** : relance planifiée à J+7 après l'envoi, liste « à relancer », bouton « relance faite » qui replanifie à J+7
- Alerte sur les dates limites proches (3 jours)
- Rappel quotidien planifié (tâche `@Scheduled`)

## Stack technique
| Couche | Technologies |
|---|---|
| Back-end | Java 17, Spring Boot 4, Spring Web MVC, Spring Data JPA (Hibernate), Bean Validation |
| Base de données | PostgreSQL, migrations versionnées avec Flyway |
| Front-end | Vue.js 3 (Composition API), Vue Router, Vite |
| Tests | JUnit 5, AssertJ (règles métier testées avec une horloge injectée) |
| Exploitation | Linux (LXC sur Proxmox), service systemd, GitHub Actions |

## Architecture
~~~
frontend/   Interface Vue.js (compilée dans src/main/resources/static au build)
src/main/java/fr/conesa/candidatures/
  candidature/   Entité, repository, service (règles métier), contrôleur REST
  contact/       Contacts liés aux candidatures
  relance/       Rappel quotidien planifié
  config/        Configuration (horloge, planification)
src/main/resources/db/migration/   Scripts Flyway
~~~

## API REST (extrait)
| Méthode | URL | Rôle |
|---|---|---|
| GET | `/api/candidatures?statut=` | Liste, filtrable par statut |
| GET | `/api/candidatures/a-relancer` | Candidatures dont la relance est due |
| GET | `/api/candidatures/echeances` | Dates limites dans les 3 jours |
| POST | `/api/candidatures/{id}/relancer` | Enregistre une relance et planifie la suivante |
| GET/POST | `/api/candidatures/{id}/contacts` | Contacts d'une candidature |

## Lancer en local
Prérequis : Java 17+, Node.js 20.19+, PostgreSQL avec une base `candidatures`.

~~~bash
export DB_PASSWORD='...'
./build.sh
java -jar target/suivi-candidatures-0.0.1-SNAPSHOT.jar
~~~
Puis http://localhost:8080

En développement : `./mvnw spring-boot:run` pour l'API et `cd frontend && npm run dev` pour l'interface (http://localhost:5173).
