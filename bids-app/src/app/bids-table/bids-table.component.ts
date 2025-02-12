import { Component, OnInit, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Bid {
  id: number;
  bid: number;
  idProduct: number;
  idBuyer: number;
  idSeller: number;
  message: string;
  dateCreate: number;
  dateUpdate: number;
  active: boolean;
}

@Component({
  selector: 'app-bids-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './bids-table.component.html',
  styleUrls: ['./bids-table.component.css']
})
export class BidsTableComponent implements OnInit {
  bids: Bid[] = [];
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api-0.0.1/api/Bids';

  ngOnInit(): void {
    this.fetchBids();
  }

  fetchBids(): void {
    const headers = new HttpHeaders({
      'accept': 'application/json',
      'Authorization': 'bearer ok'
    });

    this.http.get<Bid[]>(this.apiUrl, { headers }).subscribe(
      data => this.bids = data,
      error => console.error('Erreur lors de la récupération des enchères', error)
    );
  }
}
