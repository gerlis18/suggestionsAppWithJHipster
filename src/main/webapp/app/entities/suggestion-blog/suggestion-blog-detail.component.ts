import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { SuggestionBlog } from './suggestion-blog.model';
import { SuggestionBlogService } from './suggestion-blog.service';
import { Principal } from '../../shared/auth/principal.service';

@Component({
    selector: 'jhi-suggestion-blog-detail',
    templateUrl: './suggestion-blog-detail.component.html'
})
export class SuggestionBlogDetailComponent implements OnInit, OnDestroy {

    suggestionBlog: SuggestionBlog;
    private subscription: any;
    private eventSubscriber: Subscription;

    currentAccount: any;
    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private suggestionBlogService: SuggestionBlogService,
        private route: ActivatedRoute,
        private principal: Principal
    ) {
        this.jhiLanguageService.setLocations(['suggestionBlog']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSuggestionBlogs();
    }

    load(id) {
        this.suggestionBlogService.find(id).subscribe((suggestionBlog) => {
            this.suggestionBlog = suggestionBlog;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSuggestionBlogs() {
        this.eventSubscriber = this.eventManager.subscribe('suggestionBlogListModification', (response) => this.load(this.suggestionBlog.id));
    }
}
