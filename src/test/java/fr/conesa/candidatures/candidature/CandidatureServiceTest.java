package fr.conesa.candidatures.candidature;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class CandidatureServiceTest {

    private final Clock horloge = Clock.fixed(Instant.parse("2026-11-25T09:00:00Z"), ZoneId.of("Europe/Paris"));
    private final CandidatureService service = new CandidatureService(null, horloge);

    private Candidature candidature(Statut statut, LocalDate envoi) {
        Candidature c = new Candidature();
        c.setStatut(statut);
        c.setDateCandidature(envoi);
        return c;
    }

    @Test
    void relancePrevueAJPlus7ApresEnvoi() {
        Candidature c = candidature(Statut.ENVOYEE, LocalDate.of(2026, 11, 19));
        service.appliquerRegleRelance(c);
        assertThat(c.getDateRelance()).isEqualTo(LocalDate.of(2026, 11, 26));
    }

    @Test
    void pasDeRelanceTantQueRienNEstEnvoye() {
        Candidature c = candidature(Statut.A_ENVOYER, null);
        service.appliquerRegleRelance(c);
        assertThat(c.getDateRelance()).isNull();
    }

    @Test
    void relanceManuelleConservee() {
        Candidature c = candidature(Statut.ENVOYEE, LocalDate.of(2026, 11, 19));
        c.setDateRelance(LocalDate.of(2026, 12, 1));
        service.appliquerRegleRelance(c);
        assertThat(c.getDateRelance()).isEqualTo(LocalDate.of(2026, 12, 1));
    }

    @Test
    void plusDeRelanceUneFoisCloturee() {
        Candidature c = candidature(Statut.REFUSEE, LocalDate.of(2026, 11, 19));
        c.setDateRelance(LocalDate.of(2026, 11, 26));
        service.appliquerRegleRelance(c);
        assertThat(c.getDateRelance()).isNull();
    }
}
