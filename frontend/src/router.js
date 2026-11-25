import { createRouter, createWebHashHistory } from 'vue-router'
import ListeCandidatures from './views/ListeCandidatures.vue'
import FicheCandidature from './views/FicheCandidature.vue'

export default createRouter({
  // Mode "hash" (#/...) : aucune configuration serveur nécessaire une fois intégré à Spring Boot
  history: createWebHashHistory(),
  routes: [
    { path: '/', component: ListeCandidatures },
    { path: '/candidatures/:id', component: FicheCandidature, props: true }
  ]
})
