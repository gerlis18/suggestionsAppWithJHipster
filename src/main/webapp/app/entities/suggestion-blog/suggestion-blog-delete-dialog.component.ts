import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { SuggestionBlog } from './suggestion-blog.model';
import { SuggestionBlogPopupService } from './suggestion-blog-popup.service';
import { SuggestionBlogService } from './suggestion-blog.service';

@Component({
    selector: 'jhi-suggestion-blog-delete-dialog',
    templateUrl: './suggestion-blog-delete-dialog.component.html'
})
export class SuggestionBlogDeleteDialogComponent {

    suggestionBlog: SuggestionBlog;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private suggestionBlogService: SuggestionBlogService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['suggestionBlog']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.suggestionBlogService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'suggestionBlogListModification',
                content: 'Deleted an suggestionBlog'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-suggestion-blog-delete-popup',
    template: ''
})
export class SuggestionBlogDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private suggestionBlogPopupService: SuggestionBlogPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.suggestionBlogPopupService
                .open(SuggestionBlogDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
