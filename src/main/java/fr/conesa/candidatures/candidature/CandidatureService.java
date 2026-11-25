package fr.conesa.candidatures.candidature;

import java.time.Clock;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CandidatureService {

    public static final int DELAI_RELANCE_JOURS = 7;
    public static final int ALERTE_ECHEANCE_JOURS = 3;
    static final Set<Statut> EN_ATTENTE = EnumSet.of(Statut.ENVOYEE, Statut.RELANCEE);
    static final Set<Statut> CLOTUREES = EnumSet.of(Statut.OFFRE, Statut.REFUSEE, Statut.ABANDONNEE);

    private final CandidatureRepository repo;
    private final Clock horloge;

    public CandidatureService(CandidatureRepository repo, Clock horloge) {
        this.repo = repo;
        this.horloge = horloge;
    }

    @Transactional(readOnly = true)
    public List<Candidature> lister(Statut statut) {
        return statut == null ? repo.findAll() : repo.findByStatutOrderByDateLimiteAsc(statut);
    }

    @Transactional(readOnly = true)
    public Candidature trouver(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature " + id + " introuvable"));
    }

    public Candidature creer(Candidature c) {
        appliquerRegleRelance(c);
        return repo.save(c);
    }

    public Candidature modifier(Long id, Candidature c) {
        Candidature existante = trouver(id);
        existante.majDepuis(c);
        appliquerRegleRelance(existante);
        return repo.save(existante);
    }

    public void supprimer(Long id) {
        repo.delete(trouver(id));
    }

    /** Enregistre une relance faite aujourd'hui et planifie la suivante. */
    public Candidature relancer(Long id) {
        Candidature c = trouver(id);
        c.setStatut(Statut.RELANCEE);
        c.setDateRelance(aujourdhui().plusDays(DELAI_RELANCE_JOURS));
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public List<Candidature> aRelancer() {
        return repo.findByStatutInAndDateRelanceLessThanEqualOrderByDateRelanceAsc(EN_ATTENTE, aujourdhui());
    }

    @Transactional(readOnly = true)
    public List<Candidature> echeancesProches() {
        return repo.findByStatutAndDateLimiteLessThanEqualOrderByDateLimiteAsc(
            Statut.A_ENVOYER, aujourdhui().plusDays(ALERTE_ECHEANCE_JOURS));
    }

    /** Règle métier : relance à J+7 après l'envoi, aucune relance une fois la candidature close. */
    void appliquerRegleRelance(Candidature c) {
        if (CLOTUREES.contains(c.getStatut())) {
            c.setDateRelance(null);
            return;
        }
        if (c.getDateRelance() == null && EN_ATTENTE.contains(c.getStatut()) && c.getDateCandidature() != null) {
            c.setDateRelance(c.getDateCandidature().plusDays(DELAI_RELANCE_JOURS));
        }
    }

    private LocalDate aujourdhui() {
        return LocalDate.now(horloge);
    }
}
