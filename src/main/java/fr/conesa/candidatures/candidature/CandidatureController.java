package fr.conesa.candidatures.candidature;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService service;

    public CandidatureController(CandidatureService service) { this.service = service; }

    @GetMapping
    public List<Candidature> lister(@RequestParam(required = false) Statut statut) { return service.lister(statut); }

    @GetMapping("/a-relancer")
    public List<Candidature> aRelancer() { return service.aRelancer(); }

    @GetMapping("/echeances")
    public List<Candidature> echeances() { return service.echeancesProches(); }

    @GetMapping("/{id}")
    public Candidature lire(@PathVariable Long id) { return service.trouver(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidature creer(@Valid @RequestBody Candidature c) { return service.creer(c); }

    @PutMapping("/{id}")
    public Candidature modifier(@PathVariable Long id, @Valid @RequestBody Candidature c) { return service.modifier(id, c); }

    @PostMapping("/{id}/relancer")
    public Candidature relancer(@PathVariable Long id) { return service.relancer(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) { service.supprimer(id); }
}
