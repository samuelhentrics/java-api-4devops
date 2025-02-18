import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ContactComponent } from './pages/contact/contact.component';
import { BidsComponent } from './pages/bids/bids.component';
import { AddBidsComponent } from './pages/bids/add-bids/add-bids.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Page d'accueil
  { path: 'contact', component: ContactComponent }, // Page "À propos"
  { path: 'bids', component: BidsComponent }, // Page "Enchères"
  { path: 'bids/add', component: AddBidsComponent }, // Page "Ajouter une enchère"
  { path: '**', redirectTo: '' } // Redirection des routes inconnues
];
