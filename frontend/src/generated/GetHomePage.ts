/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: GetHomePage
// ====================================================

export interface GetHomePage_listRootCategories {
  __typename: "CategoryBasic";
  id: string;
  name: string;
  url: string;
}

export interface GetHomePage {
  listRootCategories: GetHomePage_listRootCategories[];
}
