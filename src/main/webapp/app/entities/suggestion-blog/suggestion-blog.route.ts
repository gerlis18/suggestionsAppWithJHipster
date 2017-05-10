import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { SuggestionBlogComponent } from './suggestion-blog.component';
import { SuggestionBlogDetailComponent } from './suggestion-blog-detail.component';
import { SuggestionBlogPopupComponent } from './suggestion-blog-dialog.component';
import { SuggestionBlogDeletePopupComponent } from './suggestion-blog-delete-dialog.component';

import { Principal } from '../../shared';

export const suggestionBlogRoute: Routes = [
  {
    path: 'suggestion-blog',
    component: SuggestionBlogComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'suggestionsApp.suggestionBlog.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'suggestion-blog/:id',
    component: SuggestionBlogDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'suggestionsApp.suggestionBlog.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const suggestionBlogPopupRoute: Routes = [
  {
    path: 'suggestion-blog-new',
    component: SuggestionBlogPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'suggestionsApp.suggestionBlog.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'suggestion-blog/:id/edit',
    component: SuggestionBlogPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'suggestionsApp.suggestionBlog.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'suggestion-blog/:id/delete',
    component: SuggestionBlogDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'suggestionsApp.suggestionBlog.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
