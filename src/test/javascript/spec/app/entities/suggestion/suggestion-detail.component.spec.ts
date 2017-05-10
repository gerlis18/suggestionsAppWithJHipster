import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { SuggestionsAppTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SuggestionDetailComponent } from '../../../../../../main/webapp/app/entities/suggestion/suggestion-detail.component';
import { SuggestionService } from '../../../../../../main/webapp/app/entities/suggestion/suggestion.service';
import { Suggestion } from '../../../../../../main/webapp/app/entities/suggestion/suggestion.model';

describe('Component Tests', () => {

    describe('Suggestion Management Detail Component', () => {
        let comp: SuggestionDetailComponent;
        let fixture: ComponentFixture<SuggestionDetailComponent>;
        let service: SuggestionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SuggestionsAppTestModule],
                declarations: [SuggestionDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SuggestionService,
                    EventManager
                ]
            }).overrideComponent(SuggestionDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SuggestionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuggestionService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Suggestion(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.suggestion).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
