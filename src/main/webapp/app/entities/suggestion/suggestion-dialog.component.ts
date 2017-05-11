import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Suggestion } from './suggestion.model';
import { SuggestionPopupService } from './suggestion-popup.service';
import { SuggestionService } from './suggestion.service';
import { User, UserService } from '../../shared';
import { AccountService } from '../../shared/auth/account.service';

@Component({
    selector: 'jhi-suggestion-dialog',
    templateUrl: './suggestion-dialog.component.html'
})
export class SuggestionDialogComponent implements OnInit {

    suggestion: Suggestion;
    authorities: any[];
    isSaving: boolean;

    users: User[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private suggestionService: SuggestionService,
        private userService: UserService,
        private eventManager: EventManager,
        private accountService: AccountService
    ) {
        this.jhiLanguageService.setLocations(['suggestion']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));

        this.suggestion.create = this.getLocalDate();
        this.accountService.get().subscribe(
            author => {
                this.suggestion.author = author
            }
        );
        
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.suggestion.id !== undefined) {
            this.suggestionService.update(this.suggestion)
                .subscribe((res: Suggestion) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.suggestionService.create(this.suggestion)
                .subscribe((res: Suggestion) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Suggestion) {
        this.eventManager.broadcast({ name: 'suggestionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    getLocalDate() {
    let time = new Date().toLocaleTimeString();
    let fecha = new Date().toLocaleDateString();
    return fecha + ' ' + time;
  }

}

@Component({
    selector: 'jhi-suggestion-popup',
    template: ''
})
export class SuggestionPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private suggestionPopupService: SuggestionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.suggestionPopupService
                    .open(SuggestionDialogComponent, params['id']);
            } else {
                this.modalRef = this.suggestionPopupService
                    .open(SuggestionDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
