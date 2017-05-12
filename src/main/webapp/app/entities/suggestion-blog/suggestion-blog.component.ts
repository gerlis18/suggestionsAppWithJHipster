import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { SuggestionBlog } from './suggestion-blog.model';
import { SuggestionBlogService } from './suggestion-blog.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-suggestion-blog',
    templateUrl: './suggestion-blog.component.html'
})
export class SuggestionBlogComponent implements OnInit, OnDestroy {
suggestionBlogs: SuggestionBlog[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private suggestionBlogService: SuggestionBlogService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
        this.jhiLanguageService.setLocations(['suggestionBlog']);
    }

    loadAll() {
        this.suggestionBlogService.query().subscribe(
            (res: Response) => {
                this.suggestionBlogs = res.json();
                console.log(this.suggestionBlogs)
            },
            (res: Response) => this.onError(res.json())
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSuggestionBlogs();
        
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SuggestionBlog) {
        return item.id;
    }
    registerChangeInSuggestionBlogs() {
        this.eventSubscriber = this.eventManager.subscribe('suggestionBlogListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    getImageUrl(imageUrl) {
        return this.isAuthenticated() ? imageUrl : null;
    }
}
