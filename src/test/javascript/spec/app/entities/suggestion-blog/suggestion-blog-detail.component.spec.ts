import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { SuggestionsAppTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SuggestionBlogDetailComponent } from '../../../../../../main/webapp/app/entities/suggestion-blog/suggestion-blog-detail.component';
import { SuggestionBlogService } from '../../../../../../main/webapp/app/entities/suggestion-blog/suggestion-blog.service';
import { SuggestionBlog } from '../../../../../../main/webapp/app/entities/suggestion-blog/suggestion-blog.model';

describe('Component Tests', () => {

    describe('SuggestionBlog Management Detail Component', () => {
        let comp: SuggestionBlogDetailComponent;
        let fixture: ComponentFixture<SuggestionBlogDetailComponent>;
        let service: SuggestionBlogService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SuggestionsAppTestModule],
                declarations: [SuggestionBlogDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SuggestionBlogService,
                    EventManager
                ]
            }).overrideComponent(SuggestionBlogDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SuggestionBlogDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SuggestionBlogService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SuggestionBlog(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.suggestionBlog).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
