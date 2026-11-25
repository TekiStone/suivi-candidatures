import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    // En dev, les appels /api sont transmis à Spring Boot
    proxy: { '/api': 'http://localhost:8080' }
  },
  build: {
    // Le build atterrit dans les ressources statiques de Spring Boot (étape 4)
    outDir: '../src/main/resources/static',
    emptyOutDir: true
  }
})
