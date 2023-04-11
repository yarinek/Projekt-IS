import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { CoinVisualisationComponent } from './components/crypto-list/coin-visualisation/coin-visualisation.component';
import { CryptoListComponent } from './components/crypto-list/crypto-list.component';
import { AuthGuard } from './core/auth-guard.guard';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'crypto-list', component: CryptoListComponent, canActivate: [AuthGuard] },
  { path: 'coin-detail/:id', component: CoinVisualisationComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
