package fr.conesa.candidatures.candidature;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureRepository repo;

    public CandidatureController(CandidatureRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Candidature> lister(@RequestParam(required = false) Statut statut) {
        return statut == null ? repo.findAll() : repo.findByStatutOrderByDateLimiteAsc(statut);
    }

    @GetMapping("/{id}")
    public Candidature lire(@PathVariable Long id) { return trouver(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidature creer(@Valid @RequestBody Candidature c) { return repo.save(c); }

    @PutMapping("/{id}")
    public Candidature modifier(@PathVariable Long id, @Valid @RequestBody Candidature c) {
        Candidature existante = trouver(id);
        existante.majDepuis(c);
        return repo.save(existante);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) { repo.delete(trouver(id)); }

    private Candidature trouver(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature " + id + " introuvable"));
    }
}
