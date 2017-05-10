import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SuggestionsAppSharedModule } from '../../shared';
import { SuggestionsAppAdminModule } from '../../admin/admin.module';
import {
    SuggestionService,
    SuggestionPopupService,
    SuggestionComponent,
    SuggestionDetailComponent,
    SuggestionDialogComponent,
    SuggestionPopupComponent,
    SuggestionDeletePopupComponent,
    SuggestionDeleteDialogComponent,
    suggestionRoute,
    suggestionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...suggestionRoute,
    ...suggestionPopupRoute,
];

@NgModule({
    imports: [
        SuggestionsAppSharedModule,
        SuggestionsAppAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SuggestionComponent,
        SuggestionDetailComponent,
        SuggestionDialogComponent,
        SuggestionDeleteDialogComponent,
        SuggestionPopupComponent,
        SuggestionDeletePopupComponent,
    ],
    entryComponents: [
        SuggestionComponent,
        SuggestionDialogComponent,
        SuggestionPopupComponent,
        SuggestionDeleteDialogComponent,
        SuggestionDeletePopupComponent,
    ],
    providers: [
        SuggestionService,
        SuggestionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SuggestionsAppSuggestionModule {}
