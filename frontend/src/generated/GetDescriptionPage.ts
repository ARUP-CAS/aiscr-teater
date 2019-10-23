/* tslint:disable */
/* eslint-disable */
// This file was automatically generated and should not be edited.

import { CategoryType } from "./globalTypes";

// ====================================================
// GraphQL query operation: GetDescriptionPage
// ====================================================

export interface GetDescriptionPage_categoryBreadcrumbsWithUrl {
  __typename: "CategoryBasic";
  name: string;
  url: string;
}

export interface GetDescriptionPage_singleCategoryWithUrl_children {
  __typename: "CategoryBasic";
  id: string;
  name: string;
  categoryType: CategoryType;
  url: string;
  isLeaf: boolean;
}

export interface GetDescriptionPage_singleCategoryWithUrl_parentsNeighbors {
  __typename: "CategoryBasic";
  id: string;
  name: string;
  categoryType: CategoryType;
  url: string;
  isLeaf: boolean;
}

export interface GetDescriptionPage_singleCategoryWithUrl_neighbors {
  __typename: "CategoryBasic";
  id: string;
  name: string;
  categoryType: CategoryType;
  url: string;
  isLeaf: boolean;
}

export interface GetDescriptionPage_singleCategoryWithUrl {
  __typename: "Category";
  id: string;
  name: string;
  description: string;
  children: GetDescriptionPage_singleCategoryWithUrl_children[];
  parentsNeighbors: GetDescriptionPage_singleCategoryWithUrl_parentsNeighbors[];
  neighbors: GetDescriptionPage_singleCategoryWithUrl_neighbors[];
}

export interface GetDescriptionPage {
  categoryBreadcrumbsWithUrl: GetDescriptionPage_categoryBreadcrumbsWithUrl[];
  singleCategoryWithUrl: GetDescriptionPage_singleCategoryWithUrl;
}

export interface GetDescriptionPageVariables {
  url: string;
}
