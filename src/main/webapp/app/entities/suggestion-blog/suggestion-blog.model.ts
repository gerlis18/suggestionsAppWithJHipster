import { User } from '../../shared';
import { Category } from '../category';
export class SuggestionBlog {
    constructor(
        public id?: number,
        public title?: string,
        public content?: string,
        public create?: any,
        public author?: User,
        public category?: Category,
    ) {
    }
}
