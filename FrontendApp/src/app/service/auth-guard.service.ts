import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  routeURL: string;

  constructor(private router: Router) {
    this.routeURL = this.router.url;
  }


  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {

    return new Promise((resolve, reject) => {
        if (sessionStorage.getItem('currentUser') == null) {
          this.routeURL = '/';
          this.router.navigate(['/'], {
            queryParams: {
              return: 'login'
            }
          });
          return resolve(false);
        } else {
          this.routeURL = this.router.url;
          return resolve(true);
        }

    });
  }
}
