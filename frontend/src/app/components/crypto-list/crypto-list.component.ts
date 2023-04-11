import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { CryptoListService } from './crypto-list.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-crypto-list',
  templateUrl: './crypto-list.component.html',
  styleUrls: ['./crypto-list.component.scss']
})
export class CryptoListComponent {
  bannerData: any = [];
  currency : string = "USD"
  dataSource!: MatTableDataSource<any>;
  displayedColumns: string[] = ['symbol', 'current_price', 'price_change_percentage_24h', 'market_cap'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  constructor(private service: CryptoListService, private router: Router) { }

  ngOnInit(): void {
    this.getAllData();
    this.getBannerData();
  }

  getBannerData(): void {
    this.service.getTrendingCurrency(this.currency)
      .subscribe(res => {
        this.bannerData = res;
      })
  }

  getAllData(): void {
    this.service.getCurrency(this.currency)
      .subscribe(res => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      })
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getDetails(row: any): void {
    this.router.navigate(['coin-detail',row.id])
  }
}
