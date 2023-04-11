import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { loginDTO, registerDTO } from './auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  login(body: loginDTO): Observable<any> {
    return this.http.post(`/api/login`, body, {observe: 'response'});
  }

  register(body: registerDTO): Observable<void> {
    return this.http.post<void>(`/api/api/rest/register`, body);
  }
}
