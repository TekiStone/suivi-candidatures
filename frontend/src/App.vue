<template>
  <header class="entete">
    <router-link to="/" class="titre">Suivi des candidatures</router-link>
    <router-link to="/candidatures/nouvelle" class="bouton">+ Nouvelle candidature</router-link>
  </header>
  <main>
    <router-view />
  </main>
</template>
