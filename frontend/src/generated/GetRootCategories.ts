/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

// ====================================================
// GraphQL query operation: GetRootCategories
// ====================================================

export interface GetRootCategories_listRootCategories {
  __typename: "CategoryBasic";
  id: string;
  name: string;
  url: string;
}

export interface GetRootCategories {
  listRootCategories: GetRootCategories_listRootCategories[];
}
