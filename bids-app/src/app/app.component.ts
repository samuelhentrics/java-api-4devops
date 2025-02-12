import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BidsTableComponent } from './bids-table/bids-table.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, BidsTableComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'bids-app';
}
