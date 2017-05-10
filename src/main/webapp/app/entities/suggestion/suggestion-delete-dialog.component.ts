import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Suggestion } from './suggestion.model';
import { SuggestionPopupService } from './suggestion-popup.service';
import { SuggestionService } from './suggestion.service';

@Component({
    selector: 'jhi-suggestion-delete-dialog',
    templateUrl: './suggestion-delete-dialog.component.html'
})
export class SuggestionDeleteDialogComponent {

    suggestion: Suggestion;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private suggestionService: SuggestionService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['suggestion']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.suggestionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'suggestionListModification',
                content: 'Deleted an suggestion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-suggestion-delete-popup',
    template: ''
})
export class SuggestionDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private suggestionPopupService: SuggestionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.suggestionPopupService
                .open(SuggestionDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
