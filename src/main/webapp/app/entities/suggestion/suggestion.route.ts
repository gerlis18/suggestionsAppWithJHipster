import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { SuggestionComponent } from './suggestion.component';
import { SuggestionDetailComponent } from './suggestion-detail.component';
import { SuggestionPopupComponent } from './suggestion-dialog.component';
import { SuggestionDeletePopupComponent } from './suggestion-delete-dialog.component';

import { Principal } from '../../shared';

export const suggestionRoute: Routes = [
  {
    path: 'suggestion',
    component: SuggestionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'suggestionsApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'suggestion/:id',
    component: SuggestionDetailComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'suggestionsApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const suggestionPopupRoute: Routes = [
  {
    path: 'suggestion-new',
    component: SuggestionPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'suggestionsApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'suggestion/:id/edit',
    component: SuggestionPopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'suggestionsApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'suggestion/:id/delete',
    component: SuggestionDeletePopupComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'suggestionsApp.suggestion.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
