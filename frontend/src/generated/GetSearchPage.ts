/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { SearchType } from "./globalTypes";

// ====================================================
// GraphQL query operation: GetSearchPage
// ====================================================

export interface GetSearchPage_search {
  __typename: "SearchItem";
  name: string;
  url: string;
  searchType: SearchType;
}

export interface GetSearchPage_listRootCategories {
  __typename: "CategoryBasic";
  id: string;
  name: string;
  url: string;
}

export interface GetSearchPage {
  search: GetSearchPage_search[];
  listRootCategories: GetSearchPage_listRootCategories[];
}

export interface GetSearchPageVariables {
  value: string;
}
