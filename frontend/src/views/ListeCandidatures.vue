<script setup>
import { ref, computed, onMounted } from 'vue'
import { api, STATUTS, formatDate } from '../api.js'

const candidatures = ref([])
const aRelancer = ref([])
const echeances = ref([])
const filtre = ref('')
const erreur = ref('')

async function charger() {
  try {
    erreur.value = ''
    const [toutes, relances, limites] = await Promise.all([api.candidatures(), api.aRelancer(), api.echeances()])
    candidatures.value = toutes
    aRelancer.value = relances
    echeances.value = limites
  } catch (e) {
    erreur.value = e.message
  }
}

async function relanceFaite(c) {
  try {
    await api.relancer(c.id)
    await charger()
  } catch (e) {
    erreur.value = e.message
  }
}

const affichees = computed(() =>
  candidatures.value
    .filter((c) => !filtre.value || c.statut === filtre.value)
    .sort((a, b) => (b.dateCandidature || '').localeCompare(a.dateCandidature || ''))
)

onMounted(charger)
</script>

<template>
  <p v-if="erreur" class="erreur">{{ erreur }}</p>

  <section v-if="aRelancer.length || echeances.length" class="alertes">
    <div v-if="aRelancer.length">
      <h2>À relancer</h2>
      <ul>
        <li v-for="c in aRelancer" :key="c.id">
          <router-link :to="`/candidatures/${c.id}`">{{ c.entreprise }}</router-link>, {{ c.poste }}
          <small>(prévue le {{ formatDate(c.dateRelance) }})</small>
          <button @click="relanceFaite(c)">Relance faite</button>
        </li>
      </ul>
    </div>
    <div v-if="echeances.length">
      <h2>Dates limites proches</h2>
      <ul>
        <li v-for="c in echeances" :key="c.id">
          <router-link :to="`/candidatures/${c.id}`">{{ c.entreprise }}</router-link>, {{ c.poste }} :
          avant le {{ formatDate(c.dateLimite) }}
        </li>
      </ul>
    </div>
  </section>

  <section>
    <div class="barre">
      <h2>Candidatures ({{ affichees.length }})</h2>
      <select v-model="filtre">
        <option value="">Tous les statuts</option>
        <option v-for="(libelle, code) in STATUTS" :key="code" :value="code">{{ libelle }}</option>
      </select>
    </div>
    <table>
      <thead>
        <tr>
          <th>Entreprise</th><th>Poste</th><th>Lieu</th><th>Statut</th>
          <th>Envoyée le</th><th>Date limite</th><th>Relance</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="c in affichees" :key="c.id" class="cliquable" @click="$router.push(`/candidatures/${c.id}`)">
          <td>{{ c.entreprise }}</td>
          <td>{{ c.poste }}</td>
          <td>{{ c.lieu }}</td>
          <td><span :class="['statut', c.statut]">{{ STATUTS[c.statut] }}</span></td>
          <td>{{ formatDate(c.dateCandidature) }}</td>
          <td>{{ formatDate(c.dateLimite) }}</td>
          <td>{{ formatDate(c.dateRelance) }}</td>
        </tr>
      </tbody>
    </table>
  </section>
</template>
