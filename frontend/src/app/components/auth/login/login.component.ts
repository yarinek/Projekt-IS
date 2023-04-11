import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form = new UntypedFormGroup({
    username: new UntypedFormControl(null, Validators.required),
    password: new UntypedFormControl(null, Validators.required),
  })

  constructor(private service: AuthService, private router: Router) {}

  ngOnInit(): void {
    const token = sessionStorage.getItem('token');
    if(!!token) {
      this.router.navigate(['crypto-list']);
    }
  }

  login(): void {
    this.service.login(this.form.value).pipe(
      tap((response) => {
        const token = response.headers.get('authorization')
        sessionStorage.setItem('token', token);
        this.router.navigate(['crypto-list']);
      })
    ).subscribe();
  } 
}
