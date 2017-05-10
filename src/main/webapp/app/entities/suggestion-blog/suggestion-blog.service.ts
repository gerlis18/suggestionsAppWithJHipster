import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { SuggestionBlog } from './suggestion-blog.model';
import { DateUtils } from 'ng-jhipster';
@Injectable()
export class SuggestionBlogService {

    private resourceUrl = 'api/suggestion-blogs';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(suggestionBlog: SuggestionBlog): Observable<SuggestionBlog> {
        const copy: SuggestionBlog = Object.assign({}, suggestionBlog);
        copy.create = this.dateUtils.toDate(suggestionBlog.create);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(suggestionBlog: SuggestionBlog): Observable<SuggestionBlog> {
        const copy: SuggestionBlog = Object.assign({}, suggestionBlog);

        copy.create = this.dateUtils.toDate(suggestionBlog.create);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<SuggestionBlog> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            jsonResponse.create = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.create);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: any): any {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].create = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].create);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
