import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Suggestion } from './suggestion.model';
import { SuggestionService } from './suggestion.service';
@Injectable()
export class SuggestionPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private suggestionService: SuggestionService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.suggestionService.find(id).subscribe((suggestion) => {
                suggestion.create = this.datePipe
                    .transform(suggestion.create, 'yyyy-MM-ddThh:mm');
                this.suggestionModalRef(component, suggestion);
            });
        } else {
            return this.suggestionModalRef(component, new Suggestion());
        }
    }

    suggestionModalRef(component: Component, suggestion: Suggestion): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.suggestion = suggestion;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
