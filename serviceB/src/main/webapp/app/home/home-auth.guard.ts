import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../login/login.service';

@Injectable({
  providedIn: 'root',
})
export class HomeAuthGuard implements CanActivate {
  constructor(private router: Router, private loginService: LoginService) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (route.queryParams.code) {
      this.loginService.getAccessTokenFromCode(route.queryParams.code).subscribe(res => {
        const access_token: string = res.access_token;
        const refresh_token: string = res.refresh_token;
        const id_token: string = res.id_token;
        localStorage.setItem('authenticationToken', access_token);
        localStorage.setItem('refresh_token', refresh_token);
        localStorage.setItem('id_token', id_token);
        sessionStorage.setItem('is_reload', 'true');
        this.router.navigate(['']);
      });
    }

    return true;
  }
}
