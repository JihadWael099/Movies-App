import { Movie } from "./movie";

export interface SearchResponse {

    search: Movie[]; 
    totalResults: string;
    response: string;
}
