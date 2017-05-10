import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SuggestionsAppSuggestionModule } from './suggestion/suggestion.module';
import { SuggestionsAppCategoryModule } from './category/category.module';
import { SuggestionsAppSuggestionBlogModule } from './suggestion-blog/suggestion-blog.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SuggestionsAppSuggestionModule,
        SuggestionsAppCategoryModule,
        SuggestionsAppSuggestionBlogModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SuggestionsAppEntityModule {}
