import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SuggestionsAppSharedModule } from '../../shared';
import { SuggestionsAppAdminModule } from '../../admin/admin.module';
import {
    SuggestionBlogService,
    SuggestionBlogPopupService,
    SuggestionBlogComponent,
    SuggestionBlogDetailComponent,
    SuggestionBlogDialogComponent,
    SuggestionBlogPopupComponent,
    SuggestionBlogDeletePopupComponent,
    SuggestionBlogDeleteDialogComponent,
    suggestionBlogRoute,
    suggestionBlogPopupRoute,
} from './';

const ENTITY_STATES = [
    ...suggestionBlogRoute,
    ...suggestionBlogPopupRoute,
];

@NgModule({
    imports: [
        SuggestionsAppSharedModule,
        SuggestionsAppAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SuggestionBlogComponent,
        SuggestionBlogDetailComponent,
        SuggestionBlogDialogComponent,
        SuggestionBlogDeleteDialogComponent,
        SuggestionBlogPopupComponent,
        SuggestionBlogDeletePopupComponent,
    ],
    entryComponents: [
        SuggestionBlogComponent,
        SuggestionBlogDialogComponent,
        SuggestionBlogPopupComponent,
        SuggestionBlogDeleteDialogComponent,
        SuggestionBlogDeletePopupComponent,
    ],
    providers: [
        SuggestionBlogService,
        SuggestionBlogPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SuggestionsAppSuggestionBlogModule {}
