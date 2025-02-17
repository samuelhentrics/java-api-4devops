import { Component } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-bids',
  standalone: true,
  templateUrl: './add-bids.component.html',
  styleUrls: ['./add-bids.component.css'],
  imports: [FormsModule,  HttpClientModule]
})
export class AddBidsComponent {
  bid = {
    bid: 0,
    idProduct: 0,
    idSeller: 0,
    message: " ",
    dateCreate: new Date().toISOString(),
    dateUpdate: new Date().toISOString(),
    active: true
  };

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const token = 'bearer token'; // Remplacer par le token récupéré dynamiquement

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    console.log('Soumission de l\'offre', this.bid);
    this.http.post('http://localhost:8080/api-0.0.1/api/Bids', this.bid, { headers })
      .subscribe(
        response => {
          console.log('Offre ajoutée avec succès', response);
          this.router.navigate(['/bids']); // Redirige vers la page des offres après succès
        },
        error => {
          console.error('Erreur lors de l\'ajout de l\'offre', error);
        }
      );
  }

  isFormValid() {
    return this.bid.idProduct && this.bid.idSeller && this.bid.message;
  }
}
