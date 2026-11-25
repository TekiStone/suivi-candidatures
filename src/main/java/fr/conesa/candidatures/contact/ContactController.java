package fr.conesa.candidatures.contact;

import fr.conesa.candidatures.candidature.CandidatureService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactRepository repo;
    private final CandidatureService candidatures;

    public ContactController(ContactRepository repo, CandidatureService candidatures) {
        this.repo = repo;
        this.candidatures = candidatures;
    }

    @GetMapping("/candidatures/{id}/contacts")
    public List<Contact> lister(@PathVariable Long id) {
        candidatures.trouver(id);
        return repo.findByCandidature_IdOrderByNomAsc(id);
    }

    @PostMapping("/candidatures/{id}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    public Contact ajouter(@PathVariable Long id, @Valid @RequestBody Contact c) {
        c.setCandidature(candidatures.trouver(id));
        return repo.save(c);
    }

    @PutMapping("/contacts/{id}")
    public Contact modifier(@PathVariable Long id, @Valid @RequestBody Contact c) {
        Contact existant = trouver(id);
        existant.majDepuis(c);
        return repo.save(existant);
    }

    @DeleteMapping("/contacts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) { repo.delete(trouver(id)); }

    private Contact trouver(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact " + id + " introuvable"));
    }
}
