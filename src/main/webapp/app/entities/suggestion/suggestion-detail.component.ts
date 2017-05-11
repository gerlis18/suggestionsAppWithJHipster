import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Suggestion } from './suggestion.model';
import { SuggestionService } from './suggestion.service';
import { Principal } from '../../shared/auth/principal.service';

@Component({
    selector: 'jhi-suggestion-detail',
    templateUrl: './suggestion-detail.component.html'
})
export class SuggestionDetailComponent implements OnInit, OnDestroy {

    suggestion: Suggestion;
    private subscription: any;
    private eventSubscriber: Subscription;
    private currentAccount: any;
    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private suggestionService: SuggestionService,
        private route: ActivatedRoute,
        private principal: Principal
    ) {
        this.jhiLanguageService.setLocations(['suggestion']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSuggestions();
    }

    load(id) {
        this.suggestionService.find(id).subscribe((suggestion) => {
            this.suggestion = suggestion;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSuggestions() {
        this.eventSubscriber = this.eventManager.subscribe('suggestionListModification', (response) => this.load(this.suggestion.id));
    }
}
