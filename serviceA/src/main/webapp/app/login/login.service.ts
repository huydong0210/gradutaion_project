import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { Login } from './login.model';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private applicationConfigService: ApplicationConfigService,
    private http: HttpClient
  ) {}

  login(): Observable<any> {
    return this.http.get(this.applicationConfigService.getEndpointFor('/api/login'));
  }

  logout(): void {
    this.authServerProvider.logout().subscribe({ complete: () => this.accountService.authenticate(null) });
  }
  getAccessTokenFromCode(code: string): Observable<any> {
    return this.http.get(SERVER_API_URL + '/api/access-token?code=' + code);
  }

  logoutKeycloak(): Observable<any> {
    const id_token: string | null = localStorage.getItem('id_token') ?? '';
    return this.http.get(SERVER_API_URL + '/api/logout?idToken=' + `${id_token}`);
  }
}
