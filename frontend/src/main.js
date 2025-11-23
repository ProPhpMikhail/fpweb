import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import VueGoogleMaps from 'vue3-google-map';

const app = createApp(App);

app.use(router);

app.use(VueGoogleMaps, {
    load: {
        key: 'AIzaSyA2CZsUrSywE1X3BUBsIC22y9keUs0A_bc',
    },
});

app.mount('#app');
