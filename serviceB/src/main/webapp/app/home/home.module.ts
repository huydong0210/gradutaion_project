import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE, HOME_ROUTE_AUTH } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE, HOME_ROUTE_AUTH])],
  declarations: [HomeComponent],
})
export class HomeModule {}
