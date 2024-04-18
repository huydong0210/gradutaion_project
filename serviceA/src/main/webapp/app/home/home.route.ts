import { Route } from '@angular/router';

import { HomeComponent } from './home.component';
import { HomeAuthGuard } from './home-auth.guard';

export const HOME_ROUTE: Route = {
  path: '',
  component: HomeComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
export const HOME_ROUTE_AUTH: Route = {
  path: 'login/oauth2/code/oidc',
  component: HomeComponent,
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
  canActivate: [HomeAuthGuard],
};
