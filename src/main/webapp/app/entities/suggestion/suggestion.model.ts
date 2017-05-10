import { User } from '../../shared';
export class Suggestion {
    constructor(
        public id?: number,
        public title?: string,
        public message?: string,
        public create?: any,
        public addressee?: User,
        public author?: User,
    ) {
    }
}
