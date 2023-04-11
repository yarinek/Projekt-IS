import { formatDate } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ChartConfiguration, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { catchError, tap } from 'rxjs';
import { dateLessThanToday } from 'src/app/shared/utils/utils';
import { CryptoListService } from '../crypto-list.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Category, Coins } from './coin-visualisation.model';

@Component({
  selector: 'app-coin-visualisation',
  templateUrl: './coin-visualisation.component.html',
  styleUrls: ['./coin-visualisation.component.scss']
})
export class CoinVisualisationComponent {
  coinData : any;
  coinId !: string;
  days : number = 30;
  currency : string = "USD";
  form = new UntypedFormGroup({
    start: new UntypedFormControl(null, [Validators.required, dateLessThanToday()]),
    end: new UntypedFormControl(null, Validators.required),
  })
  showChart = false;
  lineChartData: ChartConfiguration['data'] = {
    datasets: [
      {
        data: [],
        label: `Price Trends`,
        backgroundColor: 'rgba(148,159,177,0.2)',
        borderColor: '#009688',
        pointBackgroundColor: '#009688',
        pointBorderColor: '#009688',
        pointHoverBackgroundColor: '#009688',
        pointHoverBorderColor: '#009688',

      }
    ],
    labels: []
  };
  lineChartOptions: ChartConfiguration['options'] = {
    elements: {
      point: {
        radius: 1
      }
    },

    plugins: {
      legend: { display: true },
    }
  };
  lineChartType: ChartType = 'line';
  @ViewChild(BaseChartDirective) myLineChart !: BaseChartDirective;

  get dateAfterTodayError(): boolean {
    //@ts-ignore
    return this.form.get('start')?.errors?.dateAfterToday;
  };

  events: any[] = [];
  filtered = false;
  displayedColumns: string[] = ['coin', 'category', 'startDate', 'endDate', 'title', 'url'];

  constructor(private service: CryptoListService, private activatedRoute: ActivatedRoute, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(val=>{
      this.coinId = val['id'];
    });
    
  }

  getGraphData(from: number, to: number): void {
    this.service.getGrpahicalCurrencyDataRange(this.coinId, this.currency, from, to).pipe(
      tap((response) => {
        setTimeout(() => {
          this.myLineChart.chart?.update();
        }, 200);
        this.lineChartData.datasets[0].data = response.prices.map((a:any)=>{
          return a[1];
        });
        this.lineChartData.labels = response.prices.map((a:any)=>{
          let date = new Date(a[0]);
          let time = date.getHours() > 12 ?
          `${date.getHours() - 12}: ${date.getMinutes()} PM` :
          `${date.getHours()}: ${date.getMinutes()} AM`
          return this.days === 1 ? time : date.toLocaleDateString();
        })
        this.showChart = true;
      })
    )
    .subscribe()
  }

  getDetails(): void {
    const { start, end } = this.form.value;
    if(this.form.invalid) {
      return;
    }
    const from = this.convertDateToUnixTimestamp(start);
    const to = this.convertDateToUnixTimestamp(end);
    this.getGraphData(from, to);
    const startDate = formatDate(start, 'YYYY-MM-dd', 'en-US');
    const endDate = formatDate(end, 'YYYY-MM-dd', 'en-US');
    this.service.getEvents(startDate, endDate).pipe(
      tap((response) => {
        const soapEvents = response['SOAP-ENV:Envelope']['SOAP-ENV:Body']['ns2:getEventsResponse']?.['ns2:events'];
        if(soapEvents) {
          const events = soapEvents.map((item: any) => {
            return {
              // @ts-ignore
              category: Category[item['ns2:category']['#text']],
              coin: item['ns2:coin']['#text'],
              endDate: item['ns2:endDate']['#text'],
              startDate: item['ns2:startDate']['#text'],
              title: item['ns2:title']['#text'],
              url: item['ns2:url']['#text'],
            }
          })
          //@ts-ignore
          this.events = events.filter((item: any) => item.coin === Coins[this.coinId]);
        }
        this.filtered = true;
      })
    ).subscribe();
  }

  importDatabase(event: any): void {
    const file = event.target.files[0];
    if(file) {
      const formData = new FormData();
      formData.append('file', file);
      this.service.importDatabase(formData).pipe(
        tap((response) => {
          this.openSnackbar('Poprawnie zaimportowano bazę danych');
        }),
        catchError((err) => {
          if(err.status === 403) {
            this.openSnackbar('Nie masz uprawnień do tej czynności')
          }
          throw err;
        })
      ).subscribe();
    }
    event.target.value = null;
  }

  exportDatabase(): void {
    this.service.exportDatabase().pipe(
      tap((response) => {
        this.downloadFile(response, 'db.sql');
      }),
      catchError((err) => {
        if(err.status === 403) {
          this.openSnackbar('Nie masz uprawnień do tej czynności')
          throw err;
        }
        this.downloadFile(err, 'db.sql');
        throw err;
      })
    ).subscribe();
  }

  importEvents(event: any): void {
    const file = event.target.files[0];
    if(file) {
      const formData = new FormData();
      formData.append('file', file);
      this.service.importEvents(formData).pipe(
        tap((response) => {
          this.openSnackbar('Poprawnie zaimportowano wydarzenia');
        })
      ).subscribe();
    }
    event.target.value = null;
  }

  exportEvents(): void {
    this.service.exportEvents().pipe(
      tap((response) => {
        if(!response.length) {
          this.openSnackbar('Brak wydarzeń do wyeksporowania');
        } else {
          this.downloadFile(response, 'events.json');
        }
      }),
      catchError((err) => {
        throw err;
      })
    ).subscribe();
  }

  private downloadFile(response: any, fileName: string): void {
    let binaryData = [];
    binaryData.push(response?.error?.text ?? JSON.stringify(response));
    let downloadLink = document.createElement('a');
    downloadLink.href = window.URL.createObjectURL(new Blob(binaryData as any, { type: 'blob' }));
    downloadLink.setAttribute('download', fileName);
    document.body.appendChild(downloadLink);
    downloadLink.click();
  }


  private convertDateToUnixTimestamp(date: Date): number {
    return date.getTime() / 1000;
  }

  openSnackbar(message: string): void {
    this.snackBar.open(message, 'Zamknij', {
      duration: 2000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    })
  }
}
