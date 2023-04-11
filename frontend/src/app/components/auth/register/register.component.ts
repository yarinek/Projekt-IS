import { Component } from '@angular/core';
import { AbstractControl, UntypedFormControl, UntypedFormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { catchError, tap } from 'rxjs';
import { passwordMatchValidator } from 'src/app/shared/utils/utils';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  form = new UntypedFormGroup({
    username: new UntypedFormControl(null, Validators.required),
    password: new UntypedFormControl(null, Validators.required),
    matchingPassword: new UntypedFormControl(null, Validators.required),
    email: new UntypedFormControl(null, [Validators.required, Validators.email])
  }, {validators: passwordMatchValidator})

  get notSamePassword(): boolean {
    const {password, matchingPassword} = this.form.controls;
    //@ts-ignore
    return password.touched && matchingPassword.touched && this.form?.errors?.notSame;
  };

  constructor(private service: AuthService, private router: Router, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    const token = sessionStorage.getItem('token');
    if(!!token) {
      this.router.navigate(['crypto-list']);
    }
  }

  register(): void {
    this.service.register(this.form.value).pipe(
      tap(() => {
        this.router.navigate(['login']);
      }),
      catchError((err: HttpErrorResponse): any => {
        if(err.error.message === 'Password is not strong enough') {
          this.openSnackbar('Hasło nie jest wystarczająco mocne')
        }
        throw err;
      })
    ).subscribe();
  } 

  private openSnackbar(message: string): void {
    this.snackBar.open(message, 'Zamknij', {
      duration: 2000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    })
  }
}
