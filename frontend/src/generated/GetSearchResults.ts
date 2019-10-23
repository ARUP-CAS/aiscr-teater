/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { SearchType } from "./globalTypes";

// ====================================================
// GraphQL query operation: GetSearchResults
// ====================================================

export interface GetSearchResults_search {
  __typename: "SearchItem";
  name: string;
  url: string;
  searchType: SearchType;
}

export interface GetSearchResults {
  search: GetSearchResults_search[];
}

export interface GetSearchResultsVariables {
  value: string;
  limit?: number | null;
}
