<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { api, STATUTS, nettoyer } from '../api.js'

const props = defineProps({ id: String })
const router = useRouter()
const estNouvelle = computed(() => props.id === 'nouvelle')

const vide = () => ({
  entreprise: '', poste: '', lieu: '', lien: '', source: '',
  dateCandidature: '', dateLimite: '', dateRelance: '', statut: 'A_ENVOYER', notes: ''
})
const contactVide = () => ({ nom: '', fonction: '', email: '', telephone: '', linkedin: '' })

const c = ref(vide())
const contacts = ref([])
const nouveauContact = ref(contactVide())
const erreur = ref('')
const message = ref('')

async function charger() {
  erreur.value = ''
  message.value = ''
  if (estNouvelle.value) {
    c.value = vide()
    contacts.value = []
    return
  }
  try {
    c.value = await api.candidature(props.id)
    contacts.value = await api.contacts(props.id)
  } catch (e) {
    erreur.value = e.message
  }
}

async function enregistrer() {
  erreur.value = ''
  message.value = ''
  try {
    if (estNouvelle.value) {
      const creee = await api.creer(nettoyer(c.value))
      router.replace(`/candidatures/${creee.id}`)
    } else {
      c.value = await api.modifier(props.id, nettoyer(c.value))
      message.value = 'Enregistré'
    }
  } catch (e) {
    erreur.value = e.message
  }
}

async function supprimer() {
  if (!confirm(`Supprimer la candidature ${c.value.entreprise} et ses contacts ?`)) return
  try {
    await api.supprimer(props.id)
    router.push('/')
  } catch (e) {
    erreur.value = e.message
  }
}

async function ajouterContact() {
  try {
    await api.ajouterContact(props.id, nettoyer(nouveauContact.value))
    nouveauContact.value = contactVide()
    contacts.value = await api.contacts(props.id)
  } catch (e) {
    erreur.value = e.message
  }
}

async function supprimerContact(ct) {
  if (!confirm(`Supprimer le contact ${ct.nom} ?`)) return
  try {
    await api.supprimerContact(ct.id)
    contacts.value = await api.contacts(props.id)
  } catch (e) {
    erreur.value = e.message
  }
}

onMounted(charger)
watch(() => props.id, charger)
</script>

<template>
  <router-link to="/">&larr; Retour à la liste</router-link>
  <h2>{{ estNouvelle ? 'Nouvelle candidature' : `${c.entreprise} : ${c.poste}` }}</h2>
  <p v-if="erreur" class="erreur">{{ erreur }}</p>
  <p v-if="message" class="succes">{{ message }}</p>

  <form class="grille" @submit.prevent="enregistrer">
    <label>Entreprise *<input v-model="c.entreprise" required /></label>
    <label>Poste *<input v-model="c.poste" required /></label>
    <label>Lieu<input v-model="c.lieu" /></label>
    <label>Source<input v-model="c.source" placeholder="Passerelles, candidature directe..." /></label>
    <label class="large">Lien de l'offre
      <input v-model="c.lien" type="url" />
      <a v-if="c.lien" :href="c.lien" target="_blank" rel="noopener">Ouvrir l'offre</a>
    </label>
    <label>Statut
      <select v-model="c.statut">
        <option v-for="(libelle, code) in STATUTS" :key="code" :value="code">{{ libelle }}</option>
      </select>
    </label>
    <label>Date d'envoi<input v-model="c.dateCandidature" type="date" /></label>
    <label>Date limite<input v-model="c.dateLimite" type="date" /></label>
    <label>Prochaine relance<input v-model="c.dateRelance" type="date" /></label>
    <label class="large">Notes<textarea v-model="c.notes" rows="4"></textarea></label>
    <div class="actions large">
      <button type="submit">{{ estNouvelle ? 'Créer' : 'Enregistrer' }}</button>
      <button v-if="!estNouvelle" type="button" class="danger" @click="supprimer">Supprimer</button>
    </div>
  </form>

  <section v-if="!estNouvelle">
    <h3>Contacts</h3>
    <table v-if="contacts.length">
      <thead><tr><th>Nom</th><th>Fonction</th><th>E-mail</th><th>Téléphone</th><th></th></tr></thead>
      <tbody>
        <tr v-for="ct in contacts" :key="ct.id">
          <td>
            {{ ct.nom }}
            <a v-if="ct.linkedin" :href="ct.linkedin" target="_blank" rel="noopener">(LinkedIn)</a>
            <div v-if="ct.notes"><small>{{ ct.notes }}</small></div>
          </td>
          <td>{{ ct.fonction }}</td>
          <td><a v-if="ct.email" :href="`mailto:${ct.email}`">{{ ct.email }}</a></td>
          <td>{{ ct.telephone }}</td>
          <td><button class="danger petit" @click="supprimerContact(ct)">Supprimer</button></td>
        </tr>
      </tbody>
    </table>
    <p v-else>Aucun contact pour le moment.</p>

    <form class="contact" @submit.prevent="ajouterContact">
      <input v-model="nouveauContact.nom" placeholder="Nom *" required />
      <input v-model="nouveauContact.fonction" placeholder="Fonction" />
      <input v-model="nouveauContact.email" type="email" placeholder="E-mail" />
      <input v-model="nouveauContact.telephone" placeholder="Téléphone" />
      <input v-model="nouveauContact.linkedin" placeholder="URL LinkedIn" />
      <button type="submit">Ajouter</button>
    </form>
  </section>
</template>
