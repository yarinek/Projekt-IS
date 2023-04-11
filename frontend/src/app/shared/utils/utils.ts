import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import * as moment from 'moment';

export function dateLessThanToday(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if(!control.value) {
            return null;
        }

        return moment(control.value).isAfter(moment()) ? {dateAfterToday: true} : null;
    }
}


export const passwordMatchValidator: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => { 
    let pass = group.get('password')?.value;
    let confirmPass = group.get('matchingPassword')?.value
    return pass === confirmPass ? null : { notSame: true }
  }