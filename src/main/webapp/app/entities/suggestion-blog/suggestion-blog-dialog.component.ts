import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { SuggestionBlog } from './suggestion-blog.model';
import { SuggestionBlogPopupService } from './suggestion-blog-popup.service';
import { SuggestionBlogService } from './suggestion-blog.service';
import { User, UserService } from '../../shared';
import { Category, CategoryService } from '../category';
import { AccountService } from '../../shared/auth/account.service';

@Component({
    selector: 'jhi-suggestion-blog-dialog',
    templateUrl: './suggestion-blog-dialog.component.html'
})
export class SuggestionBlogDialogComponent implements OnInit {

    suggestionBlog: SuggestionBlog;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    categories: Category[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private suggestionBlogService: SuggestionBlogService,
        private userService: UserService,
        private categoryService: CategoryService,
        private eventManager: EventManager,
        private accountService: AccountService
    ) {
        this.jhiLanguageService.setLocations(['suggestionBlog']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.categoryService.query().subscribe(
            (res: Response) => { this.categories = res.json(); }, (res: Response) => this.onError(res.json()));
        this.accountService.get().subscribe(
            user => {
                this.suggestionBlog.author = user
            }
        );

        this.suggestionBlog.create = this.getLocalDate();
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.suggestionBlog.id !== undefined) {
            this.suggestionBlogService.update(this.suggestionBlog)
                .subscribe((res: SuggestionBlog) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.suggestionBlogService.create(this.suggestionBlog)
                .subscribe((res: SuggestionBlog) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: SuggestionBlog) {
        this.eventManager.broadcast({ name: 'suggestionBlogListModification', content: 'OK' });
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

    trackCategoryById(index: number, item: Category) {
        return item.id;
    }

    getLocalDate() {
        let time = new Date().toLocaleTimeString();
        let fecha = new Date().toLocaleDateString();
        return fecha + ' ' + time;
    }
}

@Component({
    selector: 'jhi-suggestion-blog-popup',
    template: ''
})
export class SuggestionBlogPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private suggestionBlogPopupService: SuggestionBlogPopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.modalRef = this.suggestionBlogPopupService
                    .open(SuggestionBlogDialogComponent, params['id']);
            } else {
                this.modalRef = this.suggestionBlogPopupService
                    .open(SuggestionBlogDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
