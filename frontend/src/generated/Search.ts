/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { SearchType } from "./globalTypes";

// ====================================================
// GraphQL query operation: Search
// ====================================================

export interface Search_search {
  __typename: "SearchItem";
  name: string;
  url: string;
  searchType: SearchType;
}

export interface Search {
  search: Search_search[];
}

export interface SearchVariables {
  value: string;
}
