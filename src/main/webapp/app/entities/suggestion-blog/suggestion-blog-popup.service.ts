import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { SuggestionBlog } from './suggestion-blog.model';
import { SuggestionBlogService } from './suggestion-blog.service';
@Injectable()
export class SuggestionBlogPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private suggestionBlogService: SuggestionBlogService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.suggestionBlogService.find(id).subscribe((suggestionBlog) => {
                suggestionBlog.create = this.datePipe
                    .transform(suggestionBlog.create, 'yyyy-MM-ddThh:mm');
                this.suggestionBlogModalRef(component, suggestionBlog);
            });
        } else {
            return this.suggestionBlogModalRef(component, new SuggestionBlog());
        }
    }

    suggestionBlogModalRef(component: Component, suggestionBlog: SuggestionBlog): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.suggestionBlog = suggestionBlog;
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
