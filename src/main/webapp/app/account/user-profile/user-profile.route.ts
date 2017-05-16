import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { UserProfileComponent } from './user-profile.component';

export const userRoute: Route = {
  path: 'user/:login',
  component: UserProfileComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'global.menu.account.userProfile'
  },
  canActivate: [UserRouteAccessService]
};
