import { Component } from '@angular/core';
import { BidsTableComponent } from '../../bids-table/bids-table.component';

@Component({
  selector: 'app-bids',
  standalone: true,
  imports: [BidsTableComponent],
  templateUrl: './bids.component.html',
  styleUrl: './bids.component.css'
})
export class BidsComponent {

}
