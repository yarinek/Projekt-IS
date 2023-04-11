import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CryptoListService {

  constructor(private http: HttpClient) { }

  getCurrency(currency:string): Observable<any> {
    return this.http.get<any>(`https://api.coingecko.com/api/v3/coins/markets?vs_currency=${currency}&order=market_cap_desc&sparkline=false`);
  }

  getTrendingCurrency(currency:string): Observable<any> {
    return this.http.get<any>(`https://api.coingecko.com/api/v3/coins/markets?vs_currency=${currency}&order=gecko_desc&per_page=10&page=1&sparkline=false&price_change_percentage=24h`)
  }
  
  getGrpahicalCurrencyData(coinId:string, currency:string, days: number): Observable<any> {
    return this.http.get<any>(`https://api.coingecko.com/api/v3/coins/${coinId}/market_chart?vs_currency=${currency}&days=${days}`)
  }

  getGrpahicalCurrencyDataRange(coinId:string, currency:string, from: number, to: number): Observable<any> {
    return this.http.get<any>(`https://api.coingecko.com/api/v3/coins/${coinId}/market_chart/range?vs_currency=${currency}&from=${from}&to=${to}`)
  }

  getCurrencyById(coinId:string): Observable<any>{
    return this.http.get<any>(`https://api.coingecko.com/api/v3/coins/${coinId}`)
  }

  getEvents(startDate: string, endDate: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'text/xml'
    })

    const body = `
          <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://localhost/soap">
        <soapenv:Header/>
        <soapenv:Body>
            <soap:getEventsRequest>
              <soap:startDate>${startDate}</soap:startDate>
              <soap:endDate>${endDate}</soap:endDate>
            </soap:getEventsRequest>
        </soapenv:Body>
      </soapenv:Envelope>
    `
    return this.http.post('/api/ws', body, {responseType: 'text', headers }).pipe(
      map((xmlString: any) => {
        const asJson = this.xmlStringToJson(xmlString);
        return asJson;
      })
    )
  }

  importDatabase(formData: FormData): Observable<void> {
    const headers = new HttpHeaders({
      'Authorization': sessionStorage.getItem('token') ?? ''
    })
    
    return this.http.post<void>('/server/api/rest/db/import', formData, {headers});
  }

  exportDatabase(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': sessionStorage.getItem('token') ?? ''
    })
    return this.http.get<void>(`/server/api/rest/db/export`, {headers});
  }

  importEvents(formData: FormData): Observable<void> {
    const headers = new HttpHeaders({
      'Authorization': sessionStorage.getItem('token') ?? ''
    })
    
    return this.http.post<void>('/server/api/rest/events/import', formData, {headers});
  }

  exportEvents(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': sessionStorage.getItem('token') ?? ''
    })
    return this.http.get<void>(`/server/api/rest/events/export`, {headers});
  }

  private xmlStringToJson(xml: string)
  {
    const oParser = new DOMParser();
    const xmlDoc = oParser.parseFromString(xml, "application/xml");
    return this.xmlToJson(xmlDoc);
  }

  private xmlToJson(xml: any)
  {
    // Create the return object
    var obj: any = {};

    if (xml.nodeType == 1) { // element
      // do attributes
      if (xml.attributes.length > 0) {
      obj["@attributes"] = {};
        for (var j = 0; j < xml.attributes.length; j++) {
          var attribute = xml.attributes.item(j);
          obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
        }
      }
    } else if (xml.nodeType == 3) { // text
      obj = xml.nodeValue;
    }

    // do children
    if (xml.hasChildNodes()) {
      for(var i = 0; i < xml.childNodes.length; i++) {
        var item = xml.childNodes.item(i);
        var nodeName = item.nodeName;
        if (typeof(obj[nodeName]) == "undefined") {
          obj[nodeName] = this.xmlToJson(item);
        } else {
          if (typeof(obj[nodeName].push) == "undefined") {
            var old = obj[nodeName];
            obj[nodeName] = [];
            obj[nodeName].push(old);
          }
          obj[nodeName].push(this.xmlToJson(item));
        }
      }
    }
    return obj;
  }
}
